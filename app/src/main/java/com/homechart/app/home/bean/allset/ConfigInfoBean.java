package com.homechart.app.home.bean.allset;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/25.
 */

public class ConfigInfoBean implements Serializable {

    private String is_enable_item_similar;
    private String is_subscribed_tag;

    public ConfigInfoBean(String is_enable_item_similar, String is_subscribed_tag) {
        this.is_enable_item_similar = is_enable_item_similar;
        this.is_subscribed_tag = is_subscribed_tag;
    }

    public String getIs_enable_item_similar() {
        return is_enable_item_similar;
    }

    public void setIs_enable_item_similar(String is_enable_item_similar) {
        this.is_enable_item_similar = is_enable_item_similar;
    }

    public String getIs_subscribed_tag() {
        return is_subscribed_tag;
    }

    public void setIs_subscribed_tag(String is_subscribed_tag) {
        this.is_subscribed_tag = is_subscribed_tag;
    }

    @Override
    public String toString() {
        return "ConfigInfoBean{" +
                "is_enable_item_similar='" + is_enable_item_similar + '\'' +
                ", is_subscribed_tag='" + is_subscribed_tag + '\'' +
                '}';
    }
}
