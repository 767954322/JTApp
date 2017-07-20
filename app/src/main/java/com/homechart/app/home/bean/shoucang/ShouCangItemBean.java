package com.homechart.app.home.bean.shoucang;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/8.
 */

public class ShouCangItemBean implements Serializable{

    private  ShouCangItemInfo item_info ;

    public ShouCangItemBean(ShouCangItemInfo item_info) {
        this.item_info = item_info;
    }

    public ShouCangItemInfo getItem_info() {
        return item_info;
    }

    public void setItem_info(ShouCangItemInfo item_info) {
        this.item_info = item_info;
    }

    @Override
    public String toString() {
        return "ShouCangItemBean{" +
                "item_info=" + item_info +
                '}';
    }
}
