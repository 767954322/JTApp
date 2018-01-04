package com.homechart.app.lingganji.common.entity.inspirationpics;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/1/4.
 */

public class InsPicsBean implements Serializable {

    private List<InsPicItemBean> item_list ;

    public InsPicsBean(List<InsPicItemBean> item_list) {
        this.item_list = item_list;
    }

    public List<InsPicItemBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<InsPicItemBean> item_list) {
        this.item_list = item_list;
    }

    @Override
    public String toString() {
        return "InsPicsBean{" +
                "item_list=" + item_list +
                '}';
    }
}
