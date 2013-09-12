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
package com.msopentech.odatajclient.proxy.performance;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.msopentech.odatajclient.engine.client.http.AbstractBasicAuthHttpClientFactory;
import com.msopentech.odatajclient.engine.communication.request.retrieve.ODataEntityRequest;
import com.msopentech.odatajclient.engine.communication.request.retrieve.ODataRetrieveRequestFactory;
import com.msopentech.odatajclient.engine.communication.response.ODataRetrieveResponse;
import com.msopentech.odatajclient.engine.data.ODataEntity;
import com.msopentech.odatajclient.engine.data.ODataInlineEntity;
import com.msopentech.odatajclient.engine.data.ODataLink;
import com.msopentech.odatajclient.engine.format.ODataPubFormat;
import com.msopentech.odatajclient.engine.uri.ODataURIBuilder;
import com.msopentech.odatajclient.engine.utils.Configuration;
import com.msopentech.odatajclient.proxy.AbstractTest;
import com.msopentech.odatajclient.proxy.api.EntityContainerFactory;
import com.msopentech.odatajclient.proxy.defaultservice.microsoft.test.odata.services.astoriadefaultservice.DefaultContainer;
import com.msopentech.odatajclient.proxy.defaultservice.microsoft.test.odata.services.astoriadefaultservice.types.Customer;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runners.MethodSorters;

@BenchmarkOptions(warmupRounds = 25, benchmarkRounds = 1000)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BasicPerfTestITCase extends AbstractTest {

    @Rule
    public TestRule benchmarkRun = new BenchmarkRule();

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

    private void engine(final ODataPubFormat format) {
        final ODataURIBuilder uriBuilder = new ODataURIBuilder(testAuthServiceRootURL).
                appendEntityTypeSegment("Customer").appendKeySegment(-10).expand("Info");

        final ODataEntityRequest req = ODataRetrieveRequestFactory.getEntityRequest(uriBuilder.build());
        req.setFormat(format);

        final ODataRetrieveResponse<ODataEntity> res = req.execute();
        final ODataEntity entity = res.getBody();
        assertNotNull(entity);

        boolean found = false;
        for (ODataLink link : entity.getNavigationLinks()) {
            if (link instanceof ODataInlineEntity && "Info".equals(link.getName())) {
                final ODataEntity info = ((ODataInlineEntity) link).getEntity();
                assertNotNull(info);
                found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void engineFromAtom() {
        engine(ODataPubFormat.ATOM);
    }

    @Test
    public void engineFromJSON() {
        engine(ODataPubFormat.JSON_FULL_METADATA);
    }

    private void proxy() {
        EntityContainerFactory.getContext().detachAll();
        container = EntityContainerFactory.getInstance(testAuthServiceRootURL).
                getEntityContainer(DefaultContainer.class);

        final Customer customer = container.getCustomer().get(-10);
        assertNotNull(customer);
        assertNotNull(customer.getInfo());
    }

    @Test
    public void proxyFromAtom() {
        Configuration.setDefaultPubFormat(ODataPubFormat.ATOM);
        proxy();
        Configuration.setDefaultPubFormat(ODataPubFormat.JSON_FULL_METADATA);
    }

    @Test
    public void proxyFromJSON() {
        proxy();
    }
}
