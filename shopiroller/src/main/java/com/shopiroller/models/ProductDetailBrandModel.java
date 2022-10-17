package com.shopiroller.models;

import java.io.Serializable;

public class ProductDetailBrandModel implements Serializable {

    private String id;
    private String name;
    private ProductImage icon;
    private boolean isActive;
    private String createDate;
    private String updateDate;

    public ProductDetailBrandModel() {
    }

    public ProductDetailBrandModel(String id, String name, ProductImage icon, boolean isActive, String createDate, String updateDate) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.isActive = isActive;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductImage getIcon() {
        return icon;
    }

    public void setIcon(ProductImage icon) {
        this.icon = icon;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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
}
