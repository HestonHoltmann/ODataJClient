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
package com.msopentech.odatajclient.engine.data.metadata;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Metadata elements: <tt>&lt;ReferentialConstraint/&ht;</tt>
 */
public class ReferentialConstraint implements Serializable {

    private static final long serialVersionUID = 3255989627757784488L;

    private Set<AnnotationAttribute> annotationAttributes;

    private Documentation documentation;

    private Principal principal;

    private Dependent dependent;

    private List<AnnotationElement> annotationElements;

    public Set<AnnotationAttribute> getAnnotationAttributes() {
        return annotationAttributes;
    }

    public Documentation getDocumentation() {
        return documentation;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public Dependent getDependent() {
        return dependent;
    }

    public List<AnnotationElement> getAnnotationElements() {
        return annotationElements;
    }
}
