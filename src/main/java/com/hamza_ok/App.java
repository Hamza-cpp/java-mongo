package com.hamza_ok;

// import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hamza_ok.config.YamlConfig;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);    
    public static void main( String[] args ) {

        YamlConfig appConfig = new YamlConfig();
        String uri = appConfig.getString("mongodb.uri");

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("DBLP");
            MongoCollection<Document> collection = database.getCollection("publis");

            Document doc = collection.find().first();
            if (doc != null) {
                logger.info(doc.toJson());
            } else {
                logger.warn("No matching documents found.");
            }
        }
    }
}   


    