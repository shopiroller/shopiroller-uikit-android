package com.shopiroller.util;

public enum OrderOptionOrientation {

    Ascending("Ascending"),
    Descending("Descending");

    private String orientation;

    OrderOptionOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getOrientation() {
        return orientation;
    }

}
