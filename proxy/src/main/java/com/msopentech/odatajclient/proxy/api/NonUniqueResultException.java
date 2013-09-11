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
package com.msopentech.odatajclient.proxy.api;

/**
 * Thrown when <tt>Query.getSingleResult()</tt> or <tt>EntityQuery.getSingleResult()</tt> is executed on a query and
 * there is more than one result from the query.
 *
 * @see Query#getSingleResult()
 * @see EntityQuery#getSingleResult()
 */
public class NonUniqueResultException extends RuntimeException {

    private static final long serialVersionUID = 4444551737338550185L;

    public NonUniqueResultException() {
        super();
    }
}
