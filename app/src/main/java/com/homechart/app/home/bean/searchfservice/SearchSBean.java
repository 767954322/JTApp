package com.homechart.app.home.bean.searchfservice;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/9/14.
 */

public class SearchSBean implements Serializable {

    private String image_id;
    private List<SearchSObjectBean> object_list;

    public SearchSBean(String image_id, List<SearchSObjectBean> object_list) {
        this.image_id = image_id;
        this.object_list = object_list;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
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
                ", object_list=" + object_list +
                '}';
    }
}
