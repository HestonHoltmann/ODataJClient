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
package com.msopentech.odatajclient.engine.it;

import com.msopentech.odatajclient.engine.client.http.AbstractBasicAuthHttpClientFactory;
import com.msopentech.odatajclient.engine.client.http.DefaultHttpClientFactory;
import com.msopentech.odatajclient.engine.utils.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class AuthEntityRetrieveTestITCase extends EntityRetrieveTestITCase {

    @BeforeClass
    public static void enableBasicAuth() {
        Configuration.setHttpClientFactory(new AbstractBasicAuthHttpClientFactory() {

            @Override
            protected String getUsername() {
                return "odatajclient";
            }

            @Override
            protected String getPassword() {
                return "odatajclient";
            }
        });
    }

    @AfterClass
    public static void disableBasicAuth() {
        Configuration.setHttpClientFactory(new DefaultHttpClientFactory());
    }

    @Override
    protected String getServiceRoot() {
        return testAuthServiceRootURL;
    }
}
