package com.homechart.app.home.bean.msgguanzhu;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/8/17.
 */

public class MsgImageBean implements Serializable{

    private String img0;

    public MsgImageBean(String img0) {
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
        return "MsgImageBean{" +
                "img0='" + img0 + '\'' +
                '}';
    }
}
