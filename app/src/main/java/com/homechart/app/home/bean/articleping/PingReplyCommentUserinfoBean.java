package com.homechart.app.home.bean.articleping;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/26.
 */

public class PingReplyCommentUserinfoBean implements Serializable {

    private String user_id;
    private String nickname;
    private String profession;
    private PingReplyUserInfoAvatarBean avatar;

    public PingReplyCommentUserinfoBean(String user_id, String nickname, String profession, PingReplyUserInfoAvatarBean avatar) {
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

    public PingReplyUserInfoAvatarBean getAvatar() {
        return avatar;
    }

    public void setAvatar(PingReplyUserInfoAvatarBean avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "PingReplyCommentUserinfoBean{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profession='" + profession + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
