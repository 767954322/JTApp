package com.homechart.app.home.bean.message;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/12.
 */

public class ImageBean implements Serializable{


    private String img0;

    public ImageBean(String img0) {
        this.img0 = img0;
    }

    public String getImg0() {
        return img0;
    }

    public void setImg0(String img0) {
        this.img0 = img0;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "img0='" + img0 + '\'' +
                '}';
    }
}
