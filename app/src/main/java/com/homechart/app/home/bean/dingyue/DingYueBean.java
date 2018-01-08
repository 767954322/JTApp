package com.homechart.app.home.bean.dingyue;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/1/8.
 */

public class DingYueBean implements Serializable {

    private List<DingYueItemBean>  album_list;

    public DingYueBean(List<DingYueItemBean> album_list) {
        this.album_list = album_list;
    }

    public List<DingYueItemBean> getAlbum_list() {
        return album_list;
    }

    public void setAlbum_list(List<DingYueItemBean> album_list) {
        this.album_list = album_list;
    }

    @Override
    public String toString() {
        return "DingYueBean{" +
                "album_list=" + album_list +
                '}';
    }
}
