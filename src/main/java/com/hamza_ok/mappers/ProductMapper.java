package com.hamza_ok.mappers;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hamza_ok.models.Product;

public class ProductMapper implements DocumentMapper<Product> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Maps a Product object to a MongoDB document.
     *
     * @param entaty the Product object
     * @return the MongoDB document
     */
    @Override
    public Document toDocument(Product entaty) {

        Document document = new Document();

        try {
            document.append("_id", new ObjectId(entaty.getId()));

        } catch (Exception e) {
            ObjectId _ObjectId = new ObjectId();
            String oldId = entaty.getId();
            entaty.setId(_ObjectId.toHexString());
            document.append("_id", _ObjectId);

            logger.error("Invalid Product ID format: {}, generating a new random ID : {}",
                    oldId, entaty.getId(), e);
        }

        return document
                .append("name", entaty.getName())
                .append("description", entaty.getDescription())
                .append("price", entaty.getPrice())
                .append("stock", entaty.getStock())
                .append("category", entaty.getCategories());
    }

    /**
     * Maps a MongoDB document to a Product object.
     *
     * @param document the MongoDB document
     * @return the Product object
     */
    @Override
    public Product fromDocument(Document document) {
        Product.ProductBuilder productBuilder = Product.builder();

        try {

            productBuilder
                    .id(document.getObjectId("_id").toHexString())
                    .name(document.getString("name"))
                    .description(document.getString("description"))
                    .price(document.getDouble("price"))
                    .stock(document.getInteger("stock", 0));

            List<String> categoryList = document.getList("categories", String.class);
            productBuilder.categories(categoryList);
        } catch (ClassCastException e) {
            logger.error("Error casting product fields from document. {}", e);
        }

        return productBuilder.build();
    }
}
