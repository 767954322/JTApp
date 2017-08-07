package com.homechart.app.home.bean.huodonglist;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/8/7.
 */

public class HDItemImageBean implements Serializable {

    private float img1_ratio;
    private String img0;
    private String img1;

    public HDItemImageBean(float img1_ratio, String img0, String img1) {
        this.img1_ratio = img1_ratio;
        this.img0 = img0;
        this.img1 = img1;
    }

    public float getImg1_ratio() {
        return img1_ratio;
    }

    public void setImg1_ratio(float img1_ratio) {
        this.img1_ratio = img1_ratio;
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

    @Override
    public String toString() {
        return "HDItemImageBean{" +
                "img1_ratio=" + img1_ratio +
                ", img0='" + img0 + '\'' +
                ", img1='" + img1 + '\'' +
                '}';
    }
}
