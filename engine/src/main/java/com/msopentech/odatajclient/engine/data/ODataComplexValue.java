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
package com.msopentech.odatajclient.engine.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * OData complex property value.
 */
public class ODataComplexValue extends ODataValue implements Iterable<ODataProperty> {

    /**
     * Complex type fields.
     */
    final Map<String, ODataProperty> fields = new HashMap<String, ODataProperty>();

    /**
     * Adds field to the complex type.
     *
     * @param field field to be added.
     */
    public void add(final ODataProperty field) {
        fields.put(field.getName(), field);
    }

    /**
     * Gets field.
     *
     * @param name name of the field to be retrieved.
     * @return requested field.
     */
    public ODataProperty get(final String name) {
        return fields.get(name);
    }

    /**
     * Complex property fields iterator.
     *
     * @return fields iterator.
     */
    @Override
    public Iterator<ODataProperty> iterator() {
        return fields.values().iterator();
    }
}