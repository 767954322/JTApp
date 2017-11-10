package com.homechart.app.home.bean.searchfservice;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/9/14.
 */

public class SearchSBean implements Serializable {

    private String image_id;
    private String image_url;
    private List<SearchSObjectBean> object_list;

    public SearchSBean(String image_id, String image_url, List<SearchSObjectBean> object_list) {
        this.image_id = image_id;
        this.image_url = image_url;
        this.object_list = object_list;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public List<SearchSObjectBean> getObject_list() {
        return object_list;
    }

    public void setObject_list(List<SearchSObjectBean> object_list) {
        this.object_list = object_list;
    }

    @Override
    public String toString() {
        return "SearchSBean{" +
                "image_id='" + image_id + '\'' +
                ", image_url='" + image_url + '\'' +
                ", object_list=" + object_list +
                '}';
    }
}
