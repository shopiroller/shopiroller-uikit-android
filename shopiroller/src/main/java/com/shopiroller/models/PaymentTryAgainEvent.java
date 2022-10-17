package com.shopiroller.models;

import java.io.Serializable;

public class PaymentTryAgainEvent implements Serializable {
    public String orderId;

    public PaymentTryAgainEvent(String orderId) {
        this.orderId = orderId;
    }
}
