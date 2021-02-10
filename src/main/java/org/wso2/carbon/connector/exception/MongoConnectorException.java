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

package org.wso2.carbon.connector.exception;

import org.wso2.carbon.connector.core.ConnectException;

/**
 * Exception thrown when an error occurs connecting to the MongoDB database
 * or while performing an operation using the MongoDB connection
 */
public class MongoConnectorException extends ConnectException {

    public MongoConnectorException(Throwable e) {

        super(e);
    }

    public MongoConnectorException(String msg) {

        super(msg);
    }

    public MongoConnectorException(String message, Throwable e) {

        super(e, message);
    }
}
