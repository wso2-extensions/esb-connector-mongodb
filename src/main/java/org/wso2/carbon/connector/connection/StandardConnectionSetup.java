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
import org.wso2.carbon.connector.pojo.ConnectionConfiguration;
import org.wso2.carbon.connector.pojo.StandardConnectionConfig;

/**
 * Sets up parameter based standard connection string.
 */
public class StandardConnectionSetup implements ProtocolBasedConnectionSetup {

    protected Log log = LogFactory.getLog(this.getClass());

    @Override
    public String constructConnectionString(ConnectionConfiguration config) {

        StandardConnectionConfig standardConnectionConfig = (StandardConnectionConfig) config.getParamConnectionConfig();
        String host = standardConnectionConfig.getHost();
        String port = standardConnectionConfig.getPort();
        String seeds = standardConnectionConfig.getSeedList();

        String uri;
        String prefix = "mongodb://";
        String defaultHost = "127.0.0.1";
        int defaultPort = 27017;
        String credentials = constructCredential(config);

        if (StringUtils.isNotEmpty(seeds)) {
            log.debug("The standard connection URI was built using the seedsList parameter.");
            uri = prefix + credentials + seeds;
        } else if (StringUtils.isNotEmpty(host) && StringUtils.isNotEmpty(port)) {
            log.debug("The standard connection URI was constructed using the host and port parameters.");
            uri = prefix + credentials + host + ":" + port;
        } else if (StringUtils.isNotEmpty(host)) {
            log.debug("The standard connection URI was constructed using the host parameter and the default port.");
            uri = prefix + credentials + host + ":" + defaultPort;
        } else if (StringUtils.isNotEmpty(port)) {
            log.debug("The standard connection URI was constructed using the port parameter and the default host.");
            uri = prefix + credentials + defaultHost + ":" + port;
        } else {
            log.debug("The standard connection URI was constructed using the default host and default port.");
            uri = prefix + credentials + defaultHost + ":" + defaultPort;
        }

        String options = constructOptions(config);
        if (StringUtils.isNotEmpty(options)) {
            uri += "/?" + options;
        }

        return uri;
    }
}
