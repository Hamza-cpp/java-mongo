package com.hamza_ok.repositories;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hamza_ok.mappers.DocumentMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

public abstract class AbstractRepositoryImpl<T, ID> implements Repository<T, ID> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected final MongoCollection<Document> collection;
    private final DocumentMapper<T> mapper;

    protected AbstractRepositoryImpl(MongoDatabase database, String COLLECTION_NAME, DocumentMapper<T> mapper) {
        this.collection = database.getCollection(COLLECTION_NAME);
        this.mapper = mapper;
    }

    /**
     * Saves an entity to the collection.
     *
     * @param entity the entity to save
     * @return the saved entity in an Optional container
     */
    @Override
    public Optional<T> save(T entity) {
        try {
            Document doc = mapper.toDocument(entity);
            InsertOneResult result = collection.insertOne(doc);
            logger.info("{} created successfully with ID: {}", entity.getClass().getSimpleName(),
                    result.getInsertedId().asObjectId().getValue().toHexString());

        } catch (Exception e) {
            logger.error("Error saving Entity : {} :", entity.getClass().getSimpleName(), e);
        }
        return Optional.ofNullable(entity);
    }

    /**
     * Retrieves an entity from the collection based on the specified ID.
     *
     * @param id the ID of the entity to retrieve
     * @return an Optional container containing the entity, if found, otherwise an
     *         empty Optional
     */
    @Override
    public Optional<T> find(ID id) {

        try {
            Document doc = collection.find(eq("_id", new ObjectId(id.toString()))).first();

            if (doc != null) {
                T entity = mapper.fromDocument(doc);
                logger.info("{} found successfully", entity.getClass().getSimpleName());
                return Optional.of(entity);
            } else {
                logger.info("Unable to find Entity with ID: {}", id);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Error finding entity with ID : {} ", id, e);
            return Optional.empty();
        }
    }

    /**
     * Fetches all entities from the collection.
     *
     * @return a list of entities or an empty list
     */
    public List<T> findAll() {
        List<T> entities = new ArrayList<>();
        try {
            MongoCursor<Document> cursor = collection.find().iterator();

            try (cursor) {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    T entity = mapper.fromDocument(doc);
                    entities.add(entity);
                }
            }
        } catch (Exception e) {
            logger.error("Error fetching all entities: {}", e);
        }
        return entities;
    }

    /**
     * Updates an existing entity in the collection based on the specified ID.
     *
     * @param entity the updated entity
     * @param id     the ID of the entity to update
     * @return the updated entity, if successful, otherwise an empty Optional
     */
    @Override
    public Optional<T> update(T entity, ID id) {
        try {
            if (collection.find(eq("_id", new ObjectId(id.toString()))).first() != null) {

                Document doc = mapper.toDocument(entity);
                doc.put("_id", new ObjectId(id.toString()));

                Bson filter = eq("_id", new ObjectId(id.toString()));
                UpdateResult result = collection.replaceOne(filter, doc);

                if (result.wasAcknowledged() && result.getModifiedCount() > 0) {

                    logger.info("{} updated successfully with ID: {}", entity.getClass().getSimpleName(), id);
                    T updatedEntity = mapper.fromDocument(doc);
                    return Optional.of(updatedEntity);
                }

            } else {
                logger.info("No user found with ID: {}", id);
                logger.info("---> Update Faild <---");
                return Optional.empty();
            }

        } catch (Exception e) {
            logger.error("Error Updating {} with ID {} : {}", entity.getClass().getSimpleName(), id, e);
            return Optional.empty();
        }
        return Optional.empty();
    }

    /**
     * Deletes an entity from the collection based on the specified ID.
     *
     * @param id the ID of the entity to delete
     * @return true if the entity was deleted, false if not
     */
    @Override
    public boolean delete(ID id) {
        try {

            Bson filter = eq("_id", new ObjectId(id.toString()));
            DeleteResult result = collection.deleteOne(filter);

            if (result.wasAcknowledged() && result.getDeletedCount() > 0) {
                logger.info("User Deleted successfully with ID: {}", id);
                return true;
            } else {
                logger.info("No user found with ID: {}", id);
                logger.info("---> Delete Faild <---");
                return false;
            }
        } catch (Exception e) {
            logger.error("Error deleting entity with ID {} :", id, e);
            return false;
        }
    }

}