package com.hamza_ok.repositories;

import com.hamza_ok.mappers.DocumentMapper;
import com.hamza_ok.models.Shipment;
import com.mongodb.client.MongoDatabase;

public class ShipmentRepositoryImpl extends AbstractRepositoryImpl<Shipment, String> {

    public ShipmentRepositoryImpl(MongoDatabase database, DocumentMapper<Shipment> mapper) {
        super(database, (Shipment.class.getSimpleName().toLowerCase() + "s"), mapper);
    }

}
