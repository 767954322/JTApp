package com.homechart.app.home.bean.huodong;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/29.
 */

public class ItemInfoBean implements Serializable {

    private String item_id;
    private String tag;
    private String collect_num;
    private String comment_num;
    private String add_time;
    private ItemInfoImageBean image;

    public ItemInfoBean(String item_id, String tag, String collect_num, String comment_num, String add_time, ItemInfoImageBean image) {
        this.item_id = item_id;
        this.tag = tag;
        this.collect_num = collect_num;
        this.comment_num = comment_num;
        this.add_time = add_time;
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

    public String getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(String collect_num) {
        this.collect_num = collect_num;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
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
                ", collect_num='" + collect_num + '\'' +
                ", comment_num='" + comment_num + '\'' +
                ", add_time='" + add_time + '\'' +
                ", image=" + image +
                '}';
    }
}
