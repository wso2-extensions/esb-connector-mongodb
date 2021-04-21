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

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
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
    private static final String FIND_ONE_RESULT = "{\"FindOneResult\": \"Not Found\"}";
    private static final String FIND_RESULT = "{\"FindResult\": \"Not Found\"}";
    private static final String MATCHED_COUNT = "matchedCount";
    private static final String MODIFIED_COUNT = "modifiedCount";
    private static final String UPSERTED_ID = "upsertedId";
    private static final String DELETED_COUNT = "deletedCount";

    private final MongoClient mongoClient;
    private final MongoDatabase database;

    public SimpleMongoClient(String connectionURI, String databaseName) {

        this.mongoClient = new MongoClient(new MongoClientURI(connectionURI));
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

    public void insertOneDocument(String collectionName, Document document) {

        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        collection.insertOne(document);
    }

    public void insertManyDocuments(String collectionName, List<Document> documents, String ordered) {

        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        InsertManyOptions options = new InsertManyOptions();

        if (StringUtils.isNotEmpty(ordered)) {
            options.ordered(Boolean.parseBoolean(ordered));
        }
        collection.insertMany(documents, options);
    }

    public JSONObject findOneDocument(String collectionName, Document query, String projection, String collation) throws JSONException {

        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        FindIterable<Document> iterable = collection.find(query);

        if (StringUtils.isNotEmpty(projection)) {
            iterable.projection(Document.parse(projection));
        } else if (StringUtils.isNotEmpty(collation)) {
            iterable.collation(stringToCollation(collation));
        }
        MongoCursor<Document> resultCursor = iterable.iterator();

        Document result;
        try {
            result = iterable.first();
        } finally {
            resultCursor.close();
        }

        if (result == null) {
            return new JSONObject(FIND_ONE_RESULT);
        } else {
            return new JSONObject(result.toJson());
        }
    }

    public JSONArray findDocuments(String collectionName, Document query, String projection, String collation, String sort, int limit) throws JSONException {

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

        JSONArray resultArray = new JSONArray();
        try {
            while (resultCursor.hasNext()) {
                Document result = resultCursor.next();
                resultArray.put(new JSONObject(result.toJson()));
            }
        } finally {
            resultCursor.close();
        }

        if (resultArray.isNull(0)) {
            resultArray.put(new JSONObject(FIND_RESULT));
        }
        return resultArray;
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
        updateResult.put(UPSERTED_ID, result.getUpsertedId());
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
        updateResult.put(UPSERTED_ID, result.getUpsertedId());
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

    public void closeConnection() {

        if (mongoClient != null) {
            this.mongoClient.close();
        }
    }
}
