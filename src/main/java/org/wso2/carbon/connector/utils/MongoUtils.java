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

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.wso2.carbon.connector.exception.MongoConnectorException;

/**
 * The MongoUtil class contains all common methods used in
 * MongoDB connector implementation.
 */
public class MongoUtils {

    private static final Log log = LogFactory.getLog(MongoUtils.class);

    /**
     * Retrieves connection name from message context if configured as configKey attribute
     * or from the template parameter.
     *
     * @param messageContext Message Context from which the parameters should be extracted.
     * @return Connection name.
     */
    public static String getConnectionName(MessageContext messageContext) throws MongoConnectorException {

        String connectionName = (String) messageContext.getProperty(MongoConstants.CONNECTION_NAME);
        if (connectionName == null) {
            MongoConnectorException e = new MongoConnectorException("Connection name is not set.");
            handleError(messageContext, e, MongoConstants.MONGODB_INVALID_INPUT, e.getMessage());
        }
        return connectionName;
    }

    public static void setErrorResponse(MessageContext messageContext, Throwable e, int errorCode) {

        messageContext.setProperty(SynapseConstants.ERROR_CODE, errorCode);
        messageContext.setProperty(SynapseConstants.ERROR_MESSAGE, e.getMessage());
        messageContext.setProperty(SynapseConstants.ERROR_DETAIL, e.getMessage());
        messageContext.setProperty(SynapseConstants.ERROR_EXCEPTION, e);
    }

    public static void handleException(String message, Throwable throwable) throws MongoConnectorException {

        log.error(message, throwable);
        throw new MongoConnectorException(message, throwable);
    }

    /**
     * @param messageContext Message context to set the information to.
     * @param e              The exception thrown.
     * @param errorCode      The error code.
     * @param message        The error message.
     * @throws MongoConnectorException In case of an error occurs while saving the error details to the MessageContext.
     */
    public static void handleError(MessageContext messageContext, Throwable e, int errorCode, String message) throws MongoConnectorException {

        setErrorResponse(messageContext, e, errorCode);
        handleException(message, e);
    }

    /**
     * The setPayload method calls the getNewJsonPayload method from the wso2-synapse package
     * and sets the payload to the the MessageContext object.
     *
     * @param messageContext Axis2MessageContext to which the new JSON payload must be saved.
     * @param payload        JSON payload as a String object.
     * @throws MongoConnectorException In case of an error occurs while saving the payload to the MessageContext.
     */
    public static void setPayload(MessageContext messageContext, String payload) throws MongoConnectorException {

        try {
            org.apache.axis2.context.MessageContext axisMsgCtx = ((Axis2MessageContext) messageContext).getAxis2MessageContext();
            JsonUtil.getNewJsonPayload(axisMsgCtx, payload, true, true);
            axisMsgCtx.setProperty(Constants.Configuration.MESSAGE_TYPE, MongoConstants.JSON_CONTENT_TYPE);
            axisMsgCtx.setProperty(Constants.Configuration.CONTENT_TYPE, MongoConstants.JSON_CONTENT_TYPE);

        } catch (AxisFault e) {
            String errorMessage = "Error occurred while populating the payload in the message context.";
            handleError(messageContext, e, MongoConstants.MONGODB_INVALID_RESPONSE, errorMessage);
        }
    }
}
