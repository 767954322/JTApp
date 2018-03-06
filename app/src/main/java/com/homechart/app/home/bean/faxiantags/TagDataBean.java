package com.homechart.app.home.bean.faxiantags;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/3/6.
 */

public class TagDataBean implements Serializable {

    private List<ItemGroupBean> group_list;

    public TagDataBean(List<ItemGroupBean> group_list) {
        this.group_list = group_list;
    }

    public List<ItemGroupBean> getGroup_list() {
        return group_list;
    }

    public void setGroup_list(List<ItemGroupBean> group_list) {
        this.group_list = group_list;
    }

    @Override
    public String toString() {
        return "TagDataBean{" +
                "group_list=" + group_list +
                '}';
    }
}
