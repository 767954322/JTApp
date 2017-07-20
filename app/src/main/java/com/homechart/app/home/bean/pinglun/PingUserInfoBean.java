package com.homechart.app.home.bean.pinglun;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/28.
 */

public class PingUserInfoBean implements Serializable{

    private String user_id;
    private String nickname;
    private String profession;
    private PingUserInfoAvatar avatar;

    public PingUserInfoBean(String user_id, String nickname, String profession, PingUserInfoAvatar avatar) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.profession = profession;
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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public PingUserInfoAvatar getAvatar() {
        return avatar;
    }

    public void setAvatar(PingUserInfoAvatar avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "PingUserInfoBean{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profession='" + profession + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
