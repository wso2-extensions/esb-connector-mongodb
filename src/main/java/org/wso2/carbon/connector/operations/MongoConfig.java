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

import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.SynapseEnvironment;
import org.wso2.carbon.connector.connection.MongoConnection;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.connector.core.connection.ConnectionHandler;
import org.wso2.carbon.connector.deploy.ConnectorUndeployObserver;
import org.wso2.carbon.connector.exception.MongoConnectorException;
import org.wso2.carbon.connector.pojo.ConnectionConfiguration;
import org.wso2.carbon.connector.pojo.ParamConnectionConfig;
import org.wso2.carbon.connector.pojo.StandardConnectionConfig;
import org.wso2.carbon.connector.pojo.URIConnectionConfig;
import org.wso2.carbon.connector.utils.MongoConstants;
import org.wso2.carbon.connector.utils.MongoUtils;

/**
 * Initializes the MongoDB connection based on provided configs
 * at config/init.xml.
 * <p>
 * For more information, see https://docs.mongodb.com/drivers/java
 * and https://docs.mongodb.com/manual/reference/connection-string
 */
public class MongoConfig extends AbstractConnector implements ManagedLifecycle {

    private static final String ERROR_MESSAGE = "Failed to initiate MongoDB connector configuration.";
    private static final String INVALID_PROTOCOL_MESSAGE = "Unknown MongoDB connector protocol.";

    @Override
    public void init(SynapseEnvironment synapseEnvironment) {

        SynapseConfiguration synapseConfig = synapseEnvironment.getSynapseConfiguration();
        synapseConfig.registerObserver(new ConnectorUndeployObserver(synapseConfig));
    }

    @Override
    public void destroy() {

        throw new UnsupportedOperationException("Destroy method of config init is not supposed to be called.");
    }

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {

        String connectionName = (String) getParameter(messageContext, MongoConstants.CONNECTION_NAME);

        try {
            ConnectionConfiguration configuration = getConnectionConfigFromContext(messageContext);

            ConnectionHandler handler = ConnectionHandler.getConnectionHandler();
            if (!handler.checkIfConnectionExists(MongoConstants.CONNECTOR_NAME, connectionName)) {
                MongoConnection mongoConnection = new MongoConnection(messageContext, configuration);
                handler.createConnection(MongoConstants.CONNECTOR_NAME, connectionName, mongoConnection);
            }

        } catch (MongoConnectorException e) {
            MongoUtils.handleError(messageContext, e, MongoConstants.MONGODB_CONNECTIVITY, ERROR_MESSAGE);

        } catch (Exception e) {
            MongoUtils.handleError(messageContext, e, MongoConstants.MONGODB_UNKNOWN_EXCEPTION, ERROR_MESSAGE);
        }
    }

    private ConnectionConfiguration getConnectionConfigFromContext(MessageContext messageContext) throws MongoConnectorException {

        String connectionName = (String) getParameter(messageContext, MongoConstants.CONNECTION_NAME);
        String protocol = (String) getParameter(messageContext, MongoConstants.PROTOCOL);
        String database = (String) getParameter(messageContext, MongoConstants.DATABASE);

        ConnectionConfiguration connectionConfig = new ConnectionConfiguration();
        connectionConfig.setConnectionName(connectionName);
        connectionConfig.setProtocol(protocol);
        connectionConfig.setDatabase(database);

        if (connectionConfig.isParameterized()) {
            connectionConfig.setParamConnectionConfig(getParamConnectionConfig(messageContext, connectionConfig));
        } else {
            connectionConfig.setUriConnectionConfig(getUriConnectionConfig(messageContext, connectionConfig));
        }

        return connectionConfig;
    }

    private ParamConnectionConfig getParamConnectionConfig(MessageContext messageContext, ConnectionConfiguration configuration)
            throws MongoConnectorException {

        ParamConnectionConfig paramConnectionConfig;

        switch (configuration.getProtocol()) {
            case STANDARD:
                paramConnectionConfig = new StandardConnectionConfig();
                setCommonParamConfigs(messageContext, paramConnectionConfig);
                setStandardConnectionConfigsFromContext(messageContext, (StandardConnectionConfig) paramConnectionConfig);
                configuration.setParamConnectionConfig(paramConnectionConfig);
                break;
            case DSL:
                paramConnectionConfig = new ParamConnectionConfig();
                setCommonParamConfigs(messageContext, paramConnectionConfig);
                configuration.setParamConnectionConfig(paramConnectionConfig);
                break;
            default:
                throw new MongoConnectorException(INVALID_PROTOCOL_MESSAGE);
        }

        return paramConnectionConfig;
    }

    private void setCommonParamConfigs(MessageContext messageContext, ParamConnectionConfig paramConnectionConfig) {

        String host = (String) getParameter(messageContext, MongoConstants.HOST);
        String username = (String) getParameter(messageContext, MongoConstants.USERNAME);
        String password = (String) getParameter(messageContext, MongoConstants.PASSWORD);
        String replicaSet = (String) getParameter(messageContext, MongoConstants.REPLICA_SET);
        String authSource = (String) getParameter(messageContext, MongoConstants.AUTH_SOURCE);
        String authMechanism = (String) getParameter(messageContext, MongoConstants.AUTH_MECHANISM);
        String authMechanismProperties = (String) getParameter(messageContext, MongoConstants.AUTH_MECHANISM_PROPERTIES);
        String gssapiServiceName = (String) getParameter(messageContext, MongoConstants.GSSAPI_SERVICE_NAME);
        String w = (String) getParameter(messageContext, MongoConstants.W);
        String wtimeoutMS = (String) getParameter(messageContext, MongoConstants.W_TIMEOUT_MS);
        String journal = (String) getParameter(messageContext, MongoConstants.JOURNAL);
        String maxPoolSize = (String) getParameter(messageContext, MongoConstants.MAX_POOL_SIZE);
        String minPoolSize = (String) getParameter(messageContext, MongoConstants.MIN_POOL_SIZE);
        String maxIdleTimeMS = (String) getParameter(messageContext, MongoConstants.MAX_IDLE_TIME_MS);
        String waitQueueMultiple = (String) getParameter(messageContext, MongoConstants.WAIT_QUEUE_MULTIPLE);
        String waitQueueTimeoutMS = (String) getParameter(messageContext, MongoConstants.WAIT_QUEUE_TIMEOUT_MS);
        String ssl = (String) getParameter(messageContext, MongoConstants.SSL);
        String sslInvalidHostNamesAllowed = (String) getParameter(messageContext, MongoConstants.SSL_INVALID_HOST_NAMES_ALLOWED);
        String connectTimeoutMS = (String) getParameter(messageContext, MongoConstants.CONNECT_TIMEOUT_MS);
        String socketTimeoutMS = (String) getParameter(messageContext, MongoConstants.SOCKET_TIMEOUT_MS);
        String compressors = (String) getParameter(messageContext, MongoConstants.COMPRESSORS);
        String zlibCompressionLevel = (String) getParameter(messageContext, MongoConstants.ZLIB_COMPRESSION_LEVEL);
        String readConcernLevel = (String) getParameter(messageContext, MongoConstants.READ_CONCERN_LEVEL);
        String readPreference = (String) getParameter(messageContext, MongoConstants.READ_PREFERENCE);
        String maxStalenessSeconds = (String) getParameter(messageContext, MongoConstants.MAX_STALENESS_SECONDS);
        String readPreferenceTags = (String) getParameter(messageContext, MongoConstants.READ_PREFERENCE_TAGS);
        String localThresholdMS = (String) getParameter(messageContext, MongoConstants.LOCAL_THRESHOLD_MS);
        String serverSelectionTimeoutMS = (String) getParameter(messageContext, MongoConstants.SERVER_SELECTION_TIMEOUT_MS);
        String serverSelectionTryOnce = (String) getParameter(messageContext, MongoConstants.SERVER_SELECTION_TRY_ONCE);
        String heartbeatFrequencyMS = (String) getParameter(messageContext, MongoConstants.HEARTBEAT_FREQUENCY_MS);
        String appName = (String) getParameter(messageContext, MongoConstants.APP_NAME);
        String retryReads = (String) getParameter(messageContext, MongoConstants.RETRY_READS);
        String retryWrites = (String) getParameter(messageContext, MongoConstants.RETRY_WRITES);
        String uuidRepresentation = (String) getParameter(messageContext, MongoConstants.UUID_REPRESENTATION);

        paramConnectionConfig.setHost(host);
        paramConnectionConfig.setUsername(username);
        paramConnectionConfig.setPassword(password);
        paramConnectionConfig.setReplicaSet(replicaSet);
        paramConnectionConfig.setAuthSource(authSource);
        paramConnectionConfig.setAuthMechanism(authMechanism);
        paramConnectionConfig.setAuthMechanismProperties(authMechanismProperties);
        paramConnectionConfig.setGssapiServiceName(gssapiServiceName);
        paramConnectionConfig.setW(w);
        paramConnectionConfig.setWtimeoutMS(wtimeoutMS);
        paramConnectionConfig.setJournal(journal);
        paramConnectionConfig.setMaxPoolSize(maxPoolSize);
        paramConnectionConfig.setMinPoolSize(minPoolSize);
        paramConnectionConfig.setMaxIdleTimeMS(maxIdleTimeMS);
        paramConnectionConfig.setWaitQueueMultiple(waitQueueMultiple);
        paramConnectionConfig.setWaitQueueTimeoutMS(waitQueueTimeoutMS);
        paramConnectionConfig.setSsl(ssl);
        paramConnectionConfig.setSslInvalidHostNamesAllowed(sslInvalidHostNamesAllowed);
        paramConnectionConfig.setConnectTimeoutMS(connectTimeoutMS);
        paramConnectionConfig.setSocketTimeoutMS(socketTimeoutMS);
        paramConnectionConfig.setCompressors(compressors);
        paramConnectionConfig.setZlibCompressionLevel(zlibCompressionLevel);
        paramConnectionConfig.setReadConcernLevel(readConcernLevel);
        paramConnectionConfig.setReadPreference(readPreference);
        paramConnectionConfig.setMaxStalenessSeconds(maxStalenessSeconds);
        paramConnectionConfig.setReadPreferenceTags(readPreferenceTags);
        paramConnectionConfig.setLocalThresholdMS(localThresholdMS);
        paramConnectionConfig.setServerSelectionTimeoutMS(serverSelectionTimeoutMS);
        paramConnectionConfig.setServerSelectionTryOnce(serverSelectionTryOnce);
        paramConnectionConfig.setHeartbeatFrequencyMS(heartbeatFrequencyMS);
        paramConnectionConfig.setAppName(appName);
        paramConnectionConfig.setRetryReads(retryReads);
        paramConnectionConfig.setRetryWrites(retryWrites);
        paramConnectionConfig.setUuidRepresentation(uuidRepresentation);
    }

    private void setStandardConnectionConfigsFromContext(MessageContext messageContext, StandardConnectionConfig standardConnectionConfig) {

        String port = (String) getParameter(messageContext, MongoConstants.PORT);
        String seeds = (String) getParameter(messageContext, MongoConstants.SEED_LIST);

        standardConnectionConfig.setPort(port);
        standardConnectionConfig.setSeedList(seeds);
    }

    private URIConnectionConfig getUriConnectionConfig(MessageContext messageContext, ConnectionConfiguration configuration)
            throws MongoConnectorException {

        URIConnectionConfig uriConnectionConfig = new URIConnectionConfig();

        String uri = (String) getParameter(messageContext, MongoConstants.CONNECTION_URI);
        uriConnectionConfig.setConnectionURI(uri);
        configuration.setUriConnectionConfig(uriConnectionConfig);

        return uriConnectionConfig;
    }

}
