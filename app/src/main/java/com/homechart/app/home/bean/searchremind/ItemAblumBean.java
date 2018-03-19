package com.homechart.app.home.bean.searchremind;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/3/19.
 */

public class ItemAblumBean implements Serializable {


    private ItemAblumInfoBean album_info;

    public ItemAblumBean(ItemAblumInfoBean album_info) {
        this.album_info = album_info;
    }

    public ItemAblumInfoBean getAlbum_info() {
        return album_info;
    }

    public void setAlbum_info(ItemAblumInfoBean album_info) {
        this.album_info = album_info;
    }

    @Override
    public String toString() {
        return "ItemAblumBean{" +
                "album_info=" + album_info +
                '}';
    }
}
