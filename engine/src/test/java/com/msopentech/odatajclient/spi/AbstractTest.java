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
package com.msopentech.odatajclient.spi;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTest {

    /**
     * Logger.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractTest.class);

    protected static String testODataServiceRootURL;

    @BeforeClass
    public static void setUpConnIdBundles() throws IOException {
        InputStream propStream = null;
        try {
            propStream = AbstractTest.class.getResourceAsStream("/test.properties");
            Properties props = new Properties();
            props.load(propStream);

            testODataServiceRootURL = props.getProperty("test.odata.serviceroot.url");
        } catch (Exception e) {
            LOG.error("Could not load test.properties", e);
        } finally {
            if (propStream != null) {
                propStream.close();
            }
        }
        assertNotNull("Check value for the 'testODataServiceRootURL' property", testODataServiceRootURL);
    }
}