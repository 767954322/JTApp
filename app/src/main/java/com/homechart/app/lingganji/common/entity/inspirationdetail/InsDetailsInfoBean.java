package com.homechart.app.lingganji.common.entity.inspirationdetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/4.
 */

public class InsDetailsInfoBean implements Serializable {

    private InsDetailsAlbumInfoBean album_info;
    private InsDetailsUserInfoBean user_info;

    public InsDetailsInfoBean(InsDetailsAlbumInfoBean album_info, InsDetailsUserInfoBean user_info) {
        this.album_info = album_info;
        this.user_info = user_info;
    }

    public InsDetailsAlbumInfoBean getAlbum_info() {
        return album_info;
    }

    public void setAlbum_info(InsDetailsAlbumInfoBean album_info) {
        this.album_info = album_info;
    }

    public InsDetailsUserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(InsDetailsUserInfoBean user_info) {
        this.user_info = user_info;
    }

    @Override
    public String toString() {
        return "InsDetailsInfoBean{" +
                "album_info=" + album_info +
                ", user_info=" + user_info +
                '}';
    }
}
