package com.shopiroller.models;

import java.util.List;

public class OrderListResponse {
    public List<Order> data;
    public String key;
    public String message;
    public boolean isUserFriendlyMessage ;
    public List<String> errors;
}
