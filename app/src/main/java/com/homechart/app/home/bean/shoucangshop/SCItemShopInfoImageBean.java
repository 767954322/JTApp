package com.homechart.app.home.bean.shoucangshop;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/9/12.
 */

public class SCItemShopInfoImageBean implements Serializable {

    private String img0;

    public SCItemShopInfoImageBean(String img0) {
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
        return "SCItemShopInfoImageBean{" +
                "img0='" + img0 + '\'' +
                '}';
    }
}
