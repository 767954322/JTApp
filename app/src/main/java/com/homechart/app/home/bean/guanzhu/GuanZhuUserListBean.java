package com.homechart.app.home.bean.guanzhu;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/7.
 */

public class GuanZhuUserListBean implements Serializable{

    private String id;
    private String user_id;
    private String nickname;
    private String gender;
    private String profession;
    private GuanZhuAvatarBean avatar;

    public GuanZhuUserListBean(String id, String user_id, String nickname, String gender, String profession, GuanZhuAvatarBean avatar) {
        this.id = id;
        this.user_id = user_id;
        this.nickname = nickname;
        this.gender = gender;
        this.profession = profession;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public GuanZhuAvatarBean getAvatar() {
        return avatar;
    }

    public void setAvatar(GuanZhuAvatarBean avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "GuanZhuUserListBean{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender='" + gender + '\'' +
                ", profession='" + profession + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
