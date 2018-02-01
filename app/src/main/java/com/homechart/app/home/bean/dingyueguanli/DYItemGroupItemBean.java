package com.homechart.app.home.bean.dingyueguanli;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/2/1.
 */

public class DYItemGroupItemBean implements Serializable {


    private DYItemGroupItemTagInfoBean tag_info;

    public DYItemGroupItemBean(DYItemGroupItemTagInfoBean tag_info) {
        this.tag_info = tag_info;
    }

    public DYItemGroupItemTagInfoBean getTag_info() {
        return tag_info;
    }

    public void setTag_info(DYItemGroupItemTagInfoBean tag_info) {
        this.tag_info = tag_info;
    }

    @Override
    public String toString() {
        return "DYItemGroupItemBean{" +
                "tag_info=" + tag_info +
                '}';
    }
}
