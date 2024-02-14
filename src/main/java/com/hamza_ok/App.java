package com.hamza_ok;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hamza_ok.DAO.UserDao;
import com.hamza_ok.services.UserService;
import com.hamza_ok.DAO.UserDaoImpl;
import com.hamza_ok.config.MongoConfig;
import com.hamza_ok.config.YamlConfig;
import com.hamza_ok.models.User;
import com.mongodb.client.MongoDatabase;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        final YamlConfig appConfig = new YamlConfig();
        final String DB_NAME = appConfig.getString("mongodb.name");
        final String DB_URI = appConfig.getString("mongodb.uri");

        try (MongoConfig mongoConfig = new MongoConfig(DB_URI)) {

            MongoDatabase database = mongoConfig.getMongoClient().getDatabase(DB_NAME);

            UserDao userDao = new UserDaoImpl(database);
            UserService userService = new UserService(userDao);
            User user = User.builder()
                    .name("Cheb hassni")
                    .email("hassni@gmail.com")
                    .build();
            User user1 = User.builder()
                    .name("Cheb Bilal")
                    .email("Bilal@gmail.com")
                    .build();

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
                logger.info("Catch u: {}", user2.toString());
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

            User newUser = User.builder()
                    .name("John Dao")
                    .email("John@example.com")
                    .build();

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
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            logger.info("Application finished.");
        }
    }
}
