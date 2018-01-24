package com.homechart.app.lingganji.common.entity.inspirationlist;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/3.
 */

public class InspirationBean implements Serializable {

    private InspirationInfoBean album_info;
    private InspirationUserInfo user_info;


    public InspirationBean(InspirationInfoBean album_info, InspirationUserInfo user_info) {
        this.album_info = album_info;
        this.user_info = user_info;
    }

    public InspirationInfoBean getAlbum_info() {
        return album_info;
    }

    public void setAlbum_info(InspirationInfoBean album_info) {
        this.album_info = album_info;
    }

    public InspirationUserInfo getUser_info() {
        return user_info;
    }

    public void setUser_info(InspirationUserInfo user_info) {
        this.user_info = user_info;
    }

    @Override
    public String toString() {
        return "InspirationBean{" +
                "album_info=" + album_info +
                ", user_info=" + user_info +
                '}';
    }
}
