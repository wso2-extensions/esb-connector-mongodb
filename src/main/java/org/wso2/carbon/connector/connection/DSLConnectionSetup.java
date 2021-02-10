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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.connector.exception.MongoConnectorException;
import org.wso2.carbon.connector.pojo.ConnectionConfiguration;
import org.wso2.carbon.connector.pojo.ParamConnectionConfig;

/**
 * Sets up parameter based DSL (DNS-constructed seed list) connection string.
 */
public class DSLConnectionSetup implements ProtocolBasedConnectionSetup {

    protected Log log = LogFactory.getLog(this.getClass());

    @Override
    public String constructConnectionString(ConnectionConfiguration config) throws MongoConnectorException {

        ParamConnectionConfig paramConnectionConfig = config.getParamConnectionConfig();
        String host = paramConnectionConfig.getHost();

        String uri;
        String prefix = "mongodb+srv://";
        String credentials = constructCredential(config);

        if (StringUtils.isEmpty(host)) {
            throw new MongoConnectorException("Invalid host name.");
        }

        uri = prefix + credentials + host;
        log.debug("The DNS seed list connection URI was built using parameters.");

        String options = constructOptions(config);
        if (StringUtils.isNotEmpty(options)) {
            uri += "/?" + options;
        }

        return uri;
    }
}
