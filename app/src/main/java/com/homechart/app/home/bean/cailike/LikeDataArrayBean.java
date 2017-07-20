package com.homechart.app.home.bean.cailike;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/29.
 */

public class LikeDataArrayBean implements Serializable{

    private List<ImageLikeItemBean>  item_list;


    public LikeDataArrayBean(List<ImageLikeItemBean> item_list) {
        this.item_list = item_list;
    }

    public List<ImageLikeItemBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ImageLikeItemBean> item_list) {
        this.item_list = item_list;
    }

    @Override
    public String toString() {
        return "LikeDataArrayBean{" +
                "item_list=" + item_list +
                '}';
    }
}
