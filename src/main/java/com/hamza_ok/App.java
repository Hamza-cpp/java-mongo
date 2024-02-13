package com.hamza_ok;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hamza_ok.DAO.UserDao;
import com.hamza_ok.services.UserService;
import com.hamza_ok.DAO.UserDaoImpl;
import com.hamza_ok.config.YamlConfig;
import com.hamza_ok.models.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        final YamlConfig appConfig = new YamlConfig();
        final String DB_NAME = appConfig.getString("mongodb.name");
        final String DB_URI = appConfig.getString("mongodb.uri");

        try (MongoClient mongoClient = MongoClients.create(DB_URI)) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);

            UserDao userDao = new UserDaoImpl(database);
            UserService userService = new UserService(userDao);
            User user = new User();
            User user1 = new User();

            user.setName("Cheb hassni");
            user.setEmail("hassni@gmail.com");
            user1.setName("Cheb Bilal");
            user1.setEmail("Bilal@gmail.com");

            try {
                User createdUser = userService.save(user);
                logger.info("Created user : {}", createdUser.toString());
            } catch (Exception e) {
                logger.error("Fail to create User:", e);
            }
            try {
                User createdUser = userService.save(user1);
                logger.info("Created user : {}", createdUser.toString());
            } catch (Exception e) {
                logger.error("Fail to create User:", e);
            }

            try {
                User user2 = userService.find(user.getId());
                logger.info("Catch u: {}",user2.toString());
            } catch (Exception e) {
                logger.error("Fail to find User:", e);
            }

            try {
                List<User> users = userService.findAll();
                if (!users.isEmpty()) {
                    for (User us : users) {

                        logger.info("User : {}", us.toString());
                    }
                } else {
                    logger.info("No users to fetch");
                }
            } catch (Exception e) {
                logger.error("Fail to find Users:", e);
            }

            User newUser = new User();
            newUser.setName("John Dao");
            newUser.setEmail("John@example.com");

            try {
                User updatedUser = userService.update(user.getId(), newUser);
                logger.info("User updated : {}", updatedUser.toString());
            } catch (Exception e) {
                logger.error("Fail to update User:", e);
            }

            try {
                userService.delete(user1.getId());
            } catch (Exception e) {
                logger.error("Fail to delete User:", e);
            }
        }
    }
}
