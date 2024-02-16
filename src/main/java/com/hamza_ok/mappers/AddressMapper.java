package com.hamza_ok.mappers;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hamza_ok.models.Address;

public class AddressMapper implements DocumentMapper<Address> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

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
        Address.AddressBuilder builder = Address.builder();
        try {

            builder
                    .type(document.getString("type"))
                    .street(document.getString("street"))
                    .city(document.getString("city"))
                    .country(document.getString("country"))
                    .zip(document.getString("zip"));
        } catch (ClassCastException e) {
            logger.error("Error casting.", e);
        }

        return builder.build();
    }

}
