package com.homechart.app.home.bean.searchfservice;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/9/14.
 */

public class SearchSObjectBean implements Serializable {


    private SearchSObjectInfoBean object_info;

    public SearchSObjectBean(SearchSObjectInfoBean object_info) {
        this.object_info = object_info;
    }

    public SearchSObjectInfoBean getObject_info() {
        return object_info;
    }

    public void setObject_info(SearchSObjectInfoBean object_info) {
        this.object_info = object_info;
    }

    @Override
    public String toString() {
        return "SearchSObjectBean{" +
                "object_info=" + object_info +
                '}';
    }
}
