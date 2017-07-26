package com.homechart.app.home.bean.articleping;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/26.
 */

public class PingReplyUserInfoAvatarBean implements Serializable {

    private String big;
    private String thumb;

    public PingReplyUserInfoAvatarBean(String big, String thumb) {
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
        return "PingUserInfoAvatarBean{" +
                "big='" + big + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
