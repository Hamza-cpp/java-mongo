package com.hamza_ok.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoConfig implements AutoCloseable {
    private MongoClient mongoClient;

    public MongoConfig(String databaseUri) {
        this.mongoClient = MongoClients.create(databaseUri);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

}
