package com.homechart.app.home.bean.dingyue;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/8.
 */

public class DingYueItemBean implements Serializable {

    private DingYueItemInfoBean album_info;
    private DingYueUserInfoBean user_info;


    public DingYueItemBean(DingYueItemInfoBean album_info, DingYueUserInfoBean user_info) {
        this.album_info = album_info;
        this.user_info = user_info;
    }

    public DingYueItemInfoBean getAlbum_info() {
        return album_info;
    }

    public void setAlbum_info(DingYueItemInfoBean album_info) {
        this.album_info = album_info;
    }

    public DingYueUserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(DingYueUserInfoBean user_info) {
        this.user_info = user_info;
    }

    @Override
    public String toString() {
        return "DingYueItemBean{" +
                "album_info=" + album_info +
                ", user_info=" + user_info +
                '}';
    }
}
