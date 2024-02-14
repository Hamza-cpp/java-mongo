package com.hamza_ok.models;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {

    private String id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private List<String> categories;

}
