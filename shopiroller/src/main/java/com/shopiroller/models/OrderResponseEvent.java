package com.shopiroller.models;

public class OrderResponseEvent {

    public OrderResponseInner orderResponse;
    public OrderFailedResponse failedResponse;

    public OrderResponseEvent(OrderResponseInner orderResponse) {
        this.orderResponse = orderResponse;
    }

    public OrderResponseEvent(OrderFailedResponse failedResponse) {
        this.failedResponse = failedResponse;
    }
}
