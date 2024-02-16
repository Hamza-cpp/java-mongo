package com.hamza_ok.models;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class OrderItem {

    @NonNull
    private String productId;
    private double quantity;
    private double price;
}
