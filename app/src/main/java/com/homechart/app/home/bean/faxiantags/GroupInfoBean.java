package com.homechart.app.home.bean.faxiantags;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/3/6.
 */

public class GroupInfoBean implements Serializable {

    private List<TagListItemBean> tag_list;
    private String group_name;

    public GroupInfoBean(List<TagListItemBean> tag_list, String group_name) {
        this.tag_list = tag_list;
        this.group_name = group_name;
    }

    public List<TagListItemBean> getTag_list() {
        return tag_list;
    }

    public void setTag_list(List<TagListItemBean> tag_list) {
        this.tag_list = tag_list;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    @Override
    public String toString() {
        return "GroupInfoBean{" +
                "tag_list=" + tag_list +
                ", group_name='" + group_name + '\'' +
                '}';
    }
}
