package com.shopiroller.models;

import java.io.Serializable;
import java.util.List;

public class OrderDetailResponse implements Serializable {

    public OrderDetailModel data;
    public String key;
    public String message;
    public boolean isUserFriendlyMessage;
    public List<String> errors;
}
