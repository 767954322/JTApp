package com.homechart.app.home.bean.shoucangshop;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/9/12.
 */

public class SCShopBean implements Serializable {

    private List<SCItemShopBean> item_list;


    public SCShopBean(List<SCItemShopBean> item_list) {
        this.item_list = item_list;
    }

    public List<SCItemShopBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<SCItemShopBean> item_list) {
        this.item_list = item_list;
    }

    @Override
    public String toString() {
        return "SCShopBean{" +
                "item_list=" + item_list +
                '}';
    }
}
