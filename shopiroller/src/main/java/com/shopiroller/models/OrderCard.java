package com.shopiroller.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderCard implements Serializable {

    @SerializedName("cardHolderName")
    public String cardHolderName;
    @SerializedName("cardNumber")
    public String cardNumber;
    @SerializedName("expireMonth")
    public String expireMonth;
    @SerializedName("expireYear")
    public String expireYear;
    @SerializedName("cvc")
    public String cvc;

    @Override
    public String toString() {
        return "OrderCard{" +
                "cardHolderName='" + cardHolderName + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", expireMonth='" + expireMonth + '\'' +
                ", expireYear='" + expireYear + '\'' +
                ", cvc='" + cvc + '\'' +
                '}';
    }
}
