package com.homechart.app.home.bean.searchshops;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/9/15.
 */

public class SearchShopItemBean implements Serializable {

    private SearchShopItemInfoBean item_info;

    public SearchShopItemBean(SearchShopItemInfoBean item_info) {
        this.item_info = item_info;
    }

    public SearchShopItemInfoBean getItem_info() {
        return item_info;
    }

    public void setItem_info(SearchShopItemInfoBean item_info) {
        this.item_info = item_info;
    }

    @Override
    public String toString() {
        return "SearchShopItemBean{" +
                "item_info=" + item_info +
                '}';
    }
}
