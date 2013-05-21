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
package com.msopentech.odatajclient.proxy.meta.query;

/**
 * Thrown when <tt>Query.getSingleResult()</tt> or <tt>EntityQuery.getSingleResult()</tt> is executed on a query
 * and there is no result to return.
 * @see Query#getSingleResult()
 * @see EntityQuery#getSingleResult()
 */
public class NoResultException extends RuntimeException {

    private static final long serialVersionUID = -6643642637364303053L;

    public NoResultException() {
        super();
    }

    public NoResultException(final String message) {
        super(message);
    }
}
