package com.hamza_ok.models;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Order {

    @NonNull
    private String id;
    private String orderNumber;
    @NonNull
    private String customerId;
    private Date orderDate;
    private String status;
    @NonNull
    private List<OrderItem> orderItems;
    @NonNull
    private String shipmentId;
    private double total;

}
