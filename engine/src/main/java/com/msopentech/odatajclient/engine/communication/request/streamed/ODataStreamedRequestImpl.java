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
package com.msopentech.odatajclient.engine.communication.request.streamed;

import com.msopentech.odatajclient.engine.client.http.HttpClientException;
import com.msopentech.odatajclient.engine.communication.request.ODataRequestImpl;
import com.msopentech.odatajclient.engine.communication.request.ODataStreamer;
import com.msopentech.odatajclient.engine.communication.request.ODataStreamingManagement;
import com.msopentech.odatajclient.engine.communication.request.batch.ODataBatchRequest;
import com.msopentech.odatajclient.engine.communication.response.ODataResponse;
import com.msopentech.odatajclient.engine.utils.Configuration;
import com.msopentech.odatajclient.engine.utils.ODataBatchConstants;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;

/**
 * Streamed OData request abstract class.
 *
 * @param <V> OData response type corresponding to the request implementation.
 * @param <T> OData request payload type corresponding to the request implementation.
 */
public abstract class ODataStreamedRequestImpl<V extends ODataResponse, T extends ODataStreamingManagement<V>>
        extends ODataRequestImpl implements ODataStreamedRequest<V, T> {

    /**
     * OData request payload.
     */
    protected ODataStreamingManagement<V> payload = null;

    private Future<HttpResponse> futureRes = null;

    /**
     * Constructor.
     *
     * @param method OData request HTTP method.
     * @param uri OData request URI.
     */
    public ODataStreamedRequestImpl(final Method method, final URI uri) {
        super(method, uri);
        setAccept(ContentType.APPLICATION_OCTET_STREAM.getMimeType());
        setContentType(ContentType.APPLICATION_OCTET_STREAM.getMimeType());
    }

    /**
     * Gets OData request payload management object.
     *
     * @return OData request payload management object.
     */
    protected abstract T getPayload();

    /**
     * {@inheritDoc }
     */
    @Override
    @SuppressWarnings("unchecked")
    public T execute() {
        payload = getPayload();

        ((HttpEntityEnclosingRequestBase) request).setEntity(new InputStreamEntity(payload.getBody(), -1));

        futureRes = Configuration.getExecutor().submit(new Executor());

        // return the payload object
        return (T) payload;
    }

    /**
     * Writes (and consume) the request onto the given batch stream.
     * <p>
     * Please note that this method will consume the request (execution won't be possible anymore).
     *
     * @param req destination batch request.
     */
    public void batch(final ODataBatchRequest req) {
        batch(req, null);
    }

    public void batch(final ODataBatchRequest req, final String contentId) {
        final InputStream input = getPayload().getBody();

        try {
            // finalize the body
            getPayload().finalizeBody();

            req.rawAppend(toByteArray());
            if (StringUtils.isNotBlank(contentId)) {
                req.rawAppend((ODataBatchConstants.CHANGESET_CONTENT_ID_NAME + ": " + contentId).getBytes());
                req.rawAppend(ODataStreamer.CRLF);
            }
            req.rawAppend(ODataStreamer.CRLF);

            req.rawAppend(IOUtils.toByteArray(input));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    protected HttpResponse getResponse() {
        try {
            return futureRes.get(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.error("Failure executing request");
            throw new HttpClientException(e);
        }
    }

    private class Executor implements Callable<HttpResponse> {

        @Override
        public HttpResponse call() throws Exception {
            return doExecute();
        }
    }
}