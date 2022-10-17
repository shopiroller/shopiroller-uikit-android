package com.shopiroller.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class CategoryResponseModel implements Serializable, Parcelable {

    private String categoryId;
    private String name;
    private String icon;
    private String parentCategoryId;
    private int orderIndex;
    private String createDate;
    private String updateDate;
    private int totalProduct;
    private boolean isActive;
    private List<CategoryResponseModel> subCategories;
    public int totalProducts;

    public CategoryResponseModel() {
    }

    public CategoryResponseModel(String categoryId, String name, String icon, String parentCategoryId, int orderIndex, String createDate, String updateDate, int totalProduct, boolean isActive, List<CategoryResponseModel> subCategories, int totalProducts) {
        this.categoryId = categoryId;
        this.name = name;
        this.icon = icon;
        this.parentCategoryId = parentCategoryId;
        this.orderIndex = orderIndex;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.totalProduct = totalProduct;
        this.isActive = isActive;
        this.subCategories = subCategories;
        this.totalProducts = totalProducts;
    }

    protected CategoryResponseModel(Parcel in) {
        categoryId = in.readString();
        name = in.readString();
        icon = in.readString();
        parentCategoryId = in.readString();
        orderIndex = in.readInt();
        createDate = in.readString();
        updateDate = in.readString();
        totalProduct = in.readInt();
        isActive = in.readByte() != 0;
        subCategories = in.createTypedArrayList(CategoryResponseModel.CREATOR);
        totalProducts = in.readInt();
    }

    public static final Creator<CategoryResponseModel> CREATOR = new Creator<CategoryResponseModel>() {
        @Override
        public CategoryResponseModel createFromParcel(Parcel in) {
            return new CategoryResponseModel(in);
        }

        @Override
        public CategoryResponseModel[] newArray(int size) {
            return new CategoryResponseModel[size];
        }
    };

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
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

    public int getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(int totalProduct) {
        this.totalProduct = totalProduct;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<CategoryResponseModel> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<CategoryResponseModel> subCategories) {
        this.subCategories = subCategories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryId);
        dest.writeString(name);
        dest.writeString(icon);
        dest.writeString(parentCategoryId);
        dest.writeInt(orderIndex);
        dest.writeString(createDate);
        dest.writeString(updateDate);
        dest.writeInt(totalProduct);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeTypedList(subCategories);
        dest.writeInt(totalProducts);
    }
}
