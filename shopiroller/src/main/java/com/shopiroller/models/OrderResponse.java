package com.shopiroller.models;

import java.io.Serializable;
import java.util.List;

public class OrderResponse implements Serializable {

    public OrderResponseInner data;
    public List<String> errors;
}
