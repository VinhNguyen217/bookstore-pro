package com.bookstore.model.request;

import lombok.Data;

@Data
public class PaypalModel {
    private double total;
    private String currency;
    private String cancelUrl;
    private String successUrl;
}
