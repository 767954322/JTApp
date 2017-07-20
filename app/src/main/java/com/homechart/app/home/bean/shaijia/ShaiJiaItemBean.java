package com.homechart.app.home.bean.shaijia;

import com.homechart.app.home.bean.shoucang.ShouCangItemInfo;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/8.
 */

public class ShaiJiaItemBean implements Serializable{

    private  ShaiJiaItemInfo item_info ;

    public ShaiJiaItemBean(ShaiJiaItemInfo item_info) {
        this.item_info = item_info;
    }

    public ShaiJiaItemInfo getItem_info() {
        return item_info;
    }

    public void setItem_info(ShaiJiaItemInfo item_info) {
        this.item_info = item_info;
    }

    @Override
    public String toString() {
        return "ShaiJiaItemBean{" +
                "item_info=" + item_info +
                '}';
    }
}
