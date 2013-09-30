/**
 * Copyright © Microsoft Open Technologies, Inc.
 *
 * All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * THIS CODE IS PROVIDED *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 * ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A
 * PARTICULAR PURPOSE, MERCHANTABILITY OR NON-INFRINGEMENT.
 *
 * See the Apache License, Version 2.0 for the specific language
 * governing permissions and limitations under the License.
 */
package com.msopentech.odatajclient.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.msopentech.odatajclient.engine.client.http.HttpMethod;
import com.msopentech.odatajclient.engine.data.ODataReader;
import com.msopentech.odatajclient.engine.data.metadata.EdmMetadata;
import com.msopentech.odatajclient.engine.data.metadata.EdmType;
import com.msopentech.odatajclient.engine.data.metadata.edm.EntityContainer;
import com.msopentech.odatajclient.engine.data.metadata.edm.EntityType;
import com.msopentech.odatajclient.engine.data.metadata.edm.FunctionImport;
import com.msopentech.odatajclient.engine.data.metadata.edm.Schema;
import java.util.List;
import org.junit.Test;

public class MetadataTest extends AbstractTest {

    @Test
    public void parse() {
        final EdmMetadata metadata = ODataReader.readMetadata(getClass().getResourceAsStream("metadata.xml"));
        assertNotNull(metadata);

        final EdmType orderCollection =
                new EdmType(metadata, "Collection(Microsoft.Test.OData.Services.AstoriaDefaultService.Order)");
        assertNotNull(orderCollection);
        assertTrue(orderCollection.isCollection());
        assertFalse(orderCollection.isSimpleType());
        assertFalse(orderCollection.isEnumType());
        assertFalse(orderCollection.isComplexType());
        assertTrue(orderCollection.isEntityType());

        final EntityType order = orderCollection.getEntityType();
        assertNotNull(order);
        assertEquals("Order", order.getName());

        final EdmType stream = new EdmType(metadata, "Edm.Stream");
        assertNotNull(stream);
        assertFalse(stream.isCollection());
        assertTrue(stream.isSimpleType());
        assertFalse(stream.isEnumType());
        assertFalse(stream.isComplexType());
        assertFalse(stream.isEntityType());

        final List<FunctionImport> functionImports =
                metadata.getSchemas().get(0).getDefaultEntityContainer().getFunctionImports();
        int legacyGetters = 0;
        int legacyPosters = 0;
        int actions = 0;
        int functions = 0;
        for (FunctionImport functionImport : functionImports) {
            if (HttpMethod.GET.name().equals(functionImport.getHttpMethod())) {
                legacyGetters++;
            } else if (HttpMethod.POST.name().equals(functionImport.getHttpMethod())) {
                legacyPosters++;
            } else if (functionImport.getHttpMethod() == null) {
                if (functionImport.isSideEffecting()) {
                    actions++;
                } else {
                    functions++;
                }
            }
        }
        assertEquals(6, legacyGetters);
        assertEquals(1, legacyPosters);
        assertEquals(5, actions);
        assertEquals(0, functions);
    }

    @Test
    public void multipleSchemas() {
        final EdmMetadata metadata = ODataReader.readMetadata(getClass().getResourceAsStream("northwind-metadata.xml"));
        assertNotNull(metadata);

        final Schema first = metadata.getSchema("NorthwindModel");
        assertNotNull(first);

        final Schema second = metadata.getSchema("ODataWebV3.Northwind.Model");
        assertNotNull(second);

        final EntityContainer entityContainer = second.getDefaultEntityContainer();
        assertNotNull(entityContainer);
        assertEquals("NorthwindEntities", entityContainer.getName());
    }
}
