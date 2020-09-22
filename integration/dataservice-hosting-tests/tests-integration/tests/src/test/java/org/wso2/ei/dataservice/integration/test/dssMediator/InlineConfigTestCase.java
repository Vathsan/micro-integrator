/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.ei.dataservice.integration.test.dssMediator;

import org.apache.http.HttpResponse;
import org.apache.synapse.SynapseException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.esb.integration.common.utils.ESBIntegrationTest;
import org.wso2.esb.integration.common.utils.clients.SimpleHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertTrue;

public class InlineConfigTestCase extends ESBIntegrationTest {

    @BeforeClass(alwaysRun = true)
    public void serviceDeployment() throws Exception {
        super.init();
        verifyProxyServiceExistence("inlineSingleRequestProxy");
        verifyProxyServiceExistence("inlineBatchRequestProxy");
        verifyProxyServiceExistence("inlineRequestBoxProxy");
    }

    @Test(groups = {"wso2.esb"}, description = "Test DSS Call mediator inline configuration for single request")
    public void testInlineSingleRequest() {
        String responsePayload = sendGetRequest("inlineSingleRequestProxy");
        assertTrue(responsePayload.contains("SUCCESSFUL"), "Error adding employee record.");
    }

    @Test(groups = {"wso2.esb"}, description = "Test DSS Call mediator inline configuration for batch requests")
    public void testInlineBatchRequests() {
        String responsePayload = sendGetRequest("inlineBatchRequestProxy");
        assertTrue(responsePayload.contains("SUCCESSFUL"), "Error adding employee batch record.");
    }

    @Test(groups = {"wso2.esb"}, description = "Test DSS Call mediator inline configuration for request box")
    public void testInlineRequestBox() {
        String responsePayload = sendGetRequest("inlineRequestBoxProxy");
        assertTrue(responsePayload.contains("Ellie"), "First name Ellie not found. " +
                "Error performing request box operation.");
    }

    @AfterClass
    public void cleanUp() throws Exception {
        super.cleanup();
    }

    private String sendGetRequest(String serviceName) {

        try {
            SimpleHttpClient simpleHttpClient = new SimpleHttpClient();
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/xml");
            HttpResponse response = simpleHttpClient.doGet(getProxyServiceURLHttp(serviceName), headers);

            return simpleHttpClient.getResponsePayload(response);

        } catch (IOException exp) {
            throw new SynapseException("Error performing GET request to service " + serviceName, exp);
        }
    }
}