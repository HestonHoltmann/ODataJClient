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

public class AndFilter implements ODataFilter {

    private final ODataFilter left;

    private final ODataFilter right;

    public AndFilter(final ODataFilter left, final ODataFilter right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String build() {
        return new StringBuilder().
                append('(').append(left.build()).
                append(" and ").
                append(right.build()).append(')').
                toString();
    }
}
