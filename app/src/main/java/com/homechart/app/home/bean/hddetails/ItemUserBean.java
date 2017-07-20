package com.homechart.app.home.bean.hddetails;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/29.
 */

public class ItemUserBean implements Serializable{

    private String user_id;
    private String nickname;
    private String profession;
    private ItemUserAvatarBean avatar;

    public ItemUserBean(String user_id, String nickname, String profession, ItemUserAvatarBean avatar) {
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

    public ItemUserAvatarBean getAvatar() {
        return avatar;
    }

    public void setAvatar(ItemUserAvatarBean avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "ItemUserBean{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profession='" + profession + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
