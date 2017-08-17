package com.homechart.app.home.bean.msgguanzhu;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/8/17.
 */

public class MsgUserInfoBean implements Serializable {

    private String user_id;
    private String nickname;
    private String avatar;

    public MsgUserInfoBean(String user_id, String nickname, String avatar) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public String getUser_id() {

        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "MsgUserInfoBean{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
