package com.homechart.app.home.bean.login;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/5.
 */

public class LoginAvatar implements Serializable{

    private String big;
    private String thumb;

    public LoginAvatar(String big, String thumb) {
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
        return "LoginAvatar{" +
                "big='" + big + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
