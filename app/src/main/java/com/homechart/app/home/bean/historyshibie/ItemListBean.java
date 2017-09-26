package com.homechart.app.home.bean.historyshibie;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/9/26.
 */

public class ItemListBean implements Serializable {

    private ItemInfoBean item_info;

    public ItemListBean(ItemInfoBean item_info) {
        this.item_info = item_info;
    }

    public ItemInfoBean getItem_info() {
        return item_info;
    }

    public void setItem_info(ItemInfoBean item_info) {
        this.item_info = item_info;
    }

    @Override
    public String toString() {
        return "ItemListBean{" +
                "item_info=" + item_info +
                '}';
    }
}
