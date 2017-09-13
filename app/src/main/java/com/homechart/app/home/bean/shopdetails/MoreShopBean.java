package com.homechart.app.home.bean.shopdetails;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/9/13.
 */

public class MoreShopBean implements Serializable {

    private List<ShopDetailsItemInfoBean> item_list;

    public MoreShopBean(List<ShopDetailsItemInfoBean> item_list) {
        this.item_list = item_list;
    }

    public List<ShopDetailsItemInfoBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ShopDetailsItemInfoBean> item_list) {
        this.item_list = item_list;
    }

    @Override
    public String toString() {
        return "MoreShopBean{" +
                "item_list=" + item_list +
                '}';
    }
}
