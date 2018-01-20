package com.homechart.app.home.bean.userimage;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/20.
 */

public class ImageDataBean implements Serializable {

    private ImageItemInfoBean item_info;


    public ImageDataBean(ImageItemInfoBean item_info) {
        this.item_info = item_info;
    }

    public ImageItemInfoBean getItem_info() {
        return item_info;
    }

    public void setItem_info(ImageItemInfoBean item_info) {
        this.item_info = item_info;
    }

    @Override
    public String toString() {
        return "ImageDataBean{" +
                "item_info=" + item_info +
                '}';
    }
}
