package com.homechart.app.home.bean.fensi;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/7.
 */

public class FenSiAvatarBean implements Serializable{

    private String big;
    private String thumb;

    public FenSiAvatarBean(String big, String thumb) {
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
        return "FenSiAvatarBean{" +
                "big='" + big + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
