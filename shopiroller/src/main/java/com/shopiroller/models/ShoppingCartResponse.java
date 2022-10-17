package com.shopiroller.models;

import java.io.Serializable;
import java.util.List;

public class ShoppingCartResponse implements Serializable {

    public String userId;
    public String appId;
    public String culture;
    public String currency;
    public List<ShoppingCartItem> items;
    public List<ShoppingCartItem> invalidItems;
    public List<ShoppingCartMessage> messages;
    public double subTotalPrice;
    public double totalPrice;
    public double shippingPrice;
    public String updateDate;
    public String createDate;
    public String couponId;
    public Double couponPrice;
    public String couponName;

}
