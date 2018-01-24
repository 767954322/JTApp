package com.homechart.app.lingganji.common.entity.inspirationlist;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/24.
 */

public class InspirationUserInfo implements Serializable {


    private String user_id;
    private String nickname;
    private String slogan;
    private InspirationUserAvatarInfo avatar;


    public InspirationUserInfo(String user_id, String nickname, String slogan, InspirationUserAvatarInfo avatar) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.slogan = slogan;
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

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public InspirationUserAvatarInfo getAvatar() {
        return avatar;
    }

    public void setAvatar(InspirationUserAvatarInfo avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "InspirationUserInfo{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", slogan='" + slogan + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
