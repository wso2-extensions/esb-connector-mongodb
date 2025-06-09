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

package org.wso2.carbon.connector.utils;

/**
 * The MongoConstants class contains all constants used in
 * MongoDB connector implementation.
 */
public class MongoConstants {

    /**
     * Details of the MongoDB connector
     */
    public static final String CONNECTOR_NAME = "mongodb";
    public static final CharSequence CONNECTOR_LIBRARY_NAME = "mongodb-connector";
    public static final String CONNECTOR_LIBRARY_PACKAGE_TYPE = "org.wso2.carbon.connector";

    /**
     * Template parameters for the MongoDB connection.
     */
    public static final String CONNECTION_NAME = "name";
    public static final String INPUT_TYPE = "inputType";
    public static final String CONNECTION_STRING_URI = "Connection String URI";
    public static final String CONNECTION_PARAMETERS = "Connection Parameters";
    public static final String USE_DNS_SRV = "useDnsSrvLookup";
    public static final String DATABASE = "database";
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String SEED_LIST = "seedList";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String CONNECTION_URI = "connectionURI";
    public static final String REPLICA_SET = "replicaSet";
    public static final String AUTH_SOURCE = "authSource";
    public static final String AUTH_MECHANISM = "authMechanism";
    public static final String AUTH_MECHANISM_PROPERTIES = "authMechanismProperties";
    public static final String GSSAPI_SERVICE_NAME = "gssapiServiceName";
    public static final String W = "w";
    public static final String W_TIMEOUT_MS = "wtimeoutMS";
    public static final String JOURNAL = "journal";
    public static final String MAX_POOL_SIZE = "maxPoolSize";
    public static final String MIN_POOL_SIZE = "minPoolSize";
    public static final String MAX_IDLE_TIME_MS = "maxIdleTimeMS";
    public static final String WAIT_QUEUE_MULTIPLE = "waitQueueMultiple";
    public static final String WAIT_QUEUE_TIMEOUT_MS = "waitQueueTimeoutMS";
    public static final String SSL = "ssl";
    public static final String SSL_INVALID_HOST_NAMES_ALLOWED = "sslInvalidHostNamesAllowed";
    public static final String CONNECT_TIMEOUT_MS = "connectTimeoutMS";
    public static final String SOCKET_TIMEOUT_MS = "socketTimeoutMS";
    public static final String COMPRESSORS = "compressors";
    public static final String ZLIB_COMPRESSION_LEVEL = "zlibCompressionLevel";
    public static final String READ_CONCERN_LEVEL = "readConcernLevel";
    public static final String READ_PREFERENCE = "readPreference";
    public static final String MAX_STALENESS_SECONDS = "maxStalenessSeconds";
    public static final String READ_PREFERENCE_TAGS = "readPreferenceTags";
    public static final String LOCAL_THRESHOLD_MS = "localThresholdMS";
    public static final String SERVER_SELECTION_TIMEOUT_MS = "serverSelectionTimeoutMS";
    public static final String SERVER_SELECTION_TRY_ONCE = "serverSelectionTryOnce";
    public static final String HEARTBEAT_FREQUENCY_MS = "heartbeatFrequencyMS";
    public static final String APP_NAME = "appName";
    public static final String RETRY_READS = "retryReads";
    public static final String RETRY_WRITES = "retryWrites";
    public static final String UUID_REPRESENTATION = "uuidRepresentation";
    public static final String DOCUMENT_ID = "_id";
    public static final String DATA = "data";
    public static final String FOUND = "found";
    public static final String COUNT = "count";

    /**
     * Content type of the JSON payload.
     */
    public final static String JSON_CONTENT_TYPE = "application/json";

    /**
     * Error codes used in the MongoDB connector.
     */
    public static final int MONGODB_CONNECTIVITY = 700401;
    public static final int MONGODB_INVALID_INPUT = 700402;
    public static final int MONGODB_INVALID_RESPONSE = 700403;
    public static final int MONGODB_UNKNOWN_EXCEPTION = 700404;
}
