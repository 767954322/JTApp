package com.homechart.app.home.bean.msgguanzhu;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/8/17.
 */

public class MsgNoticeBean implements Serializable {


    private String notice_id;
    private String content;
    private String type;
    private String object_id;
    private String add_time;
    private MsgUserInfoBean user_info;
    private MsgImageBean image;

    public MsgNoticeBean(String notice_id, String content, String type, String object_id, String add_time, MsgUserInfoBean user_info, MsgImageBean image) {
        this.notice_id = notice_id;
        this.content = content;
        this.type = type;
        this.object_id = object_id;
        this.add_time = add_time;
        this.user_info = user_info;
        this.image = image;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public MsgUserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(MsgUserInfoBean user_info) {
        this.user_info = user_info;
    }

    public MsgImageBean getImage() {
        return image;
    }

    public void setImage(MsgImageBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "MsgNoticeBean{" +
                "notice_id='" + notice_id + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", object_id='" + object_id + '\'' +
                ", add_time='" + add_time + '\'' +
                ", user_info=" + user_info +
                ", image=" + image +
                '}';
    }
}
