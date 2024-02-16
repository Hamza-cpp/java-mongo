package com.hamza_ok;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hamza_ok.config.MongoConfig;
import com.hamza_ok.config.YamlConfig;
import com.hamza_ok.mappers.CustomerMapper;
import com.hamza_ok.mappers.DocumentMapper;
import com.hamza_ok.models.Address;
import com.hamza_ok.models.Customer;
import com.hamza_ok.repositories.CustomerRepositoryImpl;
import com.mongodb.client.MongoDatabase;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        final YamlConfig appConfig = new YamlConfig();
        final String DB_NAME = appConfig.get("mongodb.name", String.class);
        final String DB_URI = appConfig.get("mongodb.uri",String.class);

        try (MongoConfig mongoConfig = new MongoConfig(DB_URI)) {
            MongoDatabase database = mongoConfig.getMongoClient().getDatabase(DB_NAME);

            DocumentMapper<Customer> customerMapper = new CustomerMapper();
            CustomerRepositoryImpl customerRepository = new CustomerRepositoryImpl(database, customerMapper);

            Customer.CustomerBuilder customerBuilder = Customer.builder();
            customerBuilder
                    .firstName("Cheb Hasny")
                    .lastName("the best")
                    .id("12345678")
                    .email("bilal@gmail.com")
                    .phone("067843377373")
                    .addresses(Arrays.asList(
                            Address.builder()
                                    .type("billing Address")
                                    .street("douar Square")
                                    .city("Kenitra")
                                    .country("Morroco")
                                    .zip("1435279")
                                    .build(),
                            Address.builder()
                                    .type("Shipping Address")
                                    .city("Arbaoua")
                                    .street("tanagra")
                                    .country("9arta Khano")
                                    .zip("12345")
                                    .build()));

            Customer customer = customerBuilder.build();
            // logger.info(customer.toString());

            // customerRepository.save(customer);
            // logger.info(customer.toString());

            logger.info("Deleted : {}", customerRepository.delete("65ce599192c45c6f83b578ab"));
            customerRepository.findAll()
                    .stream().forEach(cust -> logger.info(cust.toString()));
            customerRepository.update(customer,
                    "65ce599192c45c6f83b578ab").ifPresentOrElse(
                            updatedCustomer -> logger.info(updatedCustomer.toString()),
                            () -> logger.warn("Failed to update customer."));
            // customerRepository.delete("65ce522b01b0f3490d8773c1");
            // customerRepository.update(customer, "65ce4fbdc8dea33780e95ebc");

        } catch (Exception e) {
            logger.error("Some Thing Go Wrong", e);
        } finally {
            logger.info("Application finished.");
        }
    }
}
