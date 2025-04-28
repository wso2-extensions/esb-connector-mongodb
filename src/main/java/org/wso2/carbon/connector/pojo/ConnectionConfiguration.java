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

package org.wso2.carbon.connector.pojo;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.connector.connection.MongoProtocol;
import org.wso2.carbon.connector.exception.MongoConnectorException;
import org.wso2.carbon.connector.utils.MongoConstants;

/**
 * The ConnectionConfiguration contains all the common parameters
 * needed to establish the connection with the MongoDB database.
 */
public class ConnectionConfiguration {

    private String connectionName;
    private MongoProtocol protocol;
    private boolean isParameterized = false;
    private String database;
    private ParamConnectionConfig paramConnectionConfig;
    private URIConnectionConfig uriConnectionConfig;

    public String getConnectionName() {

        return connectionName;
    }

    public void setConnectionName(String connectionName) throws MongoConnectorException {

        if (StringUtils.isNotEmpty(connectionName)) {
            this.connectionName = connectionName;
        } else {
            throw new MongoConnectorException("Mandatory parameter 'connectionName' is not set.");
        }
    }

    public MongoProtocol getProtocol() {

        return protocol;
    }

    public void setProtocol(String inputType, String useDnsSrv) throws MongoConnectorException {
        if (StringUtils.isEmpty(inputType)) {
            throw new MongoConnectorException("Mandatory parameter 'inputType' is not set.");
        }

        switch (inputType) {
            case MongoConstants.CONNECTION_STRING_URI:
                this.protocol = MongoProtocol.URI;
                this.isParameterized = false;
                break;
            case MongoConstants.CONNECTION_PARAMETERS:
                this.protocol = Boolean.parseBoolean(useDnsSrv) ?
                        MongoProtocol.DSL : MongoProtocol.STANDARD;
                this.isParameterized = true;
                break;
            default:
                throw new MongoConnectorException("Invalid protocol type: " + inputType);
        }
    }

    public boolean isParameterized() {

        return isParameterized;
    }

    public String getDatabase() {

        return database;
    }

    public void setDatabase(String workingDir) throws MongoConnectorException {

        if (StringUtils.isNotEmpty(workingDir)) {
            this.database = workingDir;
        } else {
            throw new MongoConnectorException("Mandatory parameter 'database' is not set.");
        }
    }

    public ParamConnectionConfig getParamConnectionConfig() {

        return paramConnectionConfig;
    }

    public void setParamConnectionConfig(ParamConnectionConfig paramConnectionConfig) {

        this.paramConnectionConfig = paramConnectionConfig;
    }

    public URIConnectionConfig getUriConnectionConfig() {

        return uriConnectionConfig;
    }

    public void setUriConnectionConfig(URIConnectionConfig uriConnectionConfig) {

        this.uriConnectionConfig = uriConnectionConfig;
    }
}
