package com.homechart.app.lingganji.common.entity.inspirationpics;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/4.
 */

public class InsPicItemInfoImageBean implements Serializable {

    private String img0;
    private String img1;
    private float ratio;
    private String img2;

    public InsPicItemInfoImageBean(String img0, String img1, float ratio, String img2) {
        this.img0 = img0;
        this.img1 = img1;
        this.ratio = ratio;
        this.img2 = img2;
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

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    @Override
    public String toString() {
        return "InsPicItemInfoImageBean{" +
                "img0='" + img0 + '\'' +
                ", img1='" + img1 + '\'' +
                ", ratio=" + ratio +
                ", img2='" + img2 + '\'' +
                '}';
    }
}
