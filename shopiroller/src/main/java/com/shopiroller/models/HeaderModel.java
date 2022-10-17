package com.shopiroller.models;

import java.io.Serializable;
import java.util.List;

public class HeaderModel implements Serializable {
    public List<ShowcaseResponseModel> showcaseList;
    public List<CategoryResponseModel> categoryList;
    public List<SliderDataModel> sliderList;
    public CategoriesMobileSettings categoriesMobileSettings;

    @Override
    public String toString() {
        return "HeaderModel{" +
                "showcaseList=" + showcaseList +
                ", categoryList=" + categoryList +
                ", sliderList=" + sliderList +
                '}';
    }
}
