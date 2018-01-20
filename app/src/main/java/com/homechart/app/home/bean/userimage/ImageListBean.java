package com.homechart.app.home.bean.userimage;

import java.util.List;

/**
 * Created by gumenghao on 18/1/20.
 */

public class ImageListBean {

    private List<ImageDataBean> item_list;

    public ImageListBean(List<ImageDataBean> item_list) {
        this.item_list = item_list;
    }

    public List<ImageDataBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ImageDataBean> item_list) {
        this.item_list = item_list;
    }

    @Override
    public String toString() {
        return "ImageListBean{" +
                "item_list=" + item_list +
                '}';
    }
}
