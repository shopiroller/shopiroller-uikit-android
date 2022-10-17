package com.shopiroller.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SliderSlidesModel implements Serializable, Parcelable {

    private ProductImage imageUrl;
    private String id;
    private int orderIndex;
    private String createDate;
    private String updateDate;
    private boolean isActive;
    private String navigationType;
    private String navigationLink;

    public SliderSlidesModel() {
    }

    public SliderSlidesModel(ProductImage imageUrl, String id, int orderIndex, String createDate, String updateDate, boolean isActive, String navigationType, String navigationLink) {
        this.imageUrl = imageUrl;
        this.id = id;
        this.orderIndex = orderIndex;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.isActive = isActive;
        this.navigationType = navigationType;
        this.navigationLink = navigationLink;
    }

    public ProductImage getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ProductImage imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getNavigationType() {
        return navigationType;
    }

    public void setNavigationType(String navigationType) {
        this.navigationType = navigationType;
    }

    public String getNavigationLink() {
        return navigationLink;
    }

    public void setNavigationLink(String navigationLink) {
        this.navigationLink = navigationLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
