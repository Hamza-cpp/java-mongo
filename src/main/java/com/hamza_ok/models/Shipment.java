package com.hamza_ok.models;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Shipment {

    private String id;
    private String orderId;
    private String status;
    private Date shipDate;
    private String carrier;
    private String trackingNumber;
    private Date estimatedDeliveryDate;
    private Address address;
}
