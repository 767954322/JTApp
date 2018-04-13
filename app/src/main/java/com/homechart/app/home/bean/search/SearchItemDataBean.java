package com.homechart.app.home.bean.search;

import com.homechart.app.home.bean.shouye.SYDataColorBean;
import com.homechart.app.home.bean.shouye.SYDataObjectBean;
import com.homechart.app.home.bean.shouye.SYDataUserBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/15.
 */

public class SearchItemDataBean implements Serializable{


    private SearchItemInfoDataBean item_info;
    private List<SearchDataColorBean> color_info;
    private SearchDataUserBean user_info;
    private SearchDataAlbumBean album_info;

    public SearchItemDataBean() {
    }


    public SearchItemDataBean(SearchItemInfoDataBean item_info, List<SearchDataColorBean> color_info, SearchDataUserBean user_info, SearchDataAlbumBean album_info) {
        this.item_info = item_info;
        this.color_info = color_info;
        this.user_info = user_info;
        this.album_info = album_info;
    }

    public SearchItemInfoDataBean getItem_info() {
        return item_info;
    }

    public void setItem_info(SearchItemInfoDataBean item_info) {
        this.item_info = item_info;
    }

    public List<SearchDataColorBean> getColor_info() {
        return color_info;
    }

    public void setColor_info(List<SearchDataColorBean> color_info) {
        this.color_info = color_info;
    }

    public SearchDataUserBean getUser_info() {
        return user_info;
    }

    public void setUser_info(SearchDataUserBean user_info) {
        this.user_info = user_info;
    }

    public SearchDataAlbumBean getAlbum_info() {
        return album_info;
    }

    public void setAlbum_info(SearchDataAlbumBean album_info) {
        this.album_info = album_info;
    }

    @Override
    public String toString() {
        return "SearchItemDataBean{" +
                "item_info=" + item_info +
                ", color_info=" + color_info +
                ", user_info=" + user_info +
                ", album_info=" + album_info +
                '}';
    }
}
