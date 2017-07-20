package com.homechart.app.home.bean.putheader;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/9.
 */

public class HeaderAvatar implements Serializable{

    private String big;
    private String thumb;

    public HeaderAvatar(String big, String thumb) {
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
        return "HeaderAvatar{" +
                "big='" + big + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
