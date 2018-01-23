package com.homechart.app.home.bean.imagedetail;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/1/22.
 */

public class RelatedAlbumsDataBean implements Serializable{


    private String user_avatar;
    private String album_id;
    private List<String> images;

    public RelatedAlbumsDataBean(String user_avatar, String album_id, List<String> images) {
        this.user_avatar = user_avatar;
        this.album_id = album_id;
        this.images = images;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "RelatedAlbumsDataBean{" +
                "user_avatar='" + user_avatar + '\'' +
                ", album_id='" + album_id + '\'' +
                ", images=" + images +
                '}';
    }
}
