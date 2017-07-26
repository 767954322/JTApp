package com.homechart.app.home.bean.articledetails;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/25.
 */

public class ItemImageBean implements Serializable{

    private String img0;
    private float ratio;

    public ItemImageBean(String img0, float ratio) {
        this.img0 = img0;
        this.ratio = ratio;
    }

    public String getImg0() {
        return img0;
    }

    public void setImg0(String img0) {
        this.img0 = img0;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "ItemImageBean{" +
                "img0='" + img0 + '\'' +
                ", ratio=" + ratio +
                '}';
    }
}
