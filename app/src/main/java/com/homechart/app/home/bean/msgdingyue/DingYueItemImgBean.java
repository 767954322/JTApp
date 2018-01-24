package com.homechart.app.home.bean.msgdingyue;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/8.
 */

public class DingYueItemImgBean implements Serializable{

    private String img0;
    private String img1;


    public DingYueItemImgBean(String img0, String img1) {
        this.img0 = img0;
        this.img1 = img1;
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
        return "DingYueItemImgBean{" +
                "img0='" + img0 + '\'' +
                ", img1='" + img1 + '\'' +
                '}';
    }
}
