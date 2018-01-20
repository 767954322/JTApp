package com.homechart.app.home.bean.userimage;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/20.
 */

public class ImageItemInfoBean implements Serializable {

    private String item_id;
    private String description;
    private String tag;
    private String comment_num;
    private String add_time;
    private ImageItemImageBean image;

    public ImageItemInfoBean(String item_id, String description, String tag, String comment_num, String add_time, ImageItemImageBean image) {
        this.item_id = item_id;
        this.description = description;
        this.tag = tag;
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

    public ImageItemImageBean getImage() {
        return image;
    }

    public void setImage(ImageItemImageBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ImageItemInfoBean{" +
                "item_id='" + item_id + '\'' +
                ", description='" + description + '\'' +
                ", tag='" + tag + '\'' +
                ", comment_num='" + comment_num + '\'' +
                ", add_time='" + add_time + '\'' +
                ", image=" + image +
                '}';
    }
}
