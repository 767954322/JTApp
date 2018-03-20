package com.homechart.app.home.bean.searchablums;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/3/20.
 */

public class SearchUserInfoBean implements Serializable {

    private String nickname;
    private String user_id;
    private String profession;
    private SearchUserInfoAvatarBean avatar;

    public SearchUserInfoBean(String nickname, String user_id, String profession, SearchUserInfoAvatarBean avatar) {
        this.nickname = nickname;
        this.user_id = user_id;
        this.profession = profession;
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public SearchUserInfoAvatarBean getAvatar() {
        return avatar;
    }

    public void setAvatar(SearchUserInfoAvatarBean avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "SearchUserInfoBean{" +
                "nickname='" + nickname + '\'' +
                ", user_id='" + user_id + '\'' +
                ", profession='" + profession + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
