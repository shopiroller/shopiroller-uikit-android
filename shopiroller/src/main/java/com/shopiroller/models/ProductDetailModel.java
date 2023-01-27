package com.shopiroller.models;

import com.google.gson.annotations.SerializedName;
import com.shopiroller.util.ECommerceUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailModel extends ProductListModel implements Serializable {

    @SerializedName("description")
    public String description;
    @SerializedName("images")
    public List<ProductImage> images = new ArrayList<>();
    @SerializedName("maxQuantityPerOrder")
    public int maxQuantityPerOrder;
    @SerializedName("code")
    public String code;
    @SerializedName("useFixPrice")
    public boolean useFixPrice;
    @SerializedName("brand")
    public ProductDetailBrandModel brand;
    @SerializedName("variants")
    public List<ProductDetailModel> variants = new ArrayList<>();
    @SerializedName("variationGroups")
    public List<VariationGroupsModel> variationGroups = new ArrayList<>();
    @SerializedName("variantData")
    public List<VariantDataModel> variantData = new ArrayList<>();
    @SerializedName("videos")
    public List<String> videos = new ArrayList<>();

    public String getPriceString() {
        if (campaignPrice != 0) {
            return ECommerceUtil.getFormattedPrice(campaignPrice, currency);
        } else
            return ECommerceUtil.getFormattedPrice(price, currency);
    }

}
