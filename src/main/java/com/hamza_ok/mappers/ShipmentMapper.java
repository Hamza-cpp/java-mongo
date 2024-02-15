package com.hamza_ok.mappers;

import java.util.function.Consumer;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hamza_ok.models.Address;
import com.hamza_ok.models.Shipment;

public class ShipmentMapper implements DocumentMapper<Shipment> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Converts a Shipment object to a MongoDB Document.
     *
     * @param shipment the Shipment object to convert
     * @return the MongoDB Document representation of the Shipment object
     */
    @Override
    public Document toDocument(Shipment shipment) {
        Document document = new Document();

        appendObjectId(document, "_id", shipment.getId(), shipment::setId, "Shippment");
        appendObjectId(document, "orderId", shipment.getOrderId(), shipment::setOrderId, "Order");

        document.append("status", shipment.getStatus())
                .append("shipDate", shipment.getShipDate())
                .append("carrier", shipment.getCarrier())
                .append("trackingNumber", shipment.getTrackingNumber())
                .append("estimatedDeliveryDate", shipment.getEstimatedDeliveryDate());

        if (shipment.getAddress() != null) {
            Document addressDoc = new AddressMapper().toDocument(shipment.getAddress());
            document.append("address", addressDoc);
        }

        return document;
    }

    /**
     * Converts a MongoDB Document to a Shipment object.
     *
     * @param document the MongoDB Document to convert
     * @return the Shipment object representation of the MongoDB Document
     */
    @Override
    public Shipment fromDocument(Document document) {
        Shipment.ShipmentBuilder builder = Shipment.builder();
        try {
            builder
                    .id(document.getObjectId("_id").toHexString())
                    .orderId(document.getObjectId("orderId").toHexString())
                    .status(document.getString("status"))
                    .shipDate(document.getDate("shipDate"))
                    .carrier(document.getString("carrier"))
                    .trackingNumber(document.getString("trackingNumber"))
                    .estimatedDeliveryDate(document.getDate("estimatedDeliveryDate"));
        } catch (ClassCastException e) {
            logger.error("Error casting.", e);
        }

        Document addressDoc = (Document) document.get("address");
        if (addressDoc != null) {
            Address address = new AddressMapper().fromDocument(addressDoc);
            builder.address(address);
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