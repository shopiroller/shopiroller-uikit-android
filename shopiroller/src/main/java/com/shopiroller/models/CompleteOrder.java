package com.shopiroller.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CompleteOrder implements Serializable {

    @SerializedName("orderId")
    public String orderId;
    @SerializedName("userNote")
    public String userNote;
    @SerializedName("paymentType")
    public String paymentType;
    @SerializedName("card")
    public OrderCard card;
    @SerializedName("bankAccount")
    public String bankAccount;
    @SerializedName("paymentAccount")
    public BankAccount paymentAccount;

    //PayPal Only
    @SerializedName("nonce")
    public String nonce;

    public CompleteOrder() {

    }

    public CompleteOrder(String orderId, String userNote, String paymentType, OrderCard card) {
        this.orderId = orderId;
        this.userNote = userNote;
        this.paymentType = paymentType;
        this.card = card;
    }

    public CompleteOrder(String orderId, String userNote, String paymentType,String bankAccount,BankAccount paymentAccount) {
        this.orderId = orderId;
        this.userNote = userNote;
        this.paymentType = paymentType;
        this.bankAccount = bankAccount;
        this.paymentAccount = paymentAccount;
    }

    public CompleteOrder(String orderId, String userNote, String paymentType) {
        this.orderId = orderId;
        this.userNote = userNote;
        this.paymentType = paymentType;
    }
}
