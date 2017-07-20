package com.homechart.app.home.bean.shoucang;

import com.homechart.app.home.bean.city.ItemProvinceBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/8.
 */

public class ShouCangBean implements Serializable{


    private List<ShouCangItemBean> item_list;

    public ShouCangBean(List<ShouCangItemBean> item_list) {
        this.item_list = item_list;
    }

    public List<ShouCangItemBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ShouCangItemBean> item_list) {
        this.item_list = item_list;
    }

    @Override
    public String toString() {
        return "ShouCangBean{" +
                "item_list=" + item_list +
                '}';
    }
}
