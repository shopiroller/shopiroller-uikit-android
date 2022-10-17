package com.shopiroller.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductListModel implements Serializable {

    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public String title;
    @SerializedName("stock")
    public int stock;
    @SerializedName("price")
    public double price;
    @SerializedName("campaignPrice")
    public double campaignPrice;
    @SerializedName("shippingPrice")
    public double shippingPrice;
    @SerializedName("currency")
    public String currency;
    @SerializedName("featuredImage")
    public ProductImage featuredImage;

    public boolean isValid() {
        return id != null && title != null && currency != null;
    }
}
