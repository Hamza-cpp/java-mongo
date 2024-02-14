package com.hamza_ok;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hamza_ok.config.MongoConfig;
import com.hamza_ok.config.YamlConfig;
import com.mongodb.client.MongoDatabase;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        final YamlConfig appConfig = new YamlConfig();
        final String DB_NAME = appConfig.getString("mongodb.name");
        final String DB_URI = appConfig.getString("mongodb.uri");

        try (MongoConfig mongoConfig = new MongoConfig(DB_URI)) {
            MongoDatabase database = mongoConfig.getMongoClient().getDatabase(DB_NAME);

        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            logger.info("Application finished.");
        }
    }
}
