package com.homechart.app.home.bean.searchshops;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/9/15.
 */

public class SearchShopsBean implements Serializable{

    private  List<SearchShopItemBean> item_list;

    public SearchShopsBean(List<SearchShopItemBean> item_list) {
        this.item_list = item_list;
    }

    public List<SearchShopItemBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<SearchShopItemBean> item_list) {
        this.item_list = item_list;
    }

    @Override
    public String toString() {
        return "SearchShopsBean{" +
                "item_list=" + item_list +
                '}';
    }
}
