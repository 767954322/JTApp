package com.homechart.app.home.bean.searchablums;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/3/20.
 */

public class SearchAlbumInfoBean implements Serializable {

    private String is_collected;
    private String album_id;
    private String item_num;
    private String show_type;
    private String cover_image;
    private String album_name;


    public SearchAlbumInfoBean(String is_collected, String album_id, String item_num, String show_type, String cover_image, String album_name) {
        this.is_collected = is_collected;
        this.album_id = album_id;
        this.item_num = item_num;
        this.show_type = show_type;
        this.cover_image = cover_image;
        this.album_name = album_name;
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

    public String getShow_type() {
        return show_type;
    }

    public void setShow_type(String show_type) {
        this.show_type = show_type;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    @Override
    public String toString() {
        return "SearchAlbumInfoBean{" +
                "is_collected='" + is_collected + '\'' +
                ", album_id='" + album_id + '\'' +
                ", item_num='" + item_num + '\'' +
                ", show_type='" + show_type + '\'' +
                ", cover_image='" + cover_image + '\'' +
                ", album_name='" + album_name + '\'' +
                '}';
    }
}
