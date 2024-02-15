package com.hamza_ok.models;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Product {

    @NonNull
    private String id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private List<String> categories;

}
