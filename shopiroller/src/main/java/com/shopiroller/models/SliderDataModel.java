package com.shopiroller.models;

import java.io.Serializable;
import java.util.List;

public class SliderDataModel implements Serializable {

    private String name;
    private String id;
    private String createDate;
    private String updateDate;
    private boolean isActive;
    private List<SliderSlidesModel> slides;

    public SliderDataModel(String name, String id, String createDate, String updateDate, boolean isActive, List<SliderSlidesModel> slides) {
        this.name = name;
        this.id = id;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.isActive = isActive;
        this.slides = slides;
    }

    public SliderDataModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<SliderSlidesModel> getSlides() {
        return slides;
    }

    public void setSlides(List<SliderSlidesModel> slides) {
        this.slides = slides;
    }

}
