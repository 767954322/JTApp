package com.homechart.app.home.bean.message;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/12.
 */

public class ItemMessageBean implements Serializable{

    private String notice_id;
    private String type;
    private String content;
    private String object_id;
    private String add_time;
    private ImageBean image;

    public ItemMessageBean(String notice_id, String type, String content, String object_id, String add_time, ImageBean image) {
        this.notice_id = notice_id;
        this.type = type;
        this.content = content;
        this.object_id = object_id;
        this.add_time = add_time;
        this.image = image;
    }

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public ImageBean getImage() {
        return image;
    }

    public void setImage(ImageBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ItemMessageBean{" +
                "notice_id='" + notice_id + '\'' +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", object_id='" + object_id + '\'' +
                ", add_time='" + add_time + '\'' +
                ", image=" + image +
                '}';
    }
}
