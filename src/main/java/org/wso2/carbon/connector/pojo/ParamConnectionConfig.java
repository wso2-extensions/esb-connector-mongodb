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
 * The ParamConnectionConfig contains all the common parameters
 * needed to build the connection URI.
 */
public class ParamConnectionConfig {

    private String host;
    private String username;
    private String password;
    private String replicaSet;
    private String authSource;
    private String authMechanism;
    private String authMechanismProperties;
    private String gssapiServiceName;
    private String w;
    private String wtimeoutMS;
    private String journal;
    private String maxPoolSize;
    private String minPoolSize;
    private String maxIdleTimeMS;
    private String waitQueueMultiple;
    private String waitQueueTimeoutMS;
    private String ssl;
    private String sslInvalidHostNamesAllowed;
    private String connectTimeoutMS;
    private String socketTimeoutMS;
    private String compressors;
    private String zlibCompressionLevel;
    private String readConcernLevel;
    private String readPreference;
    private String maxStalenessSeconds;
    private String readPreferenceTags;
    private String localThresholdMS;
    private String serverSelectionTimeoutMS;
    private String serverSelectionTryOnce;
    private String heartbeatFrequencyMS;
    private String appName;
    private String retryReads;
    private String retryWrites;
    private String uuidRepresentation;

    public String getHost() {

        return host;
    }

    public void setHost(String host) {

        if (StringUtils.isNotEmpty(host)) {
            this.host = host;
        }
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        if (StringUtils.isNotEmpty(username)) {
            this.username = username;
        }
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        if (StringUtils.isNotEmpty(password)) {
            this.password = password;
        }
    }

    public String getReplicaSet() {

        return replicaSet;
    }

    public void setReplicaSet(String replicaSet) {

        if (StringUtils.isNotEmpty(replicaSet)) {
            this.replicaSet = replicaSet;
        }
    }

    public String getAuthSource() {

        return authSource;
    }

    public void setAuthSource(String authenticationDatabase) {

        if (StringUtils.isNotEmpty(authenticationDatabase)) {
            this.authSource = authenticationDatabase;
        }
    }

    public String getAuthMechanism() {

        return authMechanism;
    }

    public void setAuthMechanism(String authMechanism) {

        if (StringUtils.isNotEmpty(authMechanism)) {
            this.authMechanism = authMechanism;
        }
    }

    public String getAuthMechanismProperties() {

        return authMechanismProperties;
    }

    public void setAuthMechanismProperties(String authMechanismProperties) {

        if (StringUtils.isNotEmpty(authMechanismProperties)) {
            this.authMechanismProperties = authMechanismProperties;
        }
    }

    public String getGssapiServiceName() {

        return gssapiServiceName;
    }

    public void setGssapiServiceName(String gssapiServiceName) {

        if (StringUtils.isNotEmpty(gssapiServiceName)) {
            this.gssapiServiceName = gssapiServiceName;
        }
    }

    public String getW() {

        return w;
    }

    public void setW(String w) {

        if (StringUtils.isNotEmpty(w)) {
            this.w = w;
        }
    }

    public String getWtimeoutMS() {

        return wtimeoutMS;
    }

    public void setWtimeoutMS(String wtimeoutMS) {

        if (StringUtils.isNotEmpty(wtimeoutMS)) {
            this.wtimeoutMS = wtimeoutMS;
        }
    }

    public String getJournal() {

        return journal;
    }

    public void setJournal(String journal) {

        if (StringUtils.isNotEmpty(journal)) {
            this.journal = journal;
        }
    }

    public String getMaxPoolSize() {

        return maxPoolSize;
    }

    public void setMaxPoolSize(String maxPoolSize) {

        if (StringUtils.isNotEmpty(maxPoolSize)) {
            this.maxPoolSize = maxPoolSize;
        }
    }

    public String getMinPoolSize() {

        return minPoolSize;
    }

    public void setMinPoolSize(String minPoolSize) {

        if (StringUtils.isNotEmpty(minPoolSize)) {
            this.minPoolSize = minPoolSize;
        }
    }

    public String getMaxIdleTimeMS() {

        return maxIdleTimeMS;
    }

    public void setMaxIdleTimeMS(String maxIdleTimeMS) {

        if (StringUtils.isNotEmpty(maxIdleTimeMS)) {
            this.maxIdleTimeMS = maxIdleTimeMS;
        }
    }

    public String getWaitQueueMultiple() {

        return waitQueueMultiple;
    }

    public void setWaitQueueMultiple(String waitQueueMultiple) {

        if (StringUtils.isNotEmpty(waitQueueMultiple)) {
            this.waitQueueMultiple = waitQueueMultiple;
        }
    }

    public String getWaitQueueTimeoutMS() {

        return waitQueueTimeoutMS;
    }

    public void setWaitQueueTimeoutMS(String waitQueueTimeoutMS) {

        if (StringUtils.isNotEmpty(waitQueueTimeoutMS)) {
            this.waitQueueTimeoutMS = waitQueueTimeoutMS;
        }
    }

    public String getSsl() {

        return ssl;
    }

    public void setSsl(String ssl) {

        if (StringUtils.isNotEmpty(ssl)) {
            this.ssl = ssl;
        }
    }

    public String getSslInvalidHostNamesAllowed() {

        return sslInvalidHostNamesAllowed;
    }

    public void setSslInvalidHostNamesAllowed(String sslInvalidHostNamesAllowed) {

        if (StringUtils.isNotEmpty(sslInvalidHostNamesAllowed)) {
            this.sslInvalidHostNamesAllowed = sslInvalidHostNamesAllowed;
        }
    }

    public String getConnectTimeoutMS() {

        return connectTimeoutMS;
    }

    public void setConnectTimeoutMS(String connectTimeoutMS) {

        if (StringUtils.isNotEmpty(connectTimeoutMS)) {
            this.connectTimeoutMS = connectTimeoutMS;
        }
    }

    public String getSocketTimeoutMS() {

        return socketTimeoutMS;
    }

    public void setSocketTimeoutMS(String socketTimeoutMS) {

        if (StringUtils.isNotEmpty(socketTimeoutMS)) {
            this.socketTimeoutMS = socketTimeoutMS;
        }
    }

    public String getCompressors() {

        return compressors;
    }

    public void setCompressors(String compressors) {

        if (StringUtils.isNotEmpty(compressors)) {
            this.compressors = compressors;
        }
    }

    public String getZlibCompressionLevel() {

        return zlibCompressionLevel;
    }

    public void setZlibCompressionLevel(String zlibCompressionLevel) {

        if (StringUtils.isNotEmpty(zlibCompressionLevel)) {
            this.zlibCompressionLevel = zlibCompressionLevel;
        }
    }

    public String getReadConcernLevel() {

        return readConcernLevel;
    }

    public void setReadConcernLevel(String readConcernLevel) {

        if (StringUtils.isNotEmpty(readConcernLevel)) {
            this.readConcernLevel = readConcernLevel;
        }
    }

    public String getReadPreference() {

        return readPreference;
    }

    public void setReadPreference(String readPreference) {

        if (StringUtils.isNotEmpty(readPreference)) {
            this.readPreference = readPreference;
        }
    }

    public String getMaxStalenessSeconds() {

        return maxStalenessSeconds;
    }

    public void setMaxStalenessSeconds(String maxStalenessSeconds) {

        if (StringUtils.isNotEmpty(maxStalenessSeconds)) {
            this.maxStalenessSeconds = maxStalenessSeconds;
        }
    }

    public String getReadPreferenceTags() {

        return readPreferenceTags;
    }

    public void setReadPreferenceTags(String readPreferenceTags) {

        if (StringUtils.isNotEmpty(readPreferenceTags)) {
            this.readPreferenceTags = readPreferenceTags;
        }
    }

    public String getLocalThresholdMS() {

        return localThresholdMS;
    }

    public void setLocalThresholdMS(String localThresholdMS) {

        if (StringUtils.isNotEmpty(localThresholdMS)) {
            this.localThresholdMS = localThresholdMS;
        }
    }

    public String getServerSelectionTimeoutMS() {

        return serverSelectionTimeoutMS;
    }

    public void setServerSelectionTimeoutMS(String serverSelectionTimeoutMS) {

        if (StringUtils.isNotEmpty(serverSelectionTimeoutMS)) {
            this.serverSelectionTimeoutMS = serverSelectionTimeoutMS;
        }
    }

    public String getServerSelectionTryOnce() {

        return serverSelectionTryOnce;
    }

    public void setServerSelectionTryOnce(String serverSelectionTryOnce) {

        if (StringUtils.isNotEmpty(serverSelectionTryOnce)) {
            this.serverSelectionTryOnce = serverSelectionTryOnce;
        }
    }

    public String getHeartbeatFrequencyMS() {

        return heartbeatFrequencyMS;
    }

    public void setHeartbeatFrequencyMS(String heartbeatFrequencyMS) {

        if (StringUtils.isNotEmpty(heartbeatFrequencyMS)) {
            this.heartbeatFrequencyMS = heartbeatFrequencyMS;
        }
    }

    public String getAppName() {

        return appName;
    }

    public void setAppName(String appName) {

        if (StringUtils.isNotEmpty(appName)) {
            this.appName = appName;
        }
    }

    public String getRetryReads() {

        return retryReads;
    }

    public void setRetryReads(String retryReads) {

        if (StringUtils.isNotEmpty(retryReads)) {
            this.retryReads = retryReads;
        }
    }

    public String getRetryWrites() {

        return retryWrites;
    }

    public void setRetryWrites(String retryWrites) {

        if (StringUtils.isNotEmpty(retryWrites)) {
            this.retryWrites = retryWrites;
        }
    }

    public String getUuidRepresentation() {

        return uuidRepresentation;
    }

    public void setUuidRepresentation(String uuidRepresentation) {

        if (StringUtils.isNotEmpty(uuidRepresentation)) {
            this.uuidRepresentation = uuidRepresentation;
        }
    }
}
