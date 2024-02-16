package com.hamza_ok.models;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Customer {

    @NonNull
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<Address> addresses;
}