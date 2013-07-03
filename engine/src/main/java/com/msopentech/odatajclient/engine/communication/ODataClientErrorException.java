/*
 * Copyright 2013 MS OpenTech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.msopentech.odatajclient.engine.communication;

import com.msopentech.odatajclient.engine.data.ODataError;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.StatusLine;

/**
 * Represents a client error in OData.
 *
 * @see ODataError
 */
public class ODataClientErrorException extends RuntimeException {

    private static final long serialVersionUID = -2551523202755268162L;

    private final StatusLine statusLine;

    private final ODataError error;

    public ODataClientErrorException(final StatusLine statusLine) {
        super(statusLine.toString());

        this.statusLine = statusLine;
        this.error = null;
    }

    public ODataClientErrorException(final StatusLine statusLine, final ODataError error) {
        super((StringUtils.isBlank(error.getCode()) ? StringUtils.EMPTY : "(" + error.getCode() + ") ")
                + error.getMessageValue() + " [" + statusLine.toString() + "]");

        this.statusLine = statusLine;
        this.error = error;

        if (this.error.getInnerErrorType() != null && this.error.getInnerErrorMessage() != null) {
            final RuntimeException cause =
                    new RuntimeException(this.error.getInnerErrorType() + ": " + this.error.getInnerErrorMessage());

            if (this.error.getInnerErrorStacktrace() != null) {
                List<String> stLines;
                try {
                    stLines = IOUtils.readLines(new StringReader(this.error.getInnerErrorStacktrace()));
                } catch (IOException e) {
                    stLines = Collections.<String>emptyList();
                }
                StackTraceElement[] stElements = new StackTraceElement[stLines.size()];
                for (int i = 0; i < stLines.size(); i++) {
                    final String stLine = stLines.get(i).substring(stLines.get(i).indexOf("at ") + 3);
                    final int lastDotPos = stLine.lastIndexOf('.');
                    stElements[i] = new StackTraceElement(
                            stLine.substring(0, lastDotPos), stLine.substring(lastDotPos + 1), null, 0);
                }
                cause.setStackTrace(stElements);
            }

            initCause(cause);
        }
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public ODataError getODataError() {
        return error;
    }
}
