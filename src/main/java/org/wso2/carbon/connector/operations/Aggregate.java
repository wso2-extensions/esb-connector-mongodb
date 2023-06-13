/*
 *  Copyright (c) 2017-2023, WSO2 LLC (https://www.wso2.com).
 *
 *  WSO2 LLC licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.wso2.carbon.connector.operations;

import com.mongodb.MongoException;
import org.apache.commons.lang.StringUtils;
import org.apache.synapse.MessageContext;
import org.bson.BsonArray;
import org.bson.BsonInvalidOperationException;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Class mediator for adding aggregation operation.
 * For more information, see https://www.mongodb.com/docs/manual/reference/aggregation/
 */
public class Aggregate extends AbstractConnector {

    private static final String COLLECTION = "collection";
    private static final String STAGES = "stages";
    private static final String AGGREGATION_RESULT = "AggregationResult : ";
    private static final String INVALID_MONGODB_CONFIG_MESSAGE = "MongoDB connection has not been instantiated.";
    private static final String EMPTY_STAGE_MESSAGE = "Stages of the aggregation cannot be empty.";
    private static final String INVALID_DOCUMENTS_MESSAGE = "Not a valid aggregation stage.";
    private static final String ERROR_MESSAGE = "Error occurred while performing aggregation.";

    /**
     * The stringToDocumentList method converts a String into a list of
     * MongoDB Documents.
     *
     * @param stringStages String to be converted into a pipeline stages.
     * @return List This returns the list of MongoDB Documents.
     */
    private static List<Document> stringToStageList(String stringStages) {

        BsonArray bsonArray = BsonArray.parse(stringStages);
        List<Document> stages = new ArrayList<>();

        for (Object object : bsonArray) {
            String singleStringStage = object.toString();
            Document singleStage = Document.parse(singleStringStage);
            stages.add(singleStage);
        }
        return stages;
    }

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {

        ConnectionHandler handler = ConnectionHandler.getConnectionHandler();
        SimpleMongoClient simpleMongoClient;

        String collection = (String) getParameter(messageContext, COLLECTION);
        String stagesStrings = (String) getParameter(messageContext, STAGES);

        if (StringUtils.isEmpty(stagesStrings)) {
            MongoConnectorException e = new MongoConnectorException(EMPTY_STAGE_MESSAGE);
            MongoUtils.handleError(messageContext, e, MongoConstants.MONGODB_CONNECTIVITY, e.getMessage());
        }

        try {
            String connectionName = MongoUtils.getConnectionName(messageContext);
            MongoConnection mongoConnection = (MongoConnection) handler.getConnection(MongoConstants.CONNECTOR_NAME, connectionName);
            simpleMongoClient = mongoConnection.getSimpleMongoClient();

            if (simpleMongoClient == null) {
                throw new MongoConnectorException(INVALID_MONGODB_CONFIG_MESSAGE);
            }

            List<Document> stages = stringToStageList(stagesStrings);

            JSONArray aggregationResult = simpleMongoClient.aggregationPipeline(collection, stages);

            if (log.isDebugEnabled()) {
                log.debug(AGGREGATION_RESULT + aggregationResult.toString());
            }
            MongoUtils.setPayload(messageContext, aggregationResult.toString());

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
