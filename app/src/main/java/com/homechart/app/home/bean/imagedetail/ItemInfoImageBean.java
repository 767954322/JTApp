package com.homechart.app.home.bean.imagedetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/27.
 */

public class ItemInfoImageBean implements Serializable{

    private float ratio;
    private String img0;

    public ItemInfoImageBean(float ratio, String img0) {
        this.ratio = ratio;
        this.img0 = img0;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public String getImg0() {
        return img0;
    }

    public void setImg0(String img0) {
        this.img0 = img0;
    }

    @Override
    public String toString() {
        return "ItemInfoImageBean{" +
                "ratio=" + ratio +
                ", img0='" + img0 + '\'' +
                '}';
    }
}
