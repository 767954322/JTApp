package com.homechart.app.lingganji.common.entity.inspirationdetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/4.
 */

public class InsDetailsAlbumInfoBean implements Serializable {

    private String album_id;
    private String album_name;
    private String tag;
    private String description;
    private String item_num;
    private String subscribe_num;
    private String update_time;
    private String is_subscribed;

    public InsDetailsAlbumInfoBean(String album_id, String album_name, String tag, String description, String item_num, String subscribe_num, String update_time, String is_subscribed) {
        this.album_id = album_id;
        this.album_name = album_name;
        this.tag = tag;
        this.description = description;
        this.item_num = item_num;
        this.subscribe_num = subscribe_num;
        this.update_time = update_time;
        this.is_subscribed = is_subscribed;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItem_num() {
        return item_num;
    }

    public void setItem_num(String item_num) {
        this.item_num = item_num;
    }

    public String getSubscribe_num() {
        return subscribe_num;
    }

    public void setSubscribe_num(String subscribe_num) {
        this.subscribe_num = subscribe_num;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getIs_subscribed() {
        return is_subscribed;
    }

    public void setIs_subscribed(String is_subscribed) {
        this.is_subscribed = is_subscribed;
    }

    @Override
    public String toString() {
        return "InsDetailsAlbumInfoBean{" +
                "album_id='" + album_id + '\'' +
                ", album_name='" + album_name + '\'' +
                ", tag='" + tag + '\'' +
                ", description='" + description + '\'' +
                ", item_num='" + item_num + '\'' +
                ", subscribe_num='" + subscribe_num + '\'' +
                ", update_time='" + update_time + '\'' +
                ", is_subscribed='" + is_subscribed + '\'' +
                '}';
    }
}
