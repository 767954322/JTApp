package com.homechart.app.home.bean.pinglun;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/30.
 */

public class ItemInfoBean implements Serializable{

    private String item_id;
    private String user_id;

    public ItemInfoBean(String item_id, String user_id) {
        this.item_id = item_id;
        this.user_id = user_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "ItemInfoBean{" +
                "item_id='" + item_id + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
