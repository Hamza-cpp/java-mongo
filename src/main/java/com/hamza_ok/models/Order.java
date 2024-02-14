package com.hamza_ok.models;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {

    private String id;
    private String orderNumber;
    private String customerId;
    private Date orderDate;
    private String status;
    private List<OrderItem> orderItems;
    private String shipmentId;
    private double total;

}
