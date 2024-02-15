package com.hamza_ok.mappers;

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

        try {
            document.append("_id", new ObjectId(shipment.getId()));
            document.append("orderId", new ObjectId(shipment.getOrderId()));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid Shipment ID format: {}", shipment.getId());
        }

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
}