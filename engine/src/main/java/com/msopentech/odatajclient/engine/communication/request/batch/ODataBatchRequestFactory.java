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

import com.msopentech.odatajclient.engine.data.ODataURIBuilder;

/**
 * OData batch request factory class.
 */
public final class ODataBatchRequestFactory {

    private ODataBatchRequestFactory() {
        // Empty private constructor for static utility classes
    }

    /**
     * Gets a batch request object instance.
     *
     * @return new ODataBatchRequest instance.
     */
    public static ODataBatchRequest getBatchRequest(final String serviceRoot) {
        return new ODataBatchRequest(new ODataURIBuilder(serviceRoot).appendBatchSegment().build());
    }
}
