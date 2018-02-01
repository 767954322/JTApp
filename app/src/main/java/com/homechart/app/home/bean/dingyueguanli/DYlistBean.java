package com.homechart.app.home.bean.dingyueguanli;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/2/1.
 */

public class DYlistBean implements Serializable {

    private List<DYItemBean> group_list;


    public DYlistBean(List<DYItemBean> group_list) {
        this.group_list = group_list;
    }

    public List<DYItemBean> getGroup_list() {
        return group_list;
    }

    public void setGroup_list(List<DYItemBean> group_list) {
        this.group_list = group_list;
    }

    @Override
    public String toString() {
        return "DYlistBean{" +
                "group_list=" + group_list +
                '}';
    }
}
