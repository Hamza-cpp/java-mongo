package com.hamza_ok.mappers;

import org.bson.Document;

public interface DocumentMapper<T> {

    Document toDocument(T entaty);

    T fromDocument(Document document);
}
