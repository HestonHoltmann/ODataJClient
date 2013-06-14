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
package com.msopentech.odatajclient.engine.data.metadata.edm.geospatial;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Base class for all geospatial info.
 */
public abstract class Geospatial {

    public enum Dimension {

        GEOMETRY,
        GEOGRAPHY;

    }

    public enum Type {

        /**
         * The OGIS geometry type number for points.
         */
        POINT,
        /**
         * The OGIS geometry type number for lines.
         */
        LINESTRING,
        /**
         * The OGIS geometry type number for polygons.
         */
        POLYGON,
        /**
         * The OGIS geometry type number for aggregate points.
         */
        MULTIPOINT,
        /**
         * The OGIS geometry type number for aggregate lines.
         */
        MULTILINESTRING,
        /**
         * The OGIS geometry type number for aggregate polygons.
         */
        MULTIPOLYGON,
        /**
         * The OGIS geometry type number for feature collections.
         */
        GEOSPATIALCOLLECTION;

    }

    protected final Dimension dimension;

    protected final Type type;

    /**
     * Null value means it is expected to vary per instance.
     */
    protected Integer srid;

    protected Geospatial(final Dimension dimension, final Type type) {
        this.dimension = dimension;
        this.type = type;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Type getType() {
        return type;
    }

    public Integer getSrid() {
        return srid;
    }

    public void setSrid(final Integer srid) {
        this.srid = srid;
    }

    @Override
    public boolean equals(final Object obj) {
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
