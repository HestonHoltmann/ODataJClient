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

import com.msopentech.odatajclient.engine.uri.ODataURIBuilder;
import com.msopentech.odatajclient.engine.utils.URIUtils;
import com.msopentech.odatajclient.proxy.api.EntityContainerFactory;
import com.msopentech.odatajclient.proxy.api.annotations.EntityContainer;
import com.msopentech.odatajclient.proxy.api.annotations.FunctionImport;
import com.msopentech.odatajclient.proxy.utils.ClassUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.commons.lang3.ArrayUtils;

public class EntityContainerInvocationHandler extends AbstractInvocationHandler {

    private static final long serialVersionUID = 7379006755693410764L;

    private final EntityContainerFactory factory;

    protected final String schemaName;

    private final String entityContainerName;

    private final boolean defaultEC;

    public static EntityContainerInvocationHandler getInstance(
            final Class<?> ref, final EntityContainerFactory factory) {

        final EntityContainerInvocationHandler instance = new EntityContainerInvocationHandler(ref, factory);
        instance.containerHandler = instance;
        return instance;
    }

    private EntityContainerInvocationHandler(final Class<?> ref, final EntityContainerFactory factory) {
        super(null);

        final Annotation annotation = ref.getAnnotation(EntityContainer.class);
        if (!(annotation instanceof EntityContainer)) {
            throw new IllegalArgumentException(ref.getName()
                    + " is not annotated as @" + EntityContainer.class.getSimpleName());
        }

        this.factory = factory;
        this.entityContainerName = ((EntityContainer) annotation).name();
        this.defaultEC = ((EntityContainer) annotation).isDefaultEntityContainer();
        this.schemaName = ClassUtils.getNamespace(ref);
    }

    EntityContainerFactory getFactory() {
        return factory;
    }

    boolean isDefaultEntityContainer() {
        return defaultEC;
    }

    String getEntityContainerName() {
        return entityContainerName;
    }

    String getSchemaName() {
        return schemaName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (isSelfMethod(method, args)) {
            return invokeSelfMethod(method, args);
        } else if ("flush".equals(method.getName()) && ArrayUtils.isEmpty(args)) {
            new Container(factory).flush();
            return ClassUtils.returnVoid();
        } else {
            final Annotation[] methodAnnots = method.getAnnotations();
            // 1. access top-level entity sets
            if (methodAnnots.length == 0) {
                final Class<?> returnType = method.getReturnType();

                return Proxy.newProxyInstance(
                        Thread.currentThread().getContextClassLoader(),
                        new Class<?>[] {returnType},
                        EntitySetInvocationHandler.getInstance(returnType, this));
            } // 2. invoke function imports
            else if (methodAnnots[0] instanceof FunctionImport) {
                final com.msopentech.odatajclient.engine.data.metadata.edm.EntityContainer container =
                        getFactory().getMetadata().getSchema(schemaName).getEntityContainer(entityContainerName);
                final com.msopentech.odatajclient.engine.data.metadata.edm.EntityContainer.FunctionImport funcImp =
                        container.getFunctionImport(((FunctionImport) methodAnnots[0]).name());

                final ODataURIBuilder uriBuilder = new ODataURIBuilder(factory.getServiceRoot()).
                        appendFunctionImportSegment(URIUtils.rootFunctionImportURISegment(container, funcImp));

                return functionImport((FunctionImport) methodAnnots[0], method, args, uriBuilder.build(), funcImp);
            } else {
                throw new UnsupportedOperationException("Method not found: " + method);
            }
        }
    }
}
