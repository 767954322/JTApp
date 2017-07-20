package com.homechart.app.home.bean.putheader;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/9.
 */

public class HeaderBean implements Serializable{


    private String avatar_id;
    private HeaderAvatar avatar;


    public HeaderBean(String avatar_id, HeaderAvatar avatar) {
        this.avatar_id = avatar_id;
        this.avatar = avatar;
    }

    public String getAvatar_id() {
        return avatar_id;
    }

    public void setAvatar_id(String avatar_id) {
        this.avatar_id = avatar_id;
    }

    public HeaderAvatar getAvatar() {
        return avatar;
    }

    public void setAvatar(HeaderAvatar avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "HeaderBean{" +
                "avatar_id='" + avatar_id + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
