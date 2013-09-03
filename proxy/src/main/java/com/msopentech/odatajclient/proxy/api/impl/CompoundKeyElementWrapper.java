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
package com.msopentech.odatajclient.proxy.api.impl;

import java.lang.reflect.Method;

class CompoundKeyElementWrapper implements Comparable<CompoundKeyElementWrapper> {

    private final String name;

    private final Method method;

    private final int position;

    protected CompoundKeyElementWrapper(final String name, final Method method, final int position) {
        this.name = name;
        this.method = method;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public Method getMethod() {
        return method;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public int compareTo(final CompoundKeyElementWrapper other) {
        if (other == null) {
            return 1;
        } else {
            return getPosition() > other.getPosition() ? 1 : getPosition() == other.getPosition() ? 0 : -1;
        }
    }
}
