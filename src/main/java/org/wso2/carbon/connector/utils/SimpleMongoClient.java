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

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CollationAlternate;
import com.mongodb.client.model.CollationCaseFirst;
import com.mongodb.client.model.CollationMaxVariable;
import com.mongodb.client.model.CollationStrength;
import com.mongodb.client.model.DeleteOptions;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * The SimpleMongoClient class acts as a wrapper class
 * for MongoDB CRUD operations.
 */
public class SimpleMongoClient {

    private static final String LOCALE = "locale";
    private static final String CASE_LEVEL = "caseLevel";
    private static final String CASE_FIRST = "caseFirst";
    private static final String STRENGTH = "strength";
    private static final String NUMERIC_ORDERING = "numericOrdering";
    private static final String ALTERNATE = "alternate";
    private static final String MAX_VARIABLE = "maxVariable";
    private static final String BACKWARDS = "backwards";
    private static final String NORMALIZATION = "normalization";
    private static final String MATCHED_COUNT = "matchedCount";
    private static final String MODIFIED_COUNT = "modifiedCount";
    private static final String UPSERTED_ID = "upsertedId";
    private static final String DELETED_COUNT = "deletedCount";
    private static final String INSERTED_COUNT = "insertedCount";
    private static final String INSERTED_ID = "insertedId";
    private static final String INSERTED_IDS = "insertedIds";

    private final MongoClient mongoClient;
    private final MongoDatabase database;

    public SimpleMongoClient(String connectionURI, String databaseName) {
        this.mongoClient = MongoClients.create(connectionURI);
        this.database = mongoClient.getDatabase(databaseName);
    }

    public static Collation stringToCollation(String collationString) {

        Document collation = Document.parse(collationString);
        Collation.Builder builder = Collation.builder();

        if (collation.containsKey(LOCALE)) {
            builder.locale(collation.getString(LOCALE));
        }
        if (collation.containsKey(CASE_LEVEL)) {
            builder.caseLevel(collation.getBoolean(CASE_LEVEL));
        }
        if (collation.containsKey(CASE_FIRST)) {
            builder.collationCaseFirst(CollationCaseFirst.fromString(collation.getString(CASE_FIRST)));
        }
        if (collation.containsKey(STRENGTH)) {
            builder.collationStrength(CollationStrength.fromInt(collation.getInteger(STRENGTH)));
        }
        if (collation.containsKey(NUMERIC_ORDERING)) {
            builder.numericOrdering(collation.getBoolean(NUMERIC_ORDERING));
        }
        if (collation.containsKey(ALTERNATE)) {
            builder.collationAlternate(CollationAlternate.fromString(collation.getString(ALTERNATE)));
        }
        if (collation.containsKey(MAX_VARIABLE)) {
            builder.collationMaxVariable(CollationMaxVariable.fromString(collation.getString(MAX_VARIABLE)));
        }
        if (collation.containsKey(BACKWARDS)) {
            builder.backwards(collation.getBoolean(BACKWARDS));
        }
        if (collation.containsKey(NORMALIZATION)) {
            builder.normalization(collation.getBoolean(NORMALIZATION));
        }
        return builder.build();
    }

    public JSONObject insertOneDocument(String collectionName, Document document) {
        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        InsertOneResult result = collection.insertOne(document);
        JSONObject insertResult = new JSONObject();
        
        // Handle different ID types safely using helper method
        String insertedIdValue = convertBsonValueToString(result.getInsertedId());
        
        insertResult.put(INSERTED_ID, insertedIdValue);
        insertResult.put(INSERTED_COUNT, 1);
        return insertResult;
    }

    public JSONObject insertManyDocuments(String collectionName, List<Document> documents, String ordered) {

        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        InsertManyOptions options = new InsertManyOptions();

        if (StringUtils.isNotEmpty(ordered)) {
            options.ordered(Boolean.parseBoolean(ordered));
        }
        InsertManyResult result = collection.insertMany(documents, options);
        JSONObject insertResult = new JSONObject();
        JSONArray insertedIds = new JSONArray();

        result.getInsertedIds().forEach((index, id) -> {
            String idValue = convertBsonValueToString(id);
            insertedIds.put(idValue);
        });

        insertResult.put(INSERTED_IDS, insertedIds);
        insertResult.put(INSERTED_COUNT, result.getInsertedIds().size());
        return insertResult;
    }

    public JSONObject findOneDocument(String collectionName, Document query, String projection, String collation) throws JSONException {
        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        FindIterable<Document> iterable = collection.find(query);

        if (StringUtils.isNotEmpty(projection)) {
            iterable.projection(Document.parse(projection));
        }
        if (StringUtils.isNotEmpty(collation)) {
            iterable.collation(stringToCollation(collation));
        }

        JSONObject response = new JSONObject();
        Document result = null;
        MongoCursor<Document> resultCursor = iterable.iterator();

        try {
            if (resultCursor.hasNext()) {
                result = resultCursor.next();
                response.put(MongoConstants.DATA, new JSONObject(result.toJson()));
                response.put(MongoConstants.FOUND, true);
            } else {
                response.put(MongoConstants.DATA, new JSONObject());
                response.put(MongoConstants.FOUND, false);
            }
        } finally {
            resultCursor.close();
        }

        return response;
    }

    public JSONObject findDocuments(String collectionName, Document query, String projection, String collation, String sort, int limit) throws JSONException {

        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        FindIterable<Document> iterable;
        if (limit != 0) {
            iterable = collection.find(query).limit(limit);
        } else {
            iterable = collection.find(query);
        }
        if (StringUtils.isNotEmpty(projection)) {
            iterable.projection(Document.parse(projection));
        }
        if (StringUtils.isNotEmpty(collation)) {
            iterable.collation(stringToCollation(collation));
        }
        if (StringUtils.isNotEmpty(sort)) {
            iterable.sort(Document.parse(sort));
        }
        MongoCursor<Document> resultCursor = iterable.iterator();

        JSONObject response = new JSONObject();
        JSONArray resultArray = new JSONArray();
        try {
            while (resultCursor.hasNext()) {
                Document result = resultCursor.next();
                resultArray.put(new JSONObject(result.toJson()));
            }
        } finally {
            resultCursor.close();
        }

        response.put(MongoConstants.DATA, resultArray);
        response.put(MongoConstants.COUNT, resultArray.length());
        response.put(MongoConstants.FOUND, resultArray.length() > 0);

        return response;
    }

    public JSONObject updateOneDocument(String collectionName, Document filter, Document update, String upsert, String collation, String arrayFilters)
            throws JSONException {

        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        UpdateOptions options = new UpdateOptions();

        if (StringUtils.isNotEmpty(upsert)) {
            options.upsert(Boolean.parseBoolean(upsert));
        }
        if (StringUtils.isNotEmpty(collation)) {
            options.collation(stringToCollation(collation));
        }
        if (StringUtils.isNotEmpty(arrayFilters)) {
            Document arrayDoc = Document.parse(arrayFilters);
            options.arrayFilters(Arrays.asList(arrayDoc));
        }

        UpdateResult result = collection.updateOne(filter, update, options);
        JSONObject updateResult = new JSONObject();
        updateResult.put(MATCHED_COUNT, result.getMatchedCount());
        updateResult.put(MODIFIED_COUNT, result.getModifiedCount());
        
        // Handle different upserted ID types safely using helper method
        String upsertedIdValue = convertBsonValueToString(result.getUpsertedId());
        updateResult.put(UPSERTED_ID, upsertedIdValue);
        return updateResult;
    }

    public JSONObject updateManyDocuments(String collectionName, Document filter, Document update, String upsert, String collation,
                                          String arrayFilters) throws JSONException {

        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        UpdateOptions options = new UpdateOptions();

        if (StringUtils.isNotEmpty(upsert)) {
            options.upsert(Boolean.parseBoolean(upsert));
        }
        if (StringUtils.isNotEmpty(collation)) {
            options.collation(stringToCollation(collation));
        }
        if (StringUtils.isNotEmpty(arrayFilters)) {
            Document arrayDoc = Document.parse(arrayFilters);
            options.arrayFilters(Arrays.asList(arrayDoc));
        }

        UpdateResult result = collection.updateMany(filter, update, options);
        JSONObject updateResult = new JSONObject();
        updateResult.put(MATCHED_COUNT, result.getMatchedCount());
        updateResult.put(MODIFIED_COUNT, result.getModifiedCount());
        
        // Handle different upserted ID types safely using helper method
        String upsertedIdValue = convertBsonValueToString(result.getUpsertedId());
        updateResult.put(UPSERTED_ID, upsertedIdValue);
        return updateResult;
    }

    public JSONObject deleteOneDocument(String collectionName, Document filter, String collation) throws JSONException {

        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        DeleteOptions options = new DeleteOptions();

        if (StringUtils.isNotEmpty(collation)) {
            options.collation(stringToCollation(collation));
        }

        DeleteResult result = collection.deleteOne(filter, options);
        JSONObject deleteResult = new JSONObject();
        deleteResult.put(DELETED_COUNT, result.getDeletedCount());
        return deleteResult;
    }

    public JSONObject deleteManyDocuments(String collectionName, Document filter, String collation) throws JSONException {

        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        DeleteOptions options = new DeleteOptions();

        if (StringUtils.isNotEmpty(collation)) {
            options.collation(stringToCollation(collation));
        }

        DeleteResult result = collection.deleteMany(filter, options);
        JSONObject deleteResult = new JSONObject();
        deleteResult.put(DELETED_COUNT, result.getDeletedCount());
        return deleteResult;
    }

    /**
     * Safely converts a BsonValue to a string representation, handling different BSON types.
     * This prevents BsonInvalidOperationException when MongoDB returns different ID types.
     *
     * @param bsonValue The BsonValue to convert (can be null)
     * @return String representation of the ID, or null if bsonValue is null
     */
    private static String convertBsonValueToString(org.bson.BsonValue bsonValue) {
        if (bsonValue == null) {
            return null;
        }

        switch (bsonValue.getBsonType()) {
            case OBJECT_ID:
                return bsonValue.asObjectId().getValue().toString();
            case STRING:
                return bsonValue.asString().getValue();
            case INT32:
                return String.valueOf(bsonValue.asInt32().getValue());
            case INT64:
                return String.valueOf(bsonValue.asInt64().getValue());
            default:
                return bsonValue.toString();
        }
    }

    public void closeConnection() {

        if (mongoClient != null) {
            this.mongoClient.close();
        }
    }
}
