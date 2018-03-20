package com.homechart.app.home.bean.searchablums;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/3/20.
 */

public class SearchAlbumInfoBean implements Serializable {

    private String is_collected;
    private String album_id;
    private String item_num;
    private String cover_image;

    public SearchAlbumInfoBean(String is_collected, String album_id, String item_num, String cover_image) {
        this.is_collected = is_collected;
        this.album_id = album_id;
        this.item_num = item_num;
        this.cover_image = cover_image;
    }

    public String getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(String is_collected) {
        this.is_collected = is_collected;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getItem_num() {
        return item_num;
    }

    public void setItem_num(String item_num) {
        this.item_num = item_num;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    @Override
    public String toString() {
        return "SearchAlbumInfoBean{" +
                "is_collected='" + is_collected + '\'' +
                ", album_id='" + album_id + '\'' +
                ", item_num='" + item_num + '\'' +
                ", cover_image='" + cover_image + '\'' +
                '}';
    }
}
