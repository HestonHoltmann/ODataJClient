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

import com.msopentech.odatajclient.engine.data.metadata.edm.EdmSimpleType;
import java.util.List;

public class LineString extends ComposedGeospatial<Point> {

    public LineString(final Dimension dimension, final List<Point> points) {
        super(dimension, Type.LINESTRING, points);
    }

    @Override
    public EdmSimpleType getEdmSimpleType() {
        return dimension == Dimension.GEOGRAPHY
                ? EdmSimpleType.GEOGRAPHY_LINE_STRING
                : EdmSimpleType.GEOMETRY_LINE_STRING;
    }
}
