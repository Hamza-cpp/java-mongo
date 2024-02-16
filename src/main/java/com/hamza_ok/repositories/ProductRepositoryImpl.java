package com.hamza_ok.repositories;

import com.hamza_ok.mappers.DocumentMapper;
import com.hamza_ok.models.Product;
import com.mongodb.client.MongoDatabase;

public class ProductRepositoryImpl extends AbstractRepositoryImpl<Product, String> {

    public ProductRepositoryImpl(MongoDatabase database, DocumentMapper<Product> mapper) {
        super(database, (Product.class.getSimpleName().toLowerCase() + "s"), mapper);
    }

}
