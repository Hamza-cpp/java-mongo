package com.hamza_ok.models;

import java.util.Date;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Shipment {

    @NonNull
    private String id;
    @NonNull
    private String orderId;
    private String status;
    private Date shipDate;
    private String carrier;
    private String trackingNumber;
    private Date estimatedDeliveryDate;
    private Address address;
}
