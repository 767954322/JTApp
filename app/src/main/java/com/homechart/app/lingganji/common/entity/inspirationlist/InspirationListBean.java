package com.homechart.app.lingganji.common.entity.inspirationlist;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/1/3.
 */

public class InspirationListBean implements Serializable{

    private List<InspirationBean> album_list;

    public InspirationListBean() {
    }

    public InspirationListBean(List<InspirationBean> album_list) {
        this.album_list = album_list;
    }

    public List<InspirationBean> getAlbum_list() {
        return album_list;
    }

    public void setAlbum_list(List<InspirationBean> album_list) {
        this.album_list = album_list;
    }

    @Override
    public String toString() {
        return "InspirationListBean{" +
                "album_list=" + album_list +
                '}';
    }
}
