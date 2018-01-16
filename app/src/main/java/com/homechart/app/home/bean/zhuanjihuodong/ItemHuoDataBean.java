package com.homechart.app.home.bean.zhuanjihuodong;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/16.
 */

public class ItemHuoDataBean implements Serializable {

    private ItemHuoAlbumDataBean album_info;

    public ItemHuoDataBean(ItemHuoAlbumDataBean album_info) {
        this.album_info = album_info;
    }

    public ItemHuoAlbumDataBean getAlbum_info() {
        return album_info;
    }

    public void setAlbum_info(ItemHuoAlbumDataBean album_info) {
        this.album_info = album_info;
    }

    @Override
    public String toString() {
        return "ItemHuoDataBean{" +
                "album_info=" + album_info +
                '}';
    }
}
