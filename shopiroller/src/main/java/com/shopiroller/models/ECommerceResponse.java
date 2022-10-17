package com.shopiroller.models;

import java.io.Serializable;

public class ECommerceResponse<T> extends ECommerceErrorResponse implements Serializable {

    public T data;
    public boolean success;

}
