package com.homechart.app.home.bean.shopdetails;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/9/13.
 */

public class ShopDetailsBean implements Serializable{

    private ShopDetailsItemInfoBean item_info;


    public ShopDetailsBean(ShopDetailsItemInfoBean item_info) {
        this.item_info = item_info;
    }

    public ShopDetailsItemInfoBean getItem_info() {
        return item_info;
    }

    public void setItem_info(ShopDetailsItemInfoBean item_info) {
        this.item_info = item_info;
    }

    @Override
    public String toString() {
        return "ShopDetailsBean{" +
                "item_info=" + item_info +
                '}';
    }
}
