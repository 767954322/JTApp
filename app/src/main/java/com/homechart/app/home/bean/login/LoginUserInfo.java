package com.homechart.app.home.bean.login;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/5.
 */

public class LoginUserInfo implements Serializable{

    private String user_id;
    private String nickname;
    private String slogan;
    private String email;
    private String mobile;
    private String profession;
    private LoginAvatar avatar;

    public LoginUserInfo(String user_id, String nickname, String slogan, String email, String mobile, String profession, LoginAvatar avatar) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.slogan = slogan;
        this.email = email;
        this.mobile = mobile;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public LoginAvatar getAvatar() {
        return avatar;
    }

    public void setAvatar(LoginAvatar avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "LoginUserInfo{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", slogan='" + slogan + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", profession='" + profession + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
