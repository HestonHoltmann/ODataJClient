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
package com.msopentech.odatajclient.engine.uri.filter;

/**
 * Filter property path; obtain instances via <tt>ODataFilterArgFactory</tt>.
 *
 * @see com.msopentech.odatajclient.engine.uri.filter.ODataFilterArgFactory
 */
public class ODataFilterProperty implements ODataFilterArg {

    private final String propertyPath;

    ODataFilterProperty(final String value) {
        this.propertyPath = value;
    }

    @Override
    public String build() {
        return propertyPath;
    }
}
