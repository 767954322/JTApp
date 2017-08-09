package com.homechart.app.home.bean.cailike;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/29.
 */

public class ItemInfoBean implements Serializable{


    private String item_id;
    private String tag;
    private ItemInfoImageBean image;
    private String is_collected;
    private String collect_num;

    public ItemInfoBean(String item_id, String tag, ItemInfoImageBean image, String is_collected, String collect_num) {
        this.item_id = item_id;
        this.tag = tag;
        this.image = image;
        this.is_collected = is_collected;
        this.collect_num = collect_num;
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

    public String getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(String is_collected) {
        this.is_collected = is_collected;
    }

    public String getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(String collect_num) {
        this.collect_num = collect_num;
    }

    @Override
    public String toString() {
        return "ItemInfoBean{" +
                "item_id='" + item_id + '\'' +
                ", tag='" + tag + '\'' +
                ", image=" + image +
                ", is_collected='" + is_collected + '\'' +
                ", collect_num='" + collect_num + '\'' +
                '}';
    }
}
