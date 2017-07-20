package com.homechart.app.home.bean.cailike;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/29.
 */

public class UserInfoBean implements Serializable{

    private String user_id;
    private String nickname;
    private UserInfoAvatarBean avatar;
    private String profession;

    public UserInfoBean(String user_id, String nickname, UserInfoAvatarBean avatar, String profession) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.avatar = avatar;
        this.profession = profession;
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

    public UserInfoAvatarBean getAvatar() {
        return avatar;
    }

    public void setAvatar(UserInfoAvatarBean avatar) {
        this.avatar = avatar;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar=" + avatar +
                ", profession='" + profession + '\'' +
                '}';
    }
}
