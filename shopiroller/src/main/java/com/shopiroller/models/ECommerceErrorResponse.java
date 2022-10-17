package com.shopiroller.models;

import java.io.Serializable;
import java.util.List;

public class ECommerceErrorResponse implements Serializable {

    public boolean isUserFriendlyMessage;
    public String key;
    public String message;
    public List<String> errors;

    // Only for order
    public Order order;
    public OrderPaymentResult paymentResult;

}
