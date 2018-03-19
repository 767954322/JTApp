package com.homechart.app.home.bean.searchremind;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/3/19.
 */

public class ItemAblumInfoBean implements Serializable {

    private String avatar;
    private String album_id;
    private List<String> images;

    public ItemAblumInfoBean(String avatar, String album_id, List<String> images) {
        this.avatar = avatar;
        this.album_id = album_id;
        this.images = images;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
        return "ItemAblumInfoBean{" +
                "avatar='" + avatar + '\'' +
                ", album_id='" + album_id + '\'' +
                ", images=" + images +
                '}';
    }
}
