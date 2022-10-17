package com.shopiroller.models;


import java.io.Serializable;

public class OrderPaymentEvent implements Serializable {

    public String paymentType;
    public BankAccount bankAccount;
    public OrderCard orderCard;


}
