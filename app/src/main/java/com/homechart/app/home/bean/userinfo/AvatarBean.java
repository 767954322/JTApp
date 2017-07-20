package com.homechart.app.home.bean.userinfo;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/7.
 */

public class AvatarBean implements Serializable{

    private String big;
    private String thumb;

    public AvatarBean(String big, String thumb) {
        this.big = big;
        this.thumb = thumb;
    }

    public AvatarBean() {
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
        return "AvatarBean{" +
                "big='" + big + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
