package com.shopiroller.models;

import java.io.Serializable;

public class SupportedPaymentType implements Serializable {

    public String paymentType;
    public String description;

    // PayPal Only
    public PaymentConfiguration configuration;

    public SupportedPaymentType() {
    }

    public SupportedPaymentType(String paymentType, String description) {
        this.paymentType = paymentType;
        this.description = description;
    }
}
