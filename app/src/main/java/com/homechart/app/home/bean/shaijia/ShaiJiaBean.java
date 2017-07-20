package com.homechart.app.home.bean.shaijia;

import com.homechart.app.home.bean.shoucang.ShouCangItemBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/8.
 */

public class ShaiJiaBean implements Serializable{


    private List<ShaiJiaItemBean> item_list;


    public ShaiJiaBean(List<ShaiJiaItemBean> item_list) {
        this.item_list = item_list;
    }

    public List<ShaiJiaItemBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ShaiJiaItemBean> item_list) {
        this.item_list = item_list;
    }

    @Override
    public String toString() {
        return "ShaiJiaBean{" +
                "item_list=" + item_list +
                '}';
    }
}
