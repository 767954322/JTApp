package com.homechart.app.home.bean.historyshibie;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/9/26.
 */

public class ShiBieBean implements Serializable{

    private List<ItemListBean> item_list;

    public ShiBieBean(List<ItemListBean> item_list) {
        this.item_list = item_list;
    }

    public List<ItemListBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ItemListBean> item_list) {
        this.item_list = item_list;
    }

    @Override
    public String toString() {
        return "ShiBieBean{" +
                "item_list=" + item_list +
                '}';
    }
}
