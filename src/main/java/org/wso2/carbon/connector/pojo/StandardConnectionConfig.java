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

/**
 * The StandardConnectionConfig contains all the parameters
 * needed to build the standard connection URI.
 */
public class StandardConnectionConfig extends ParamConnectionConfig {

    private String port;
    private String seedList;

    public String getPort() {

        return port;
    }

    public void setPort(String port) {

        if (StringUtils.isNotEmpty(port)) {
            this.port = port;
        }
    }

    public String getSeedList() {

        return seedList;
    }

    public void setSeedList(String seedList) {

        if (StringUtils.isNotEmpty(seedList)) {
            this.seedList = seedList;
        }
    }
}
