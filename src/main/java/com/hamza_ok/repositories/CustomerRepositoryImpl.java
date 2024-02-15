package com.hamza_ok.repositories;

import com.hamza_ok.mappers.DocumentMapper;
import com.hamza_ok.models.Customer;
import com.mongodb.client.MongoDatabase;

public class CustomerRepositoryImpl extends AbstractRepositoryImpl<Customer, String> {

    public CustomerRepositoryImpl(MongoDatabase database, DocumentMapper<Customer> mapper) {
        super(database, (Customer.class.getSimpleName().toLowerCase() + "s"), mapper);
    }

}
