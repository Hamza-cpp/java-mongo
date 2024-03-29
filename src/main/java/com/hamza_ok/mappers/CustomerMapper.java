package com.hamza_ok.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hamza_ok.models.Address;
import com.hamza_ok.models.Customer;

public class CustomerMapper implements DocumentMapper<Customer> {

    private final static Logger logger = LoggerFactory.getLogger(CustomerMapper.class);

    /**
     * Converts a Customer entity to a MongoDB document.
     *
     * @param customer the Customer entity to convert
     * @return the MongoDB document representing the Customer entity
     */
    @Override
    public Document toDocument(Customer entaty) {

        Document document = new Document();

        try {
            document.append("_id", new ObjectId(entaty.getId()));
        } catch (IllegalArgumentException e) {

            ObjectId _ObjectId = new ObjectId();
            String oldId = entaty.getId();
            entaty.setId(_ObjectId.toHexString());
            document.append("_id", _ObjectId);

            logger.error("Invalid customer ID format: {}, generating a new random ID : {}",
                    oldId, entaty.getId(), e);
        }

        document.append("firstName", entaty.getFirstName())
                .append("lastName", entaty.getLastName())
                .append("email", entaty.getEmail())
                .append("phone", entaty.getPhone());

        List<Document> addressDocuments = entaty.getAddresses() != null
                ? entaty.getAddresses().stream().map(address -> {
                    return new AddressMapper().toDocument(address);
                }).collect(Collectors.toList())
                : null;

        return document.append("addresses", addressDocuments);
    }

    /**
     * Converts a MongoDB document to a Customer entity.
     *
     * @param document the MongoDB document to convert
     * @return the Customer entity represented by the MongoDB document
     */
    @Override
    public Customer fromDocument(Document document) {
        Customer.CustomerBuilder customerBuilder = Customer.builder();
        List<Address> addresses = null;

        try {
            customerBuilder
                    .id(document.getObjectId("_id").toHexString())
                    .firstName(document.getString("firstName"))
                    .lastName(document.getString("lastName"))
                    .email(document.getString("email"))
                    .phone(document.getString("phone"));

            if (document.containsKey("addresses")) {
                List<Document> addressDocs = document.getList("addresses", Document.class);
                addresses = addressDocs.stream().map(doc -> new AddressMapper().fromDocument(doc))
                        .collect(Collectors.toList());
            }
            return customerBuilder
                    .addresses(addresses)
                    .build();

        } catch (ClassCastException | NullPointerException e) {
            logger.error("Error casting.", e);
        }

        return customerBuilder.build();
    }

}
