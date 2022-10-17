package com.shopiroller.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderResponseInner implements Serializable {

    @SerializedName("order")
    public Order order;
    @SerializedName("payment")
    public OrderPaymentResult paymentResult;
    public String key;
    public String message;
    public boolean isUserFriendlyMessage ;
    public List<String> errors;
}
