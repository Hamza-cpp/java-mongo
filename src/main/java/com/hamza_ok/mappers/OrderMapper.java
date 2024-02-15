package com.hamza_ok.mappers;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hamza_ok.models.Order;
import com.hamza_ok.models.OrderItem;

import java.util.List;
import java.util.function.Consumer;
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

        // Handling Order ID
        appendObjectId(document, "_id", order.getId(), order::setId, "Order");

        // Handling Customer ID
        appendObjectId(document, "customerId", order.getCustomerId(), order::setCustomerId, "Customer");

        // Handling Shipment ID
        appendObjectId(document, "shipmentId", order.getShipmentId(), order::setShipmentId, "Shipment");

        try {
            document.append("_id", new ObjectId(order.getId()));
        } catch (IllegalArgumentException e) {

            ObjectId _ObjectId = new ObjectId();
            String oldId = order.getId();
            order.setId(_ObjectId.toHexString());
            document.append("_id", _ObjectId);

            logger.error("Invalid Order ID format: {}, generating a new random ID : {}",
                    oldId, order.getId(), e);
        }
        try {
            document.append("customerId", new ObjectId(order.getCustomerId()));
        } catch (IllegalArgumentException e) {

            ObjectId _ObjectId = new ObjectId();
            String oldId = order.getCustomerId();
            order.setCustomerId(_ObjectId.toHexString());
            document.append("_id", _ObjectId);

            logger.error("Invalid Customer ID format: {}, generating a new random ID : {}",
                    oldId, order.getCustomerId(), e);
        }
        try {

            document.append("shipmentId", new ObjectId(order.getShipmentId()));
        } catch (IllegalArgumentException e) {

            ObjectId _ObjectId = new ObjectId();
            String oldId = order.getShipmentId();
            order.setShipmentId(_ObjectId.toHexString());
            document.append("_id", _ObjectId);

            logger.error("Invalid Shippment ID format: {}, generating a new random ID : {}",
                    oldId, order.getShipmentId(), e);
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

    /**
     * Helper method to append ObjectId fields to a document, handling invalid ID
     * formats by generating a new ID.
     * 
     * @param document      The MongoDB document to append to.
     * @param fieldName     The field name for the document.
     * @param fieldValue    The current field value supposed to be an ID.
     * @param idSetter      A Consumer to set a new ID back into the object in case
     *                      of format issues.
     * @param idDescription A description for logging purpose
     *                      (e.g.,"Order","Customer").
     */
    private void appendObjectId(Document document, String fieldName, String fieldValue, Consumer<String> idSetter,
            String idDescription) {
        try {
            document.append(fieldName, new ObjectId(fieldValue));
        } catch (IllegalArgumentException e) {
            ObjectId newObjectId = new ObjectId();
            idSetter.accept(newObjectId.toHexString());
            document.append(fieldName, newObjectId);
            logger.error("Invalid {} ID format: {}, generating a new random ID : {}", idDescription, fieldValue,
                    newObjectId.toHexString(), e);
        }
    }
}
