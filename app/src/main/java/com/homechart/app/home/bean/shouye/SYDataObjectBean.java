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


    public SYDataObjectBean(String object_id, String type, String tag, String title, SYDataObjectImgBean image) {
        this.object_id = object_id;
        this.type = type;
        this.tag = tag;
        this.title = title;
        this.image = image;
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

    @Override
    public String toString() {
        return "SYDataObjectBean{" +
                "object_id='" + object_id + '\'' +
                ", type='" + type + '\'' +
                ", tag='" + tag + '\'' +
                ", title='" + title + '\'' +
                ", image=" + image +
                '}';
    }
}
