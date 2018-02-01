package com.homechart.app.home.bean.zhuanjihuodong;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/2/1.
 */

public class ItemHuoUserInfoAvatarBean implements Serializable {

    private String big;
    private String thumb;

    public ItemHuoUserInfoAvatarBean(String big, String thumb) {
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
        return "ItemHuoUserInfoAvatarBean{" +
                "big='" + big + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
