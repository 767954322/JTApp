package com.homechart.app.home.bean.huodongpeople;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/17.
 */

public class ZhongPrizeInfoItemBean implements Serializable {


    private String user_id;
    private String nickname;
    private ZhongPrizeInfoItemAvatarBean avatar;

    public ZhongPrizeInfoItemBean(String user_id, String nickname, ZhongPrizeInfoItemAvatarBean avatar) {
        this.user_id = user_id;
        this.nickname = nickname;
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

    public ZhongPrizeInfoItemAvatarBean getAvatar() {
        return avatar;
    }

    public void setAvatar(ZhongPrizeInfoItemAvatarBean avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "ZhongPrizeInfoItemBean{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
