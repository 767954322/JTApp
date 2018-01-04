package com.homechart.app.lingganji.common.entity.inspirationdetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/4.
 */

public class InsDetailsUserAvatarBean implements Serializable {

    private String big;
    private String thumb;

    public InsDetailsUserAvatarBean(String big, String thumb) {
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
        return "InsDetailsUserAvatarBean{" +
                "big='" + big + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
