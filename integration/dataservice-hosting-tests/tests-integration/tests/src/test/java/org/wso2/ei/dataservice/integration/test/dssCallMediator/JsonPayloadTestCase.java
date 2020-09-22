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

package org.wso2.ei.dataservice.integration.test.dssCallMediator;

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

public class JsonPayloadTestCase extends ESBIntegrationTest {

    @BeforeClass(alwaysRun = true)
    public void serviceDeployment() throws Exception {
        super.init();
        verifyProxyServiceExistence("dssCallMediatorSourceTypeBodyProxy");
        verifyProxyServiceExistence("dssCallMediatorJsonPayloadAsPropertyProxy");
    }

    @Test(groups = {"wso2.esb"}, description = "Test DSS Call mediator with json payload request")
    public void testDSSCallMediatorWithJsonPayload() {
        String payload = "{\n" +
                "  \"addEmployee\": {\n" +
                "    \"EmployeeNumber\": \"1234\",\n" +
                "    \"Firstname\": \"Martin\",\n" +
                "    \"LastName\": \"Craig\",\n" +
                "    \"Email\": \"craig@wso2.com\",\n" +
                "    \"Salary\": \"1234\"\n" +
                "  }\n" +
                "}";
        String response = sendPostRequest("dssCallMediatorSourceTypeBodyProxy", payload);
        assertTrue(response.contains("SUCCESSFUL"), "Error adding employee record.");
    }

    @Test(groups = {"wso2.esb"}, description = "Test DSS Call mediator with json payload when source type is " +
            "configured to property")
    public void testDSSCallMediatorWithJsonPayloadAndSourceProperty() {
        String payload = "{\n" +
                "    \"addEmployee\": {\n" +
                "      \"EmployeeNumber\": \"456\",\n" +
                "      \"Firstname\": \"Jules\",\n" +
                "      \"LastName\": \"Emmet\",\n" +
                "      \"Email\": \"emmet@wso2.com\",\n" +
                "      \"Salary\": \"456\"\n" +
                "    }\n" +
                "}";
        String response = sendPostRequest("dssCallMediatorJsonPayloadAsPropertyProxy", payload);
        assertTrue(response.contains("SUCCESSFUL"), "Error adding employee record with source type property.");
    }

    @AfterClass
    public void cleanUp() throws Exception {
        super.cleanup();
    }

    private String sendPostRequest(String serviceName, String payload) {

        try {
            SimpleHttpClient simpleHttpClient = new SimpleHttpClient();
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            HttpResponse response = simpleHttpClient.doPost(getProxyServiceURLHttp(serviceName), headers, payload, "application/json");

            return simpleHttpClient.getResponsePayload(response);

        } catch (IOException exp) {
            throw new SynapseException("Error performing POST request to service " + serviceName, exp);
        }
    }
}
