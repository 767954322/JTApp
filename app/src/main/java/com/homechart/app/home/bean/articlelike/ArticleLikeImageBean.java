package com.homechart.app.home.bean.articlelike;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/26.
 */

public class ArticleLikeImageBean implements Serializable {

    private String img0;

    public ArticleLikeImageBean(String img0) {
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
        return "ArticleLikeImageBean{" +
                "img0='" + img0 + '\'' +
                '}';
    }
}
