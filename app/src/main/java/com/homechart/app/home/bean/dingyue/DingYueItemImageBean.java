package com.homechart.app.home.bean.dingyue;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/8.
 */

public class DingYueItemImageBean implements Serializable{

    private String image_id;
    private String img0;

    public DingYueItemImageBean(String image_id, String img0) {
        this.image_id = image_id;
        this.img0 = img0;
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

    @Override
    public String toString() {
        return "DingYueItemImageBean{" +
                "image_id='" + image_id + '\'' +
                ", img0='" + img0 + '\'' +
                '}';
    }
}
