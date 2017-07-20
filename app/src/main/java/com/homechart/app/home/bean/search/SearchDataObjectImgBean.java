package com.homechart.app.home.bean.search;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/15.
 */

public class SearchDataObjectImgBean implements Serializable{

    private float ratio;
    private String img0;
    private String img1;


    public SearchDataObjectImgBean(float ratio, String img0, String img1) {
        this.ratio = ratio;
        this.img0 = img0;
        this.img1 = img1;
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

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }


    @Override
    public String toString() {
        return "SearchDataObjectImgBean{" +
                "ratio=" + ratio +
                ", img0='" + img0 + '\'' +
                ", img1='" + img1 + '\'' +
                '}';
    }
}
