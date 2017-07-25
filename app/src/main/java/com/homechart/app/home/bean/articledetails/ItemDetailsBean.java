package com.homechart.app.home.bean.articledetails;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/25.
 */

public class ItemDetailsBean implements Serializable{


    private ItemInfoBean item_info;

    public ItemDetailsBean(ItemInfoBean item_info) {
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
        return "ItemDetailsBean{" +
                "item_info=" + item_info +
                '}';
    }
}
