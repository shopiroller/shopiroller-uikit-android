package com.shopiroller.models;

import java.io.Serializable;
import java.util.List;

public class ShowcaseResponseModel implements Serializable {

    private ShowcaseModel showcase;
    private String showcaseType;
    private List<ProductDetailModel> products;

    public ShowcaseResponseModel() { }

    public ShowcaseResponseModel(ShowcaseModel showcase, String showcaseType, List<ProductDetailModel> products) {
        this.showcase = showcase;
        this.showcaseType = showcaseType;
        this.products = products;
    }

    public ShowcaseModel getShowcase() {
        return showcase;
    }

    public void setShowcase(ShowcaseModel showcase) {
        this.showcase = showcase;
    }

    public String getShowcaseType() {
        return showcaseType;
    }

    public void setShowcaseType(String showcaseType) {
        this.showcaseType = showcaseType;
    }

    public List<ProductDetailModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDetailModel> products) {
        this.products = products;
    }

}
