package com.homechart.app.home.bean.imagedetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/27.
 */

public class ItemInfoBean implements Serializable{

    private String item_id;
    private String description;
    private String tag;
    private String add_time;
    private String is_liked;
    private String activity_id;
    private String is_collected;
    private ItemInfoImageBean image;

    public ItemInfoBean(String item_id, String description, String tag, String add_time, String is_liked, String activity_id, String is_collected, ItemInfoImageBean image) {
        this.item_id = item_id;
        this.description = description;
        this.tag = tag;
        this.add_time = add_time;
        this.is_liked = is_liked;
        this.activity_id = activity_id;
        this.is_collected = is_collected;
        this.image = image;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(String is_liked) {
        this.is_liked = is_liked;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(String is_collected) {
        this.is_collected = is_collected;
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
                ", description='" + description + '\'' +
                ", tag='" + tag + '\'' +
                ", add_time='" + add_time + '\'' +
                ", is_liked='" + is_liked + '\'' +
                ", activity_id='" + activity_id + '\'' +
                ", is_collected='" + is_collected + '\'' +
                ", image=" + image +
                '}';
    }
}
