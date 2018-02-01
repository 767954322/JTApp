package com.homechart.app.home.bean.dingyueguanli;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/2/1.
 */

public class DYItemGroupBean implements Serializable {

    private String group_name;
    private List<DYItemGroupItemBean> tag_list;

    public DYItemGroupBean(String group_name, List<DYItemGroupItemBean> tag_list) {
        this.group_name = group_name;
        this.tag_list = tag_list;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public List<DYItemGroupItemBean> getTag_list() {
        return tag_list;
    }

    public void setTag_list(List<DYItemGroupItemBean> tag_list) {
        this.tag_list = tag_list;
    }

    @Override
    public String toString() {
        return "DYItemGroupBean{" +
                "group_name='" + group_name + '\'' +
                ", tag_list=" + tag_list +
                '}';
    }
}
