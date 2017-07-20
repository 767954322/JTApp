package com.homechart.app.home.bean.cailike;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/29.
 */

public class ItemInfoBean implements Serializable{


    private String item_id;
    private String tag;
    private ItemInfoImageBean image;

    public ItemInfoBean(String item_id, String tag, ItemInfoImageBean image) {
        this.item_id = item_id;
        this.tag = tag;
        this.image = image;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ItemInfoImageBean getImage() {
        return image;
    }

    public void setImage(ItemInfoImageBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ItemInfoBean{" +
                "item_id='" + item_id + '\'' +
                ", tag='" + tag + '\'' +
                ", image=" + image +
                '}';
    }
}
