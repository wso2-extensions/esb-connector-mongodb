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
package org.wso2.carbon.connector.integration.test.mongodb.utils;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * The EmbeddedMongoDatabase class provides a platform
 * for running mongodb in the integration tests.
 */
public class EmbeddedMongoDatabase {

    private final MongodStarter starter = MongodStarter.getDefaultInstance();
    private MongodExecutable mongodExecutable;
    private MongodProcess mongodProcess;

    public void startMongoServer() throws Exception {

        String host = "127.0.0.1";
        int port = 27018;

        IMongodConfig iMongodConfig =
                new MongodConfigBuilder().version(Version.Main.PRODUCTION).net(new Net(host, port, Network.localhostIsIPv6())).build();

        this.mongodExecutable = starter.prepare(iMongodConfig);
        this.mongodProcess = mongodExecutable.start();
    }

    public void stopMongoServer() {

        if (this.mongodProcess != null) {
            this.mongodProcess.stop();
            this.mongodExecutable.stop();
        }
    }
}
