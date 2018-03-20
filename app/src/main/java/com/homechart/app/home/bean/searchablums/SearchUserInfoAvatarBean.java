package com.homechart.app.home.bean.searchablums;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/3/20.
 */

public class SearchUserInfoAvatarBean implements Serializable {

    private String big;
    private String thumb;

    public SearchUserInfoAvatarBean(String big, String thumb) {
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
        return "SearchUserInfoAvatarBean{" +
                "big='" + big + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
