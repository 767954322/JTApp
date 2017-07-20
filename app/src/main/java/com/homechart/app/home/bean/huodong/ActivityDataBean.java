package com.homechart.app.home.bean.huodong;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/29.
 */

public class ActivityDataBean implements Serializable {

    private List<ItemActivityDataBean>  item_list;

    public ActivityDataBean(List<ItemActivityDataBean> item_list) {
        this.item_list = item_list;
    }

    public List<ItemActivityDataBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ItemActivityDataBean> item_list) {
        this.item_list = item_list;
    }

    @Override
    public String toString() {
        return "ActivityDataBean{" +
                "item_list=" + item_list +
                '}';
    }
}
