package com.homechart.app.home.bean.allset;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/25.
 */

public class ConfigInfoBean implements Serializable {

    private String is_enable_item_similar;

    public ConfigInfoBean(String is_enable_item_similar) {
        this.is_enable_item_similar = is_enable_item_similar;
    }


    public String getIs_enable_item_similar() {
        return is_enable_item_similar;
    }

    public void setIs_enable_item_similar(String is_enable_item_similar) {
        this.is_enable_item_similar = is_enable_item_similar;
    }

    @Override
    public String toString() {
        return "ConfigInfoBean{" +
                "is_enable_item_similar='" + is_enable_item_similar + '\'' +
                '}';
    }
}
