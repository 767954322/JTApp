package com.homechart.app.home.bean.shoucang;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/8.
 */

public class ShouCangItemImageBean implements Serializable {

    private String img0;

    public ShouCangItemImageBean(String img0) {
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
        return "ShouCangItemImageBean{" +
                "img0='" + img0 + '\'' +
                '}';
    }
}
