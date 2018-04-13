package com.homechart.app.home.bean.search;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/4/13.
 */

public class SearchDataAlbumBean implements Serializable {

    private String album_id;
    private String album_name;
    private String show_type;

    public SearchDataAlbumBean(String album_id, String album_name, String show_type) {
        this.album_id = album_id;
        this.album_name = album_name;
        this.show_type = show_type;
    }

    @Override
    public String toString() {
        return "SearchDataAlbumBean{" +
                "album_id='" + album_id + '\'' +
                ", album_name='" + album_name + '\'' +
                ", show_type='" + show_type + '\'' +
                '}';
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getShow_type() {
        return show_type;
    }

    public void setShow_type(String show_type) {
        this.show_type = show_type;
    }
}
