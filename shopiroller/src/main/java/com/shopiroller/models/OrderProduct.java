package com.shopiroller.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderProduct implements Serializable {


    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public String title;
    @SerializedName("featuredImage")
    public ProductImage featuredImage;
    @SerializedName("paidPrice")
    public double price;
    @SerializedName("quantity")
    public int quantity;
    @SerializedName("currency")
    public String currency;
    @SerializedName("campaignPrice")
    public double campaignPrice;

}
