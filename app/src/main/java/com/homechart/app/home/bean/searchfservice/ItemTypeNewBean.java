package com.homechart.app.home.bean.searchfservice;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/11/1.
 */

public class ItemTypeNewBean implements Serializable{

    private ItemTypeInfoNewBean category_info;

    public ItemTypeNewBean(ItemTypeInfoNewBean category_info) {
        this.category_info = category_info;
    }

    public ItemTypeInfoNewBean getCategory_info() {
        return category_info;
    }

    public void setCategory_info(ItemTypeInfoNewBean category_info) {
        this.category_info = category_info;
    }

    @Override
    public String toString() {
        return "ItemTypeNewBean{" +
                "category_info=" + category_info +
                '}';
    }
}
