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

package org.wso2.carbon.connector.operations;

import com.mongodb.MongoException;
import org.apache.commons.lang.StringUtils;
import org.apache.synapse.MessageContext;
import org.apache.synapse.util.InlineExpressionUtil;
import org.bson.Document;
import org.jaxen.JaxenException;
import org.json.JSONObject;
import org.wso2.carbon.connector.connection.MongoConnection;
import org.wso2.carbon.connector.exception.MongoConnectorException;
import org.wso2.carbon.connector.utils.MongoConstants;
import org.wso2.carbon.connector.utils.MongoUtils;
import org.wso2.carbon.connector.utils.SimpleMongoClient;
import org.wso2.integration.connector.core.AbstractConnectorOperation;
import org.wso2.integration.connector.core.ConnectException;
import org.wso2.integration.connector.core.connection.ConnectionHandler;

/**
 * Class mediator for updating many documents.
 * For more information, see https://docs.mongodb.com/manual/reference/method/db.collection.updateMany
 */
public class UpdateMany extends AbstractConnectorOperation {

    private static final String COLLECTION = "collection";
    private static final String QUERY = "query";
    private static final String UPDATE = "update";
    private static final String UPSERT = "upsert";
    private static final String COLLATION = "collation";
    private static final String ARRAY_FILTERS = "arrayFilters";
    private static final String UPDATE_RESULT = "Update Result: ";
    private static final String INVALID_MONGODB_CONFIG_MESSAGE = "MongoDB connection has not been instantiated.";
    private static final String EMPTY_UPDATE_MESSAGE = "The update modification cannot be null or empty.";
    private static final String ERROR_MESSAGE = "Error occurred while updating the documents in the database.";

    @Override
    public void execute(MessageContext messageContext, String responseVariable, Boolean overwriteBody)
            throws ConnectException {

        ConnectionHandler handler = ConnectionHandler.getConnectionHandler();
        SimpleMongoClient simpleMongoClient;

        String collection = getMediatorParameter(messageContext, COLLECTION, String.class, false);
        String query = getMediatorParameter(messageContext, QUERY, String.class, false);
        String update = getMediatorParameter(messageContext, UPDATE, String.class, false);
        String upsert = getMediatorParameter(messageContext, UPSERT, String.class, false);
        String collation = getMediatorParameter(messageContext, COLLATION, String.class, true);
        String arrayFilters = getMediatorParameter(messageContext, ARRAY_FILTERS, String.class, true);

        if (StringUtils.isEmpty(update)) {
            MongoConnectorException e = new MongoConnectorException(EMPTY_UPDATE_MESSAGE);
            MongoUtils.handleError(messageContext, e, MongoConstants.MONGODB_CONNECTIVITY, e.getMessage());
        }

        try {
            String connectionName = MongoUtils.getConnectionName(messageContext);
            MongoConnection mongoConnection = (MongoConnection) handler.getConnection(MongoConstants.CONNECTOR_NAME, connectionName);
            simpleMongoClient = mongoConnection.getSimpleMongoClient();

            if (simpleMongoClient == null) {
                throw new MongoConnectorException(INVALID_MONGODB_CONFIG_MESSAGE);
            }

            if (StringUtils.isEmpty(query)) {
                query = "{}";
            }

            Document queryDoc = Document.parse(query);
            Document updateDoc = Document.parse(update);

            JSONObject updateResult = simpleMongoClient.updateManyDocuments(collection, queryDoc, updateDoc, upsert, collation, arrayFilters);
            if (log.isDebugEnabled()) {
                log.debug(UPDATE_RESULT + updateResult);
            }
            handleConnectorResponse(messageContext, responseVariable, overwriteBody, updateResult.toString(),
                    null, null);

        } catch (IllegalArgumentException e) {
            MongoUtils.handleError(messageContext, e, MongoConstants.MONGODB_CONNECTIVITY, ERROR_MESSAGE);

        } catch (MongoException e) {
            MongoUtils.handleError(messageContext, e, e.getCode(), ERROR_MESSAGE);

        } catch (Exception e) {
            MongoUtils.handleError(messageContext, e, MongoConstants.MONGODB_UNKNOWN_EXCEPTION, ERROR_MESSAGE);
        }
    }
}
