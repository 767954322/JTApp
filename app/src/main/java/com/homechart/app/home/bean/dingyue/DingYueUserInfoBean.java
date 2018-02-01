package com.homechart.app.home.bean.dingyue;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/2/1.
 */

public class DingYueUserInfoBean implements Serializable {

    private  String user_id;
    private  String nickname;
    private  String slogan;
    private  String profession;
    private  DingYueUserAvatarBean avatar;

    public DingYueUserInfoBean(String user_id, String nickname, String slogan, String profession, DingYueUserAvatarBean avatar) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.slogan = slogan;
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

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public DingYueUserAvatarBean getAvatar() {
        return avatar;
    }

    public void setAvatar(DingYueUserAvatarBean avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "DingYueUserInfoBean{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", slogan='" + slogan + '\'' +
                ", profession='" + profession + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
