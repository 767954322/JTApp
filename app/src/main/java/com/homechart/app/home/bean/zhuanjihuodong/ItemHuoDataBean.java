package com.homechart.app.home.bean.zhuanjihuodong;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/16.
 */

public class ItemHuoDataBean implements Serializable {

    private ItemHuoAlbumDataBean album_info;
    private ItemHuoUserInfoBean user_info;


    public ItemHuoDataBean(ItemHuoAlbumDataBean album_info, ItemHuoUserInfoBean user_info) {
        this.album_info = album_info;
        this.user_info = user_info;
    }

    public ItemHuoAlbumDataBean getAlbum_info() {
        return album_info;
    }

    public void setAlbum_info(ItemHuoAlbumDataBean album_info) {
        this.album_info = album_info;
    }

    public ItemHuoUserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(ItemHuoUserInfoBean user_info) {
        this.user_info = user_info;
    }

    @Override
    public String toString() {
        return "ItemHuoDataBean{" +
                "album_info=" + album_info +
                ", user_info=" + user_info +
                '}';
    }
}
