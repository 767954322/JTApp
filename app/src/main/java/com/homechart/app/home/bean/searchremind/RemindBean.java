package com.homechart.app.home.bean.searchremind;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/3/19.
 */

public class RemindBean implements Serializable {

    private String kw;
    private List<String> word_list;
    private List<ItemPicBean> item_list;
    private List<ItemAblumBean> album_list;

    public RemindBean(String kw, List<String> word_list, List<ItemPicBean> item_list, List<ItemAblumBean> album_list) {
        this.kw = kw;
        this.word_list = word_list;
        this.item_list = item_list;
        this.album_list = album_list;
    }

    public String getKw() {
        return kw;
    }

    public void setKw(String kw) {
        this.kw = kw;
    }

    public List<String> getWord_list() {
        return word_list;
    }

    public void setWord_list(List<String> word_list) {
        this.word_list = word_list;
    }

    public List<ItemPicBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ItemPicBean> item_list) {
        this.item_list = item_list;
    }

    public List<ItemAblumBean> getAlbum_list() {
        return album_list;
    }

    public void setAlbum_list(List<ItemAblumBean> album_list) {
        this.album_list = album_list;
    }

    @Override
    public String toString() {
        return "RemindBean{" +
                "kw='" + kw + '\'' +
                ", word_list=" + word_list +
                ", item_list=" + item_list +
                ", album_list=" + album_list +
                '}';
    }
}
