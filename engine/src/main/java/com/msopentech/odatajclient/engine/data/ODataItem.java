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

import java.net.URI;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract representation of OData entities and links.
 */
public abstract class ODataItem {

    protected static final Logger LOG = LoggerFactory.getLogger(ODataItem.class);

    /**
     * OData item self link.
     */
    protected URI link;

    /**
     * OData entity name/type.
     */
    private final String name;

    /**
     * Constructor.
     *
     * @param name OData entity name.
     */
    public ODataItem(final String name) {
        this.name = name;
    }

    /**
     * Returns self link.
     *
     * @return entity edit link.
     */
    public URI getLink() {
        return link;
    }

    /**
     * Sets self link.
     *
     * @param self link.
     */
    public void setLink(final URI link) {
        this.link = link;
    }

    /**
     * Returns OData entity name.
     *
     * @return entity name.
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
