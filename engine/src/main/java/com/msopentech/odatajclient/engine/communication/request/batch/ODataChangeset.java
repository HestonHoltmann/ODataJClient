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
package com.msopentech.odatajclient.engine.communication.request.batch;

import com.msopentech.odatajclient.engine.communication.header.ODataHeaders;
import com.msopentech.odatajclient.engine.communication.request.ODataRequest.Method;
import com.msopentech.odatajclient.engine.communication.request.ODataRequestImpl;
import com.msopentech.odatajclient.engine.utils.ODataBatchConstants;
import java.util.UUID;

/**
 * Changeset wrapper for the corresponding batch item.
 */
public class ODataChangeset extends ODataBatchRequestItem {

    private int contentId = 0;

    private final String boundary;

    private final ODataChangesetResponseItem expectedResItem;

    /**
     * Constructor.
     * <p>
     * Send a changeset header.
     *
     * @param os piped output stream to be used to serialize.
     */
    ODataChangeset(final ODataBatchRequest req, final ODataChangesetResponseItem expectedResItem) {
        super(req);
        this.expectedResItem = expectedResItem;

        // create a random UUID value for boundary
        boundary = "changeset_" + UUID.randomUUID().toString();
    }

    /**
     * Close changeset item an send changeset request footer.
     */
    @Override
    protected void closeItem() {
        // stream close-delimiter
        if (hasStreamedSomething) {
            newLine();
            stream(("--" + boundary + "--").getBytes());
            newLine();
            newLine();
        }
    }

    /**
     * Serialize and send the given request.
     * <p>
     * An IllegalArgumentException is thrown in case of the given request is a GET.
     *
     * @param request request to be serialized.
     * @return current changeset item instance.
     */
    public ODataChangeset addRequest(final ODataBatchableRequest request) {
        if (!isOpen()) {
            throw new IllegalStateException("Current batch item is closed");
        }

        if (request.getMethod() == Method.GET) {
            throw new IllegalArgumentException("Invalid request. GET method not allowed in changeset");
        }

        if (!hasStreamedSomething) {
            stream((ODataHeaders.HeaderName.contentType.toString() + ": "
                    + ODataBatchConstants.MULTIPART_CONTENT_TYPE + ";boundary=" + boundary).getBytes());

            newLine();
            newLine();

            hasStreamedSomething = true;
        }

        contentId++;

        // preamble
        newLine();

        // stream batch-boundary
        stream(("--" + boundary).getBytes());
        newLine();

        // stream the request
        streamRequestHeader(request, contentId);

        request.batch(req, String.valueOf(contentId));
        newLine();

        // add request to the list
        expectedResItem.addResponse(String.valueOf(contentId), ((ODataRequestImpl) request).getResponseTemplate());
        return this;
    }
}
