package com.hamza_ok.mappers;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hamza_ok.models.Order;
import com.hamza_ok.models.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper implements DocumentMapper<Order> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Maps an Order object to a MongoDB document.
     *
     * @param order the Order object
     * @return the MongoDB document
     */
    @Override
    public Document toDocument(Order order) {
        Document document = new Document();

        try {
            document.append("_id", new ObjectId(order.getId()));
            document.append("customerId", new ObjectId(order.getCustomerId()));
            document.append("shipmentId", new ObjectId(order.getShipmentId()));
        } catch (Exception e) {
            logger.error("Invalid Order ID format: {}. Exception is: {}", order.getId(), e);
        }

        document.append("orderNumber", order.getOrderNumber())
                .append("orderDate", order.getOrderDate())
                .append("status", order.getStatus())
                .append("total", order.getTotal());

        List<Document> itemDocuments = order.getOrderItems().stream()
                .map(orderItem -> {
                    return new OrderItemMapper().toDocument(orderItem);
                })
                .collect(Collectors.toList());
        document.append("orderItems", itemDocuments);

        return document;
    }

    /**
     * Maps a MongoDB document to an Order object.
     *
     * @param document the MongoDB document
     * @return the Order object
     */
    @Override
    public Order fromDocument(Document document) {
        Order.OrderBuilder builder = Order.builder();
        try {
            List<Document> itemDocs = document.getList("orderItems", Document.class);
            List<OrderItem> orderItems = itemDocs.stream()
                    .map(itemDoc -> new OrderItemMapper().fromDocument(itemDoc))
                    .collect(Collectors.toList());
            builder
                    .id(document.getObjectId("_id").toHexString())
                    .orderNumber(document.getString("orderNumber"))
                    .customerId(document.getObjectId("customerId").toHexString())
                    .orderDate(document.getDate("orderDate"))
                    .status(document.getString("status"))
                    .shipmentId(document.getObjectId("shipmentId").toHexString())
                    .total(document.getDouble("total"))
                    .orderItems(orderItems);
        } catch (ClassCastException e) {
            logger.error("Error casting.", e);
        }

        return builder.build();
    }
}
