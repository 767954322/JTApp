package com.homechart.app.home.bean.zhuanjihuodong;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/16.
 */

public class ItemHuoAlbumDataBean implements Serializable {

    private String album_id;
    private String album_name;
    private String tag;
    private String description;
    private String item_num;
    private String subscribe_num;
    private String show_type;
    private String update_time;
    private ItemHuoAlbumImageDataBean cover_image;


    public ItemHuoAlbumDataBean(String album_id, String album_name, String tag, String description, String item_num, String subscribe_num, String show_type, String update_time, ItemHuoAlbumImageDataBean cover_image) {
        this.album_id = album_id;
        this.album_name = album_name;
        this.tag = tag;
        this.description = description;
        this.item_num = item_num;
        this.subscribe_num = subscribe_num;
        this.show_type = show_type;
        this.update_time = update_time;
        this.cover_image = cover_image;
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

    public String getShow_type() {
        return show_type;
    }

    public void setShow_type(String show_type) {
        this.show_type = show_type;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public ItemHuoAlbumImageDataBean getCover_image() {
        return cover_image;
    }

    public void setCover_image(ItemHuoAlbumImageDataBean cover_image) {
        this.cover_image = cover_image;
    }

    @Override
    public String toString() {
        return "ItemHuoAlbumDataBean{" +
                "album_id='" + album_id + '\'' +
                ", album_name='" + album_name + '\'' +
                ", tag='" + tag + '\'' +
                ", description='" + description + '\'' +
                ", item_num='" + item_num + '\'' +
                ", subscribe_num='" + subscribe_num + '\'' +
                ", show_type='" + show_type + '\'' +
                ", update_time='" + update_time + '\'' +
                ", cover_image=" + cover_image +
                '}';
    }
}
