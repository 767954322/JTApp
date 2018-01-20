package com.homechart.app.home.bean.userimage;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/20.
 */

public class ImageItemImageBean implements Serializable {

    private float ratio;
    private String image_id;
    private String img0;
    private String img1;

    public ImageItemImageBean(float ratio, String image_id, String img0, String img1) {
        this.ratio = ratio;
        this.image_id = image_id;
        this.img0 = img0;
        this.img1 = img1;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
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
        return "ImageItemImageBean{" +
                "ratio=" + ratio +
                ", image_id='" + image_id + '\'' +
                ", img0='" + img0 + '\'' +
                ", img1='" + img1 + '\'' +
                '}';
    }
}
