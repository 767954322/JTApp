package com.homechart.app.home.bean.searchablums;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/3/20.
 */

public class SearchItemBean implements Serializable {


    private SearchUserInfoBean user_info;
    private SearchAlbumInfoBean album_info;

    public SearchItemBean(SearchUserInfoBean user_info, SearchAlbumInfoBean album_info) {
        this.user_info = user_info;
        this.album_info = album_info;
    }

    public SearchUserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(SearchUserInfoBean user_info) {
        this.user_info = user_info;
    }

    public SearchAlbumInfoBean getAlbum_info() {
        return album_info;
    }

    public void setAlbum_info(SearchAlbumInfoBean album_info) {
        this.album_info = album_info;
    }

    @Override
    public String toString() {
        return "SearchItemBean{" +
                "user_info=" + user_info +
                ", album_info=" + album_info +
                '}';
    }
}
