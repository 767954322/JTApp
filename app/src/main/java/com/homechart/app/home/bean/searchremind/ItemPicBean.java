package com.homechart.app.home.bean.searchremind;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/3/19.
 */

public class ItemPicBean implements Serializable {

    private ItemPicInfoBean item_info;


    public ItemPicBean(ItemPicInfoBean item_info) {
        this.item_info = item_info;
    }

    public ItemPicInfoBean getItem_info() {
        return item_info;
    }

    public void setItem_info(ItemPicInfoBean item_info) {
        this.item_info = item_info;
    }

    @Override
    public String toString() {
        return "ItemPicBean{" +
                "item_info=" + item_info +
                '}';
    }
}
