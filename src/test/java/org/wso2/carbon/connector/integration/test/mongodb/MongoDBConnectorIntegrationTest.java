/*
 *  Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.connector.integration.test.mongodb;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.connector.integration.test.mongodb.utils.EmbeddedMongoDatabase;
import org.wso2.connector.integration.test.base.ConnectorIntegrationTestBase;
import org.wso2.connector.integration.test.base.RestResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Integration test class for MongoDB connector
 */
public class MongoDBConnectorIntegrationTest extends ConnectorIntegrationTestBase {

    private final Map<String, String> esbRequestHeadersMap = new HashMap<>();
    EmbeddedMongoDatabase embeddedMongoDatabase = null;

    /**
     * Set up the environment for testing.
     */
    @BeforeClass(alwaysRun = true)
    public void setEnvironment() throws Exception {

        String connectorName = System.getProperty("connector_name") + "-connector-" + System.getProperty("connector_version") + ".zip";
        init(connectorName);

        esbRequestHeadersMap.put("Accept-Charset", "UTF-8");
        esbRequestHeadersMap.put("Content-Type", "application/json");
        esbRequestHeadersMap.put("Accept", "application/json");

        embeddedMongoDatabase = new EmbeddedMongoDatabase();
        embeddedMongoDatabase.startMongoServer();
    }

    /**
     * Clean up the environment after testing.
     */
    @AfterClass(alwaysRun = true)
    public void cleanEnvironment() throws Exception {

        embeddedMongoDatabase.stopMongoServer();
        cleanUpEsb();
    }

    /**
     * Positive test case for insertOne method.
     */
    @Test(groups = {"insert"}, description = "MongoDB Connector {insertOne} integration test.")
    public void insertOne() throws IOException, JSONException {

        String methodName = "insertOneProxy";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURLHttp(methodName), "POST", esbRequestHeadersMap, "insertOne.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        String insertResult = "{\"InsertOneResult\":\"Successful\"}";
        Assert.assertEquals(esbRestResponse.getBody().toString(), insertResult);
    }

    /**
     * Positive test case for insertMany method with mandatory parameters.
     */
    @Test(groups = {"insert"}, description = "MongoDB Connector {insertMany} integration test with mandatory parameters.")
    public void insertManyWithMandatoryParameters() throws IOException, JSONException {

        String methodName = "insertManyProxy";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURLHttp(methodName), "POST", esbRequestHeadersMap, "insertManyMandatory.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        String insertResult = "{\"InsertManyResult\":\"Successful\"}";
        Assert.assertEquals(esbRestResponse.getBody().toString(), insertResult);
    }

    /**
     * Positive test case for findOne method with default parameters.
     */
    @Test(groups = {"find"}, description = "MongoDB Connector {findOne} integration test with default parameters.", dependsOnGroups = {"insert"})
    public void findOneWithDefaultParameters() throws IOException, JSONException {

        String methodName = "findOneProxy";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURLHttp(methodName), "POST", esbRequestHeadersMap, "findOneDefault.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for findOne method with mandatory parameters.
     */
    @Test(groups = {"find"}, description = "MongoDB Connector {findOne} integration test with mandatory parameters.", dependsOnGroups = {"insert"})
    public void findOneWithMandatoryParameters() throws IOException, JSONException {

        String methodName = "findOneProxy";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURLHttp(methodName), "POST", esbRequestHeadersMap, "findOneMandatory.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        String findResult = "{\"name\":\"Jane Doe\",\"_id\":\"123\"}";
        Assert.assertTrue(esbRestResponse.getBody().toString().contains(findResult));
    }

    /**
     * Positive test case for find method with default parameters.
     */
    @Test(groups = {"find"}, description = "MongoDB Connector {find} integration test with default parameters.", dependsOnGroups = {"insert"})
    public void findWithDefaultParameters() throws IOException, JSONException {

        String methodName = "findProxy";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURLHttp(methodName), "POST", esbRequestHeadersMap, "findDefault.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        String findResult = "[{\\\"name\\\":\\\"Jane Doe\\\",\\\"_id\\\":\\\"123\\\"},{\\\"name\\\":\\\"John Doe\\\",\\\"_id\\\":\\\"1234\\\"}," +
                "{\\\"name\\\":\\\"Jane Doe\\\",\\\"_id\\\":\\\"12345\\\"},{\\\"name\\\":\\\"Jane Doe\\\",\\\"_id\\\":\\\"123456\\\"}]";
        Assert.assertTrue(esbRestResponse.getBody().toString().contains(findResult));
    }

    /**
     * Positive test case for find method with mandatory parameters.
     */
    @Test(groups = {"find"}, description = "MongoDB Connector {find} integration test with mandatory parameters.", dependsOnGroups = {"insert"})
    public void findWithMandatoryParameters() throws IOException, JSONException {

        String methodName = "findProxy";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURLHttp(methodName), "POST", esbRequestHeadersMap, "findMandatory.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        String findResult = "[{\\\"name\\\":\\\"Jane Doe\\\",\\\"_id\\\":\\\"123\\\"}]";
        Assert.assertTrue(esbRestResponse.getBody().toString().contains(findResult));
    }

    /**
     * Positive test case for updateOne method with default parameters.
     */
    @Test(groups = {"update-default"}, description = "MongoDB Connector {updateOne} integration test with default parameters.", dependsOnGroups = {
            "update-mandatory"})
    public void updateOneWithDefaultParameters() throws IOException, JSONException {

        String methodName = "updateOneProxy";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURLHttp(methodName), "POST", esbRequestHeadersMap, "updateOneDefault.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for updateOne method with mandatory parameters.
     */
    @Test(groups = {
            "update-mandatory"}, description = "MongoDB Connector {updateOne} integration test with mandatory parameters.", dependsOnGroups = {
            "find"})
    public void updateOneWithMandatoryParameters() throws IOException, JSONException {

        String methodName = "updateOneProxy";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURLHttp(methodName), "POST", esbRequestHeadersMap, "updateOneMandatory.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        String updateResult = "{\"modifiedCount\":1,\"matchedCount\":1}";
        Assert.assertEquals(esbRestResponse.getBody().toString(), updateResult);
    }

    /**
     * Positive test case for updateMany method with default parameters.
     */
    @Test(groups = {"update-default"}, description = "MongoDB Connector {updateMany} integration test with default parameters.", dependsOnGroups = {
            "update-mandatory"})
    public void updateManyWithDefaultParameters() throws IOException, JSONException {

        String methodName = "updateManyProxy";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURLHttp(methodName), "POST", esbRequestHeadersMap, "updateManyDefault.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        String updateResult = "{\"modifiedCount\":4,\"matchedCount\":4}";
        Assert.assertEquals(esbRestResponse.getBody().toString(), updateResult);
    }

    /**
     * Positive test case for updateMany method with mandatory parameters.
     */
    @Test(groups = {
            "update-mandatory"}, description = "MongoDB Connector {updateMany} integration test with mandatory parameters.", dependsOnGroups = {
            "find"}, dependsOnMethods = {"updateOneWithMandatoryParameters"})
    public void updateManyWithMandatoryParameters() throws IOException, JSONException {

        String methodName = "updateManyProxy";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURLHttp(methodName), "POST", esbRequestHeadersMap, "updateManyMandatory.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        String updateResult = "{\"modifiedCount\":1,\"matchedCount\":1}";
        Assert.assertEquals(esbRestResponse.getBody().toString(), updateResult);
    }

    /**
     * Positive test case for deleteOne method with default parameters.
     */
    @Test(description = "MongoDB Connector {deleteOne} integration test with default parameters.", dependsOnGroups = {"delete-mandatory"})
    public void deleteOneWithDefaultParameters() throws IOException, JSONException {

        String methodName = "deleteOneProxy";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURLHttp(methodName), "POST", esbRequestHeadersMap, "deleteOneDefault.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
    }

    /**
     * Positive test case for deleteOne method with mandatory parameters.
     */
    @Test(groups = {
            "delete-mandatory"}, description = "MongoDB Connector {deleteOne} integration test with mandatory parameters.", dependsOnGroups = {
            "update-default"})
    public void deleteOneWithMandatoryParameters() throws IOException, JSONException {

        String methodName = "deleteOneProxy";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURLHttp(methodName), "POST", esbRequestHeadersMap, "deleteOneMandatory.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        String deleteResult = "{\"deletedCount\":1}";
        Assert.assertEquals(esbRestResponse.getBody().toString(), deleteResult);
    }

    /**
     * Positive test case for deleteMany method with default parameters.
     */
    @Test(description = "MongoDB Connector {deleteMany} integration test with default parameters.", dependsOnGroups = {"delete-mandatory"})
    public void deleteManyWithDefaultParameters() throws IOException, JSONException {

        String methodName = "deleteManyProxy";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURLHttp(methodName), "POST", esbRequestHeadersMap, "deleteManyDefault.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        String deleteResult = "{\"deletedCount\":2}";
        Assert.assertEquals(esbRestResponse.getBody().toString(), deleteResult);
    }

    /**
     * Positive test case for deleteMany method with mandatory parameters.
     */
    @Test(groups = {
            "delete-mandatory"}, description = "MongoDB Connector {deleteMany} integration test with mandatory parameters.", dependsOnGroups = {
            "update-default"}, dependsOnMethods = {"deleteOneWithMandatoryParameters"})
    public void deleteManyWithMandatoryParameters() throws IOException, JSONException {

        String methodName = "deleteManyProxy";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURLHttp(methodName), "POST", esbRequestHeadersMap, "deleteManyMandatory.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        String deleteResult = "{\"deletedCount\":1}";
        Assert.assertEquals(esbRestResponse.getBody().toString(), deleteResult);
    }
}