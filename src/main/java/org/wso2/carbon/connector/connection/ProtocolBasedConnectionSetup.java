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

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.connector.exception.MongoConnectorException;
import org.wso2.carbon.connector.pojo.ConnectionConfiguration;
import org.wso2.carbon.connector.pojo.ParamConnectionConfig;
import org.wso2.carbon.connector.utils.MongoConstants;

/**
 * Protocol specific connection URI strategy.
 */
public interface ProtocolBasedConnectionSetup {

    String constructConnectionString(ConnectionConfiguration config) throws MongoConnectorException;

    /**
     * Constructs the credential string based on configurations.
     *
     * @param config Input connection configuration.
     * @return The constructed credential string.
     */
    default String constructCredential(ConnectionConfiguration config) {

        ParamConnectionConfig paramConnectionConfig = config.getParamConnectionConfig();
        String username = paramConnectionConfig.getUsername();
        String password = paramConnectionConfig.getPassword();

        String credentials = "";
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
            credentials = encodeString(username) + ":" + encodeString(password) + "@";
        }

        return credentials;
    }

    /**
     * Encodes the given string.
     *
     * @param str The string to be encoded.
     * @return The encoded string.
     */
    default String encodeString(String str) {

        str = str.replace(":", "%3A");
        str = str.replace("/", "%2F");
        str = str.replace("?", "%3F");
        str = str.replace("#", "%23");
        str = str.replace("[", "%5B");
        str = str.replace("]", "%5D");
        str = str.replace("@", "%40");

        return str;
    }

    /**
     * Constructs the options string based on configurations.
     *
     * @param config Input connection configuration.
     * @return The constructed options string.
     */
    default String constructOptions(ConnectionConfiguration config) {

        ParamConnectionConfig paramConnectionConfig = config.getParamConnectionConfig();
        String replicaSet = paramConnectionConfig.getReplicaSet();
        String authSource = paramConnectionConfig.getAuthSource();
        String authMechanism = paramConnectionConfig.getAuthMechanism();
        String authMechanismProperties = paramConnectionConfig.getAuthMechanismProperties();
        String gssapiServiceName = paramConnectionConfig.getGssapiServiceName();
        String w = paramConnectionConfig.getW();
        String wtimeoutMS = paramConnectionConfig.getWtimeoutMS();
        String journal = paramConnectionConfig.getJournal();
        String maxPoolSize = paramConnectionConfig.getMaxPoolSize();
        String minPoolSize = paramConnectionConfig.getMinPoolSize();
        String maxIdleTimeMS = paramConnectionConfig.getMaxIdleTimeMS();
        String waitQueueMultiple = paramConnectionConfig.getWaitQueueMultiple();
        String waitQueueTimeoutMS = paramConnectionConfig.getWaitQueueTimeoutMS();
        String ssl = paramConnectionConfig.getSsl();
        String sslInvalidHostNamesAllowed = paramConnectionConfig.getSslInvalidHostNamesAllowed();
        String connectTimeoutMS = paramConnectionConfig.getConnectTimeoutMS();
        String socketTimeoutMS = paramConnectionConfig.getSocketTimeoutMS();
        String compressors = paramConnectionConfig.getCompressors();
        String zlibCompressionLevel = paramConnectionConfig.getZlibCompressionLevel();
        String readConcernLevel = paramConnectionConfig.getReadConcernLevel();
        String readPreference = paramConnectionConfig.getReadPreference();
        String maxStalenessSeconds = paramConnectionConfig.getMaxStalenessSeconds();
        String readPreferenceTags = paramConnectionConfig.getReadPreferenceTags();
        String localThresholdMS = paramConnectionConfig.getLocalThresholdMS();
        String serverSelectionTimeoutMS = paramConnectionConfig.getServerSelectionTimeoutMS();
        String serverSelectionTryOnce = paramConnectionConfig.getServerSelectionTryOnce();
        String heartbeatFrequencyMS = paramConnectionConfig.getHeartbeatFrequencyMS();
        String appName = paramConnectionConfig.getAppName();
        String retryReads = paramConnectionConfig.getRetryReads();
        String retryWrites = paramConnectionConfig.getRetryWrites();
        String uuidRepresentation = paramConnectionConfig.getUuidRepresentation();

        String options = "";
        options += validateStringOption(MongoConstants.REPLICA_SET, replicaSet);
        options += validateStringOption(MongoConstants.AUTH_SOURCE, authSource);
        options += validateStringOption(MongoConstants.AUTH_MECHANISM, authMechanism);
        options += validateStringOption(MongoConstants.AUTH_MECHANISM_PROPERTIES, authMechanismProperties);
        options += validateStringOption(MongoConstants.GSSAPI_SERVICE_NAME, gssapiServiceName);
        options += validateStringOption(MongoConstants.W, w);
        options += validateStringOption(MongoConstants.W_TIMEOUT_MS, wtimeoutMS);
        options += validateStringOption(MongoConstants.JOURNAL, journal);
        options += validateStringOption(MongoConstants.MAX_POOL_SIZE, maxPoolSize);
        options += validateStringOption(MongoConstants.MIN_POOL_SIZE, minPoolSize);
        options += validateStringOption(MongoConstants.MAX_IDLE_TIME_MS, maxIdleTimeMS);
        options += validateStringOption(MongoConstants.WAIT_QUEUE_MULTIPLE, waitQueueMultiple);
        options += validateStringOption(MongoConstants.WAIT_QUEUE_TIMEOUT_MS, waitQueueTimeoutMS);
        options += validateBooleanOption(MongoConstants.SSL, ssl);
        options += validateBooleanOption(MongoConstants.SSL_INVALID_HOST_NAMES_ALLOWED, sslInvalidHostNamesAllowed);
        options += validateStringOption(MongoConstants.CONNECT_TIMEOUT_MS, connectTimeoutMS);
        options += validateStringOption(MongoConstants.SOCKET_TIMEOUT_MS, socketTimeoutMS);
        options += validateStringOption(MongoConstants.COMPRESSORS, compressors);
        options += validateStringOption(MongoConstants.ZLIB_COMPRESSION_LEVEL, zlibCompressionLevel);
        options += validateStringOption(MongoConstants.READ_CONCERN_LEVEL, readConcernLevel);
        options += validateStringOption(MongoConstants.READ_PREFERENCE, readPreference);
        options += validateStringOption(MongoConstants.MAX_STALENESS_SECONDS, maxStalenessSeconds);
        options += validateStringOption(MongoConstants.READ_PREFERENCE_TAGS, readPreferenceTags);
        options += validateStringOption(MongoConstants.LOCAL_THRESHOLD_MS, localThresholdMS);
        options += validateStringOption(MongoConstants.SERVER_SELECTION_TIMEOUT_MS, serverSelectionTimeoutMS);
        options += validateBooleanOption(MongoConstants.SERVER_SELECTION_TRY_ONCE, serverSelectionTryOnce);
        options += validateStringOption(MongoConstants.HEARTBEAT_FREQUENCY_MS, heartbeatFrequencyMS);
        options += validateStringOption(MongoConstants.APP_NAME, appName);
        options += validateBooleanOption(MongoConstants.RETRY_READS, retryReads);
        options += validateBooleanOption(MongoConstants.RETRY_WRITES, retryWrites);
        options += validateStringOption(MongoConstants.UUID_REPRESENTATION, uuidRepresentation);

        return options;
    }

    default String validateStringOption(String option, String optionValue) {

        String optionString = "";
        if (StringUtils.isNotEmpty(optionValue)) {
            optionString = "&" + option + "=" + optionValue;
        }
        return optionString;
    }

    default String validateBooleanOption(String option, String optionValue) {

        String optionString = "";
        if (StringUtils.isNotEmpty(optionValue)) {
            boolean optionBoolean = Boolean.parseBoolean(optionValue);
            optionString = "&" + option + "=" + optionBoolean;
        }
        return optionString;
    }
}
