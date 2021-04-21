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
import org.bson.Document;
import org.json.JSONArray;
import org.wso2.carbon.connector.connection.MongoConnection;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.connector.core.connection.ConnectionHandler;
import org.wso2.carbon.connector.exception.MongoConnectorException;
import org.wso2.carbon.connector.utils.MongoConstants;
import org.wso2.carbon.connector.utils.MongoUtils;
import org.wso2.carbon.connector.utils.SimpleMongoClient;

/**
 * Class mediator for finding many documents.
 * For more information, see https://docs.mongodb.com/manual/reference/method/db.collection.find
 */
public class Find extends AbstractConnector {

    private static final String COLLECTION = "collection";
    private static final String QUERY = "query";
    private static final String PROJECTION = "projection";
    private static final String COLLATION = "collation";
    private static final String SORT = "sort";
    private static final String LIMIT = "limit";
    private static final String FIND_RESULT = "Find Result : ";
    private static final String INVALID_MONGODB_CONFIG_MESSAGE = "MongoDB connection has not been instantiated.";
    private static final String ERROR_MESSAGE = "Error occurred while searching for the documents in the database.";

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {

        ConnectionHandler handler = ConnectionHandler.getConnectionHandler();
        SimpleMongoClient simpleMongoClient;

        String collection = (String) getParameter(messageContext, COLLECTION);
        String query = (String) getParameter(messageContext, QUERY);
        String projection = (String) getParameter(messageContext, PROJECTION);
        String collation = (String) getParameter(messageContext, COLLATION);
        String sort = (String) getParameter(messageContext, SORT);
        String limitString = (String) getParameter(messageContext, LIMIT);

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

            int limit = 0;
            if (StringUtils.isNotEmpty(limitString)) {
                limit = Integer.parseInt(limitString);
            }

            JSONArray findResult = simpleMongoClient.findDocuments(collection, queryDoc, projection, collation, sort, limit);

            if (log.isDebugEnabled()) {
                log.debug(FIND_RESULT + findResult);
            }
            MongoUtils.setPayload(messageContext, findResult.toString());

        } catch (IllegalArgumentException e) {
            MongoUtils.handleError(messageContext, e, MongoConstants.MONGODB_CONNECTIVITY, ERROR_MESSAGE);

        } catch (MongoException e) {
            MongoUtils.handleError(messageContext, e, e.getCode(), ERROR_MESSAGE);

        } catch (Exception e) {
            MongoUtils.handleError(messageContext, e, MongoConstants.MONGODB_UNKNOWN_EXCEPTION, ERROR_MESSAGE);
        }
    }
}
