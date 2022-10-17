package com.shopiroller.models;

import java.io.Serializable;

public class OrderFailedResponse implements Serializable {
    public Order order;
    public OrderPaymentResult paymentResult;

    public OrderFailedResponse(ECommerceErrorResponse commerceErrorResponse) {
        order = commerceErrorResponse.order;
        paymentResult = commerceErrorResponse.paymentResult;
    }

    public OrderFailedResponse() {
    }
}
