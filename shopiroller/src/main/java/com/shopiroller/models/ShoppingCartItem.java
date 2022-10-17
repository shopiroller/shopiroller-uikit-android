package com.shopiroller.models;

import java.io.Serializable;
import java.util.List;

public class ShoppingCartItem implements Serializable {
    public String id;
    public String productId;
    public ProductDetailModel product;
    public int quantity;
    public double price;
    public boolean isValid;
    public String createDate;
    public String updateDate;
    public List<String> videos;
    public List<ShoppingCartMessage> messages;
}
