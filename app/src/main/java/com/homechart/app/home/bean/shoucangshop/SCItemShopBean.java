package com.homechart.app.home.bean.shoucangshop;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/9/12.
 */

public class SCItemShopBean implements Serializable {

    private SCItemShopInfoBean item_info;

    public SCItemShopBean(SCItemShopInfoBean item_info) {
        this.item_info = item_info;
    }

    public SCItemShopInfoBean getItem_info() {
        return item_info;
    }

    public void setItem_info(SCItemShopInfoBean item_info) {
        this.item_info = item_info;
    }

    @Override
    public String toString() {
        return "SCItemShopBean{" +
                "item_info=" + item_info +
                '}';
    }
}
