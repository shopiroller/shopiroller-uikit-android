package com.shopiroller.models;

import java.io.Serializable;

public class OrderPaymentResult implements Serializable {

    public boolean isSuccess;
    public String id;
    public String state;
    public int stateCode;
    public String status;
    public String transactionId;
    public String conversationId;
    public String message;
    public String _3DSecureHtml;
    //Stripe && PayPal
    public String token;

    //Stripe
    public String publishableKey;
}
