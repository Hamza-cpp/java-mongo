package com.hamza_ok.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {

    private String type;
    private String street;
    private String city;
    private String country;
    private String zip;
}
