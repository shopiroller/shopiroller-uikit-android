package com.shopiroller.models;

import java.io.Serializable;

public class FailurePayment implements Serializable {

    public String orderId;
    public String paymentId;

    public FailurePayment() {
    }

    public FailurePayment(String orderId) {
        this.orderId = orderId;
    }
}
