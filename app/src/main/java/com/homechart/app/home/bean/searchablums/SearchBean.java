package com.homechart.app.home.bean.searchablums;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/3/20.
 */

public class SearchBean implements Serializable {

    private List<SearchItemBean> album_list;


    public SearchBean(List<SearchItemBean> album_list) {
        this.album_list = album_list;
    }

    public List<SearchItemBean> getAlbum_list() {
        return album_list;
    }

    public void setAlbum_list(List<SearchItemBean> album_list) {
        this.album_list = album_list;
    }

    @Override
    public String toString() {
        return "SearchBean{" +
                "album_list=" + album_list +
                '}';
    }
}
