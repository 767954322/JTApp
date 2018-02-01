package com.homechart.app.home.bean.dingyue;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/2/1.
 */

public class DingYueUserAvatarBean implements Serializable {


    private String big;
    private String thumb;


    public DingYueUserAvatarBean(String big, String thumb) {
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
        return "DingYueUserAvatarBean{" +
                "big='" + big + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
