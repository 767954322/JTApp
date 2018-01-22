package com.homechart.app.home.bean.imagedetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/22.
 */

public class AlbumInfoBean implements Serializable {

    private String album_id;
    private String album_name;

    public AlbumInfoBean(String album_id, String album_name) {
        this.album_id = album_id;
        this.album_name = album_name;
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

    @Override
    public String toString() {
        return "AlbumInfoBean{" +
                "album_id='" + album_id + '\'' +
                ", album_name='" + album_name + '\'' +
                '}';
    }
}
