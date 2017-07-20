package com.homechart.app.home.bean.guanzhu;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/7.
 */

public class GuanZhuAvatarBean implements Serializable{

    private String big;
    private String thumb;

    public GuanZhuAvatarBean(String big, String thumb) {
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
        return "GuanZhuAvatarBean{" +
                "big='" + big + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
