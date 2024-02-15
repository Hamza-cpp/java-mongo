package com.hamza_ok.repositories;

import com.hamza_ok.mappers.DocumentMapper;
import com.hamza_ok.models.Order;
import com.mongodb.client.MongoDatabase;

public class OrderRepositoryImpl extends AbstractRepositoryImpl<Order, String> {

    public OrderRepositoryImpl(MongoDatabase database, DocumentMapper<Order> mapper) {
        super(database, (Order.class.getSimpleName().toLowerCase() + "s"), mapper);
    }

}
