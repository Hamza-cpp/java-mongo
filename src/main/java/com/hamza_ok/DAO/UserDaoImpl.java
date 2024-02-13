package com.hamza_ok.DAO;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hamza_ok.models.User;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private final MongoCollection<Document> usersCollection;

    public UserDaoImpl(MongoDatabase mongoDatabase) {
        this.usersCollection = mongoDatabase.getCollection("users");
    }

    /**
     * Creates a new user in the collection.
     *
     * @param user the user object to create
     * @return the created user object with its ID populated
     */
    @Override
    public User createUser(User user) {
        try {
            Document userDoc = new Document()
                    .append("name", user.getName())
                    .append("email", user.getEmail());

            InsertOneResult result = usersCollection.insertOne(userDoc);
            String insertedId = result.getInsertedId().asObjectId().getValue().toString();
            logger.info("User created successfully with ID: {}", insertedId);
            user.setId(insertedId);

        } catch (MongoException e) {
            logger.error("Unable to create user: ", e);
        }
        return user;
    }

    /**
     * Returns a user document from the collection based on the user's ID.
     *
     * @param id the ID of the user to retrieve
     * @return the user document, or null if no user is found with the specified ID
     */
    @Override
    public User getUserById(String id) {
        User user = null;
        try {
            ObjectId _ObjectId = new ObjectId(id);
            Document userDoc = usersCollection.find(eq("_id", _ObjectId)).first();
            if (userDoc != null) {
                user = new User();
                user.setId(id);
                user.setName(userDoc.getString("name"));
                user.setEmail(userDoc.getString("email"));
                logger.info("User found with ID: {} Name: {} Email: {}", user.getId(), user.getName(), user.getEmail());
            } else {
                logger.info("User not found with ID: {}", id);
            }
        } catch (IllegalArgumentException e) {
            logger.error("ID is not a valid hex string representation of an ObjectId", e);
        }
        return user;
    }

    /**
     * Returns a list of all users in the collection.
     *
     * @return a list of all users
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();

        MongoCursor<Document> allUsers = usersCollection.find().iterator();
        try (allUsers) {
            while (allUsers.hasNext()) {
                Document userDoc = allUsers.next();
                User user = new User();
                user.setId(userDoc.getObjectId("_id").toHexString());
                user.setName(userDoc.getString("name"));
                user.setEmail(userDoc.getString("email"));
                users.add(user);
            }
        } catch (ClassCastException e) {
            logger.error("Casting exception", e);
        }

        if (users.isEmpty()) {
            logger.info("No users to fetch");
        }
        return users;
    }

    /**
     * Updates an existing user in the collection.
     *
     * @param id      the ID of the user to update
     * @param newUser the updated user object
     * @return the updated user object, or null if no user was found with the
     *         specified ID
     */
    @Override
    public User updateUser(String id, User newUser) {
        User user = null;
        try {
            Document userDocument = usersCollection.find(eq("_id", new ObjectId(id))).first();
            if (userDocument != null) {
                user = new User();
                user.setId(id);
                user.setName(newUser.getName());
                user.setEmail(newUser.getEmail());

                Bson query = eq("_id", new ObjectId(id));

                Bson updates = Updates.combine(
                        Updates.set("name", newUser.getName()),
                        Updates.set("email", newUser.getEmail()));

                UpdateResult updateResult = usersCollection.updateOne(query, updates);

                logger.info("User found with ID: {} and updated.", user.getId());
                logger.info("Modified document count: {}", updateResult.getModifiedCount());
            } else {
                logger.info("No user found with ID: {}", id);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid ObjectId: {}", id, e);
        } catch (MongoException e) {
            logger.error("Error updating user with ID: {}", id, e);
        }
        return user;
    }

    /**
     * Deletes a user from the collection based on the user's ID.
     *
     * @param id the ID of the user to delete
     */
    @Override
    public void deleteUser(String id) {
        try {
            ObjectId _ObjectId = new ObjectId(id);
            Bson query = eq("_id", _ObjectId);
            DeleteResult deleteResult = usersCollection.deleteOne(query);
            if (deleteResult.getDeletedCount() > 0) {
                logger.info("User with ID: {} deleted successfully", id);
            } else {
                logger.info("No user found with ID: {}", id);
            }
        } catch (IllegalArgumentException e) {
            logger.error("ID is not a valid hex string representation of an ObjectId", e);
        }
    }

}
