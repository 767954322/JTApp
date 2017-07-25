package com.homechart.app.home.bean.articledetails;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/25.
 */

public class ItemImageBean implements Serializable{

    private String img0;

    public ItemImageBean(String img0) {
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
        return "ItemImageBean{" +
                "img0='" + img0 + '\'' +
                '}';
    }
}
