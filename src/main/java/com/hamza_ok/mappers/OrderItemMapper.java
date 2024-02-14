package com.hamza_ok.mappers;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hamza_ok.models.OrderItem;

public class OrderItemMapper implements DocumentMapper<OrderItem> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Document toDocument(OrderItem entaty) {
        Document document = new Document();
        if (entaty.getProductId() != null && !entaty.getProductId().isEmpty()) {
            try {

                ObjectId productId = new ObjectId(entaty.getProductId());
                document.append("productId", productId);
            } catch (IllegalArgumentException e) {
                logger.error("Invalid productId format: {}", e);
            }
        }
        return document
                .append("quantity", entaty.getQuantity())
                .append("price", entaty.getPrice());

    }

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
