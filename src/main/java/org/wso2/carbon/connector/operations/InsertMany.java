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
import org.bson.BsonArray;
import org.bson.BsonInvalidOperationException;
import org.bson.Document;
import org.wso2.carbon.connector.connection.MongoConnection;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.connector.core.connection.ConnectionHandler;
import org.wso2.carbon.connector.exception.MongoConnectorException;
import org.wso2.carbon.connector.utils.MongoConstants;
import org.wso2.carbon.connector.utils.MongoUtils;
import org.wso2.carbon.connector.utils.SimpleMongoClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Class mediator for inserting many documents.
 * For more information, see https://docs.mongodb.com/manual/reference/method/db.collection.insertMany
 */
public class InsertMany extends AbstractConnector {

    private static final String COLLECTION = "collection";
    private static final String DOCUMENTS = "documents";
    private static final String ORDERED = "ordered";
    private static final String INSERT_MANY_RESULT = "{\"InsertManyResult\":\"Successful\"}";
    private static final String INVALID_MONGODB_CONFIG_MESSAGE = "MongoDB connection has not been instantiated.";
    private static final String EMPTY_DOCUMENT_MESSAGE = "The documents to be inserted cannot be null or empty.";
    private static final String INVALID_DOCUMENTS_MESSAGE = "The document to be inserted cannot be an JSON object. Please provide a JSON array.";
    private static final String ERROR_MESSAGE = "Error occurred while inserting the documents to the database.";

    /**
     * The stringToDocumentList method converts a String into a list of
     * MongoDB Documents.
     *
     * @param stringDocuments String to be converted into a list.
     * @return List This returns the list of MongoDB Documents.
     */
    private static List<Document> stringToDocumentList(String stringDocuments) {

        BsonArray bsonArray = BsonArray.parse(stringDocuments);
        List<Document> documents = new ArrayList<>();

        for (Object object : bsonArray) {
            String singleStringDoc = object.toString();
            Document singleDocument = Document.parse(singleStringDoc);
            documents.add(singleDocument);
        }
        return documents;
    }

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {

        ConnectionHandler handler = ConnectionHandler.getConnectionHandler();
        SimpleMongoClient simpleMongoClient;

        String collection = (String) getParameter(messageContext, COLLECTION);
        String stringDocuments = (String) getParameter(messageContext, DOCUMENTS);
        String ordered = (String) getParameter(messageContext, ORDERED);

        if (StringUtils.isEmpty(stringDocuments)) {
            MongoConnectorException e = new MongoConnectorException(EMPTY_DOCUMENT_MESSAGE);
            MongoUtils.handleError(messageContext, e, MongoConstants.MONGODB_CONNECTIVITY, e.getMessage());
        }

        try {
            String connectionName = MongoUtils.getConnectionName(messageContext);
            MongoConnection mongoConnection = (MongoConnection) handler.getConnection(MongoConstants.CONNECTOR_NAME, connectionName);
            simpleMongoClient = mongoConnection.getSimpleMongoClient();

            if (simpleMongoClient == null) {
                throw new MongoConnectorException(INVALID_MONGODB_CONFIG_MESSAGE);
            }

            List<Document> documents = stringToDocumentList(stringDocuments);

            simpleMongoClient.insertManyDocuments(collection, documents, ordered);
            if (log.isDebugEnabled()) {
                log.debug(INSERT_MANY_RESULT);
            }
            MongoUtils.setPayload(messageContext, INSERT_MANY_RESULT);

        } catch (BsonInvalidOperationException e) {
            MongoUtils.handleError(messageContext, e, MongoConstants.MONGODB_CONNECTIVITY, INVALID_DOCUMENTS_MESSAGE);

        } catch (IllegalArgumentException e) {
            MongoUtils.handleError(messageContext, e, MongoConstants.MONGODB_CONNECTIVITY, ERROR_MESSAGE);

        } catch (MongoException e) {
            MongoUtils.handleError(messageContext, e, e.getCode(), ERROR_MESSAGE);

        } catch (Exception e) {
            MongoUtils.handleError(messageContext, e, MongoConstants.MONGODB_UNKNOWN_EXCEPTION, ERROR_MESSAGE);
        }
    }
}
