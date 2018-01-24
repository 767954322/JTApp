package com.homechart.app.lingganji.common.entity.inspirationlist;

/**
 * Created by gumenghao on 18/1/24.
 */

public class InspirationUserAvatarInfo {

    private String big;
    private String thumb;

    public InspirationUserAvatarInfo(String big, String thumb) {
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
        return "InspirationUserAvatarInfo{" +
                "big='" + big + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
