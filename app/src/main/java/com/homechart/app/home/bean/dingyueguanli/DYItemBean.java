package com.homechart.app.home.bean.dingyueguanli;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/2/1.
 */

public class DYItemBean implements Serializable {

    private DYItemGroupBean group_info;

    public DYItemBean(DYItemGroupBean group_info) {
        this.group_info = group_info;
    }

    public DYItemGroupBean getGroup_info() {
        return group_info;
    }

    public void setGroup_info(DYItemGroupBean group_info) {
        this.group_info = group_info;
    }

    @Override
    public String toString() {
        return "DYItemBean{" +
                "group_info=" + group_info +
                '}';
    }
}
