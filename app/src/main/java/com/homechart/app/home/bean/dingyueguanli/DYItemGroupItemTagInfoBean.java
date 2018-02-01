package com.homechart.app.home.bean.dingyueguanli;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/2/1.
 */

public class DYItemGroupItemTagInfoBean implements Serializable {

    private String tag_id;
    private String tag_name;
    private String is_subscribed;

    public DYItemGroupItemTagInfoBean(String tag_id, String tag_name, String is_subscribed) {
        this.tag_id = tag_id;
        this.tag_name = tag_name;
        this.is_subscribed = is_subscribed;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getIs_subscribed() {
        return is_subscribed;
    }

    public void setIs_subscribed(String is_subscribed) {
        this.is_subscribed = is_subscribed;
    }

    @Override
    public String toString() {
        return "DYItemGroupItemTagInfoBean{" +
                "tag_id='" + tag_id + '\'' +
                ", tag_name='" + tag_name + '\'' +
                ", is_subscribed='" + is_subscribed + '\'' +
                '}';
    }
}
