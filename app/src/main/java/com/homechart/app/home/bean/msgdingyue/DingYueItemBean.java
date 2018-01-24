package com.homechart.app.home.bean.msgdingyue;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/8.
 */

public class DingYueItemBean implements Serializable {

    private String notice_id;
    private String content;
    private String object_type;
    private String object_id;
    private String add_time;
    private String notice_class;
    private DingYueItemImgBean image;
    private DingYueItemUserInfoBean user_info;


    public DingYueItemBean(String notice_id, String content, String object_type, String object_id, String add_time, String notice_class, DingYueItemImgBean image, DingYueItemUserInfoBean user_info) {
        this.notice_id = notice_id;
        this.content = content;
        this.object_type = object_type;
        this.object_id = object_id;
        this.add_time = add_time;
        this.notice_class = notice_class;
        this.image = image;
        this.user_info = user_info;
    }

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
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

    public String getNotice_class() {
        return notice_class;
    }

    public void setNotice_class(String notice_class) {
        this.notice_class = notice_class;
    }

    public DingYueItemImgBean getImage() {
        return image;
    }

    public void setImage(DingYueItemImgBean image) {
        this.image = image;
    }

    public DingYueItemUserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(DingYueItemUserInfoBean user_info) {
        this.user_info = user_info;
    }

    @Override
    public String toString() {
        return "DingYueItemBean{" +
                "notice_id='" + notice_id + '\'' +
                ", content='" + content + '\'' +
                ", object_type='" + object_type + '\'' +
                ", object_id='" + object_id + '\'' +
                ", add_time='" + add_time + '\'' +
                ", notice_class='" + notice_class + '\'' +
                ", image=" + image +
                ", user_info=" + user_info +
                '}';
    }
}
