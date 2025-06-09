/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.connector.connection;

import com.mongodb.MongoException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.exception.MongoConnectorException;
import org.wso2.carbon.connector.pojo.ConnectionConfiguration;
import org.wso2.carbon.connector.utils.MongoConstants;
import org.wso2.carbon.connector.utils.MongoUtils;
import org.wso2.carbon.connector.utils.SimpleMongoClient;
import org.wso2.integration.connector.core.connection.Connection;
import org.wso2.integration.connector.core.connection.ConnectionConfig;

/**
 * The MongoConnection object that handles all MongoDB operations
 * attached to the connection.
 */
public class MongoConnection implements Connection {

    private static final String CONNECTION_RESULT = "Connection URI : ";
    private static final String INVALID_URI_MESSAGE = "The connection URI string is invalid.";
    private static final String ERROR_MESSAGE = "Error occurred while connecting to the database.";

    protected Log log = LogFactory.getLog(this.getClass());
    private SimpleMongoClient simpleMongoClient;

    /**
     * Created the SimpleMongoClient object. And this object will contain the MongoClient object
     * with all the connection details.
     *
     * @param messageContext Message context object.
     * @param config         Connection configuration object.
     * @throws Exception In case of an error while setting the SimpleMongoClient object.
     */
    public MongoConnection(MessageContext messageContext, ConnectionConfiguration config) throws Exception {

        String connectionURI = setupConnection(config);

        if (connectionURI == null) {
            MongoConnectorException e = new MongoConnectorException(INVALID_URI_MESSAGE);
            MongoUtils.handleError(messageContext, e, MongoConstants.MONGODB_CONNECTIVITY, e.getMessage());

        } else {

            if (log.isDebugEnabled()) {
                log.debug(CONNECTION_RESULT + connectionURI);
            }

            try {
                this.simpleMongoClient = new SimpleMongoClient(connectionURI, config.getDatabase());

            } catch (IllegalArgumentException e) {
                MongoUtils.handleError(messageContext, e, MongoConstants.MONGODB_CONNECTIVITY, ERROR_MESSAGE);

            } catch (MongoException e) {
                MongoUtils.handleError(messageContext, e, e.getCode(), ERROR_MESSAGE);

            } catch (Exception e) {
                MongoUtils.handleError(messageContext, e, MongoConstants.MONGODB_UNKNOWN_EXCEPTION, ERROR_MESSAGE);
            }
        }
    }

    /**
     * Constructs the connection URI based on the protocol.
     *
     * @param config Input connection configuration.
     * @return Constructed connection URI string.
     * @throws MongoConnectorException
     */
    private String setupConnection(ConnectionConfiguration config) throws MongoConnectorException {

        ProtocolBasedConnectionSetup connectionSetup;

        switch (config.getProtocol()) {
            case STANDARD:
                connectionSetup = new StandardConnectionSetup();
                break;
            case DSL:
                connectionSetup = new DSLConnectionSetup();
                break;
            case URI:
                connectionSetup = new URIConnectionSetup();
                break;
            default:
                throw new IllegalStateException("Unexpected protocol value: " + config.getProtocol());
        }

        return connectionSetup.constructConnectionString(config);
    }

    /**
     * Gets the SimpleMongoClient object containing
     * the MongoDB connection.
     *
     * @return SimpleMongoClient
     */
    public SimpleMongoClient getSimpleMongoClient() {

        return simpleMongoClient;
    }

    @Override
    public void connect(ConnectionConfig config) {

        throw new UnsupportedOperationException("Nothing to do when connecting.");
    }

    @Override
    public void close() {

        if (simpleMongoClient != null) {
            this.simpleMongoClient.closeConnection();
        }
    }
}
