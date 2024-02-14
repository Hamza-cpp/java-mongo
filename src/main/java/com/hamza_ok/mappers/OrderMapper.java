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

    @Override
    public Document toDocument(Order order) {
        Document document = new Document();

        if (order.getId() != null && !order.getId().trim().isEmpty()) {
            try {
                document.append("_id", new ObjectId(order.getId()));
            } catch (Exception e) {
                logger.error("Invalid Order ID format: {}. Exception is: {}", order.getId(), e);
            }

        }

        document.append("orderNumber", order.getOrderNumber())
                .append("customerId", order.getCustomerId())
                .append("orderDate", order.getOrderDate())
                .append("status", order.getStatus())
                .append("shipmentId", order.getShipmentId())
                .append("total", order.getTotal());

        if (order.getOrderItems() != null) {
            List<Document> itemDocuments = order.getOrderItems().stream()
                    .map(orderItem -> {
                        return new OrderItemMapper().toDocument(orderItem);
                    })
                    .collect(Collectors.toList());
            document.append("orderItems", itemDocuments);
        }

        return document;
    }

    @Override
    public Order fromDocument(Document document) {
        Order.OrderBuilder builder = Order.builder();
        try {
            builder
                    .id(document.getObjectId("_id") != null ? document.getObjectId("_id").toHexString() : null)
                    .orderNumber(document.getString("orderNumber"))
                    .customerId(document.getString("customerId"))
                    .orderDate(document.getDate("orderDate"))
                    .status(document.getString("status"))
                    .shipmentId(document.getString("shipmentId"))
                    .total(document.getDouble("total"));
        } catch (ClassCastException e) {
            logger.error("Error casting.", e);
        }

        if (document.containsKey("orderItems")) {
            List<Document> itemDocs = document.getList("orderItems", Document.class);
            List<OrderItem> orderItems = itemDocs.stream()
                    .map(itemDoc -> new OrderItemMapper().fromDocument(itemDoc))
                    .collect(Collectors.toList());
            builder.orderItems(orderItems);
        }

        return builder.build();
    }
}
