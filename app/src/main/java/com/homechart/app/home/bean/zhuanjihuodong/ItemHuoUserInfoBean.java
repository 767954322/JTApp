package com.homechart.app.home.bean.zhuanjihuodong;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/2/1.
 */

public class ItemHuoUserInfoBean implements Serializable {

    private String user_id;
    private String nickname;
    private String slogan;
    private ItemHuoUserInfoAvatarBean avatar;
    private String profession;


    public ItemHuoUserInfoBean(String user_id, String nickname, String slogan, ItemHuoUserInfoAvatarBean avatar, String profession) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.slogan = slogan;
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

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public ItemHuoUserInfoAvatarBean getAvatar() {
        return avatar;
    }

    public void setAvatar(ItemHuoUserInfoAvatarBean avatar) {
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
        return "ItemHuoUserInfoBean{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", slogan='" + slogan + '\'' +
                ", avatar=" + avatar +
                ", profession='" + profession + '\'' +
                '}';
    }
}
