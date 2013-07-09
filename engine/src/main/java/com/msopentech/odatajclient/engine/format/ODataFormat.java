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
package com.msopentech.odatajclient.engine.format;

import org.apache.http.entity.ContentType;

/**
 * Available formats to be used in various contexts.
 */
public enum ODataFormat {

    /**
     * JSON format with no metadata.
     */
    JSON_NO_METADATA(ContentType.APPLICATION_JSON.getMimeType() + ";odata=nometadata"),
    /**
     * JSON format with minimal metadata (default).
     */
    JSON(ContentType.APPLICATION_JSON.getMimeType() + ";odata=minimalmetadata"),
    /**
     * JSON format with no metadata.
     */
    JSON_FULL_METADATA(ContentType.APPLICATION_JSON.getMimeType() + ";odata=fullmetadata"),
    /**
     * XML format.
     */
    XML(ContentType.APPLICATION_XML.getMimeType());

    private final String format;

    ODataFormat(final String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return format;
    }

    public static ODataFormat fromString(final String format) {
        ODataFormat result = null;

        for (ODataFormat value : values()) {
            if (format.equals(value.toString())) {
                result = value;
            }
        }

        if (result == null) {
            throw new IllegalArgumentException(format);
        }

        return result;
    }
}
