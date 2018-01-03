package com.homechart.app.lingganji.common.entity.inspirationlist;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/3.
 */

public class InspirationInfoImageBean implements Serializable {

    private String img0;

    public InspirationInfoImageBean(String img0) {
        this.img0 = img0;
    }

    public InspirationInfoImageBean() {
    }

    public String getImg0() {
        return img0;
    }

    public void setImg0(String img0) {
        this.img0 = img0;
    }

    @Override
    public String toString() {
        return "InspirationInfoImageBean{" +
                "img0='" + img0 + '\'' +
                '}';
    }
}
