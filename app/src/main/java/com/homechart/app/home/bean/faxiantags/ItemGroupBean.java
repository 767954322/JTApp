package com.homechart.app.home.bean.faxiantags;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/3/6.
 */

public class ItemGroupBean implements Serializable {

    private GroupInfoBean group_info;

    public ItemGroupBean(GroupInfoBean group_info) {
        this.group_info = group_info;
    }

    public GroupInfoBean getGroup_info() {
        return group_info;
    }

    public void setGroup_info(GroupInfoBean group_info) {
        this.group_info = group_info;
    }

    @Override
    public String toString() {
        return "ItemGroupBean{" +
                "group_info=" + group_info +
                '}';
    }
}
