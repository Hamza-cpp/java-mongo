package com.hamza_ok.models;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<Address> addresses;
}