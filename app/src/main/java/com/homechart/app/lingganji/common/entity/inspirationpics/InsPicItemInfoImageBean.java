package com.homechart.app.lingganji.common.entity.inspirationpics;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/4.
 */

public class InsPicItemInfoImageBean implements Serializable {

    private String img0;
    private String img1;
    private float ratio;

    public InsPicItemInfoImageBean(String img0, String img1, float ratio) {
        this.img0 = img0;
        this.img1 = img1;
        this.ratio = ratio;
    }

    public String getImg0() {
        return img0;
    }

    public void setImg0(String img0) {
        this.img0 = img0;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "InsPicItemInfoImageBean{" +
                "img0='" + img0 + '\'' +
                ", img1='" + img1 + '\'' +
                ", ratio=" + ratio +
                '}';
    }
}
