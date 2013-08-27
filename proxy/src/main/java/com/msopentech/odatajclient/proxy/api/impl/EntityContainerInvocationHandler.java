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

import com.msopentech.odatajclient.proxy.api.EntityContainerFactory;
import com.msopentech.odatajclient.proxy.api.Utility;
import com.msopentech.odatajclient.proxy.api.annotations.EntityContainer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class EntityContainerInvocationHandler extends AbstractInvocationHandler {

    protected final String schemaName;

    private final String entityContainerName;

    private final boolean defaultEntityContainer;

    public EntityContainerInvocationHandler(final Class<?> ref, final EntityContainerFactory factory) {
        super(factory);

        if (!ref.isInterface()) {
            throw new IllegalArgumentException(ref.getName() + " is not an interface");
        }

        Annotation annotation = ref.getAnnotation(EntityContainer.class);
        if (!(annotation instanceof EntityContainer)) {
            throw new IllegalArgumentException(ref.getName()
                    + " is not annotated as @" + EntityContainer.class.getSimpleName());
        }
        this.entityContainerName = ((EntityContainer) annotation).name();
        this.defaultEntityContainer = ((EntityContainer) annotation).isDefaultEntityContainer();
        this.schemaName = Utility.getNamespace(ref);
    }

    public boolean isDefaultEntityContainer() {
        return defaultEntityContainer;
    }

    public String getEntityContainerName() {
        return entityContainerName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final Annotation[] methodAnnots = method.getAnnotations();
        // 1. access top-level entity sets
        if (methodAnnots.length == 0) {
            final Class<?> returnType = method.getReturnType();

            return Proxy.newProxyInstance(returnType.getClassLoader(), new Class<?>[] {returnType},
                    EntitySetInvocationHandler.getInstance(returnType, this));
        } // 2. invoke function imports
        else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}