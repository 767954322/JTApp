package com.homechart.app.home.bean.hddetails;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/29.
 */

public class ItemUserAvatarBean implements Serializable{

    private String big;
    private String thumb;

    public ItemUserAvatarBean(String big, String thumb) {
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
        return "ItemUserAvatarBean{" +
                "big='" + big + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
