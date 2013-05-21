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
package com.msopentech.odatajclient.proxy.meta;

import com.msopentech.odatajclient.engine.communication.request.ODataRequest;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark method as EDM function import.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FunctionImport {

    String name();

    /**
     * EntitySet and EntitySetPath are mutually exclusive.
     *
     * @return static EntitySet
     */
    Class<? extends Serializable> entitySet() default Serializable.class;

    /**
     * Defines the EntitySet that contains the entities that are returned by the FunctionImport when
     * that EntitySet is dependent on one of the FunctionImport parameters.
     *
     * @return EntitySet path, dependent on one of the FunctionImport parameters
     * @see Parameter
     */
    String entitySetPath() default "";

    String returnType();

    boolean isBindable() default false;

    boolean isAlwaysBindable() default false;

    /**
     * When httpMethod() is NONE, true: this annotates an action; false: this annotates a function
     * @return 
     */
    boolean isSideEffecting() default true;

    boolean isComposable() default false;

    /**
     * if not NONE, this annotates a legacy service operation.
     * @return 
     */
    ODataRequest.Method httpMethod() default ODataRequest.Method.NONE;
}
