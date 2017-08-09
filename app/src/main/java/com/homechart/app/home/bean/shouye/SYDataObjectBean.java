package com.homechart.app.home.bean.shouye;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/14.
 */

public class SYDataObjectBean implements Serializable{

    private String object_id;
    private String type;
    private String tag;
    private String title;
    private SYDataObjectImgBean image;

    private String like_num;
    private String collect_num;
    private String is_collected;

    public SYDataObjectBean(String object_id, String type, String tag, String title, SYDataObjectImgBean image, String like_num, String collect_num, String is_collected) {
        this.object_id = object_id;
        this.type = type;
        this.tag = tag;
        this.title = title;
        this.image = image;
        this.like_num = like_num;
        this.collect_num = collect_num;
        this.is_collected = is_collected;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SYDataObjectImgBean getImage() {
        return image;
    }

    public void setImage(SYDataObjectImgBean image) {
        this.image = image;
    }

    public String getLike_num() {
        return like_num;
    }

    public void setLike_num(String like_num) {
        this.like_num = like_num;
    }

    public String getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(String collect_num) {
        this.collect_num = collect_num;
    }

    public String getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(String is_collected) {
        this.is_collected = is_collected;
    }

    @Override
    public String toString() {
        return "SYDataObjectBean{" +
                "object_id='" + object_id + '\'' +
                ", type='" + type + '\'' +
                ", tag='" + tag + '\'' +
                ", title='" + title + '\'' +
                ", image=" + image +
                ", like_num='" + like_num + '\'' +
                ", collect_num='" + collect_num + '\'' +
                ", is_collected='" + is_collected + '\'' +
                '}';
    }
}
