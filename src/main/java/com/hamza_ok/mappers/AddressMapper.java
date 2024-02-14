package com.hamza_ok.mappers;

import org.bson.Document;

import com.hamza_ok.models.Address;

public class AddressMapper implements DocumentMapper<Address> {

    @Override
    public Document toDocument(Address entaty) {
        return new Document("type", entaty.getType())
                .append("street", entaty.getStreet())
                .append("city", entaty.getCity())
                .append("country", entaty.getCountry())
                .append("zip", entaty.getZip());
    }

    @Override
    public Address fromDocument(Document document) {
        return Address.builder()
                .type(document.getString("type"))
                .street(document.getString("street"))
                .city(document.getString("city"))
                .country(document.getString("country"))
                .zip(document.getString("zip"))
                .build();
    }

}
