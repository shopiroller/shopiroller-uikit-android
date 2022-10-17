package com.shopiroller.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ProductImage implements Serializable,Parcelable {

    //Thumbnail
    public String t;
    //Default(Normal)
    public String n;

    public ProductImage() {
    }

    protected ProductImage(Parcel in) {
        t = in.readString();
        n = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(t);
        dest.writeString(n);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProductImage> CREATOR = new Parcelable.Creator<ProductImage>() {
        @Override
        public ProductImage createFromParcel(Parcel in) {
            return new ProductImage(in);
        }

        @Override
        public ProductImage[] newArray(int size) {
            return new ProductImage[size];
        }
    };
}
