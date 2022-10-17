package com.shopiroller.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

    @SerializedName("id")
    public String id;
    @SerializedName("totalPrice")
    public double totalPrice;
    @SerializedName("shippingPrice")
    public double shippingPrice;
    @SerializedName("currency")
    public String currency;
    @SerializedName("currentStatus")
    public String currentStatus;
    @SerializedName("orderCode")
    public String orderCode;
    @SerializedName("createDate")
    public String createdDate;
    public String userNote;
    @SerializedName("paymentType")
    public String paymentType;
    @SerializedName("cardNumber")
    public String cardNumber;
    @SerializedName("productList")
    public List<OrderProduct> productList;
    @SerializedName("shippingTrackingCode")
    public String shippingTrackingCode;
    @SerializedName("shippingTrackingCompany")
    public String shippingTrackingCompany;

}
