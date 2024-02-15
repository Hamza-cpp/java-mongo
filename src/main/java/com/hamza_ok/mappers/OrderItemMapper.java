package com.hamza_ok.mappers;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hamza_ok.models.OrderItem;

public class OrderItemMapper implements DocumentMapper<OrderItem> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Converts an OrderItem entity to a MongoDB document.
     *
     * @param entaty the OrderItem entity to convert
     * @return the MongoDB document
     */
    @Override
    public Document toDocument(OrderItem entaty) {
        Document document = new Document();
        try {

            ObjectId productId = new ObjectId(entaty.getProductId());
            document.append("productId", productId);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid productId format: {}", e);
        }

        return document
                .append("quantity", entaty.getQuantity())
                .append("price", entaty.getPrice());

    }

    /**
     * Converts a MongoDB document to an OrderItem entity.
     *
     * @param document the MongoDB document to convert
     * @return the OrderItem entity
     */
    @Override
    public OrderItem fromDocument(Document document) {
        OrderItem.OrderItemBuilder orderItemBuilder = OrderItem.builder();
        try {
            orderItemBuilder
                    .productId(document.getObjectId("productId").toHexString())
                    .quantity(document.getDouble("quantity"))
                    .price(document.getDouble("price"));
        } catch (ClassCastException e) {
            logger.error("Error casting.", e);
        }
        return orderItemBuilder.build();
    }

}
