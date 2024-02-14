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

    @Override
    public Document toDocument(Customer entaty) {

        Document document = new Document();
        if (entaty.getId() != null && !entaty.getId().isEmpty()) {
            try {
                document.append("_id", new ObjectId(entaty.getId()));
            } catch (IllegalArgumentException e) {
                logger.error("Invalid customer ID format: {}", e);
            }
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
        } catch (ClassCastException e) {
            logger.error("Error casting.", e);
        }

        return Customer.builder()
                .addresses(addresses)
                .build();
    }

}
