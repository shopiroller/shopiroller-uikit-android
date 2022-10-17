package com.shopiroller.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderProductModel implements Serializable {

    @SerializedName("id")
    public String id;
    @SerializedName("dn")
    public String displayName;
    @SerializedName("q")
    public int quantity;

    public OrderProductModel(String id, String displayName, int quantity) {
        this.id = id;
        this.quantity = quantity;
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "OrderProductModel{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
