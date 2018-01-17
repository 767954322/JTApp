package com.homechart.app.home.bean.huodongpeople;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/17.
 */

public class ZhongPrizeInfoItemAvatarBean implements Serializable {

    private String big;
    private String thumb;

    public ZhongPrizeInfoItemAvatarBean(String big, String thumb) {
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
        return "ZhongPrizeInfoItemAvatarBean{" +
                "big='" + big + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
