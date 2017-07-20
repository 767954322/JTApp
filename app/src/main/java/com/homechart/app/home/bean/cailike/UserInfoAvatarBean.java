package com.homechart.app.home.bean.cailike;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/29.
 */

public class UserInfoAvatarBean implements Serializable{

    private String big;
    private String thumb;

    public UserInfoAvatarBean(String big, String thumb) {
        this.big = big;
        this.thumb = thumb;
    }

    public String getBig() {
        return big;
    }

    public void setBig(String big) {
        this.big = big;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return "UserInfoAvatarBean{" +
                "big='" + big + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
