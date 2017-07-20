package com.homechart.app.home.bean.imagedetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/27.
 */

public class UserInfoBean implements Serializable{

    private String user_id;
    private String nickname;
    private String profession;
    private String relation;
    private String slogan;
    private AvatarBean avatar;


    public UserInfoBean(String user_id, String nickname, String profession, String relation, String slogan, AvatarBean avatar) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.profession = profession;
        this.relation = relation;
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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public AvatarBean getAvatar() {
        return avatar;
    }

    public void setAvatar(AvatarBean avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profession='" + profession + '\'' +
                ", relation='" + relation + '\'' +
                ", slogan='" + slogan + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
