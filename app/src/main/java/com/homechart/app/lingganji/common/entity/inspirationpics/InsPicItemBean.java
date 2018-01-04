package com.homechart.app.lingganji.common.entity.inspirationpics;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/4.
 */

public class InsPicItemBean implements Serializable {

    private InsPicItemInfoBean item_info ;

    public InsPicItemBean(InsPicItemInfoBean item_info) {
        this.item_info = item_info;
    }

    public InsPicItemInfoBean getItem_info() {
        return item_info;
    }

    public void setItem_info(InsPicItemInfoBean item_info) {
        this.item_info = item_info;
    }

    @Override
    public String toString() {
        return "InsPicItemBean{" +
                "item_info=" + item_info +
                '}';
    }
}
