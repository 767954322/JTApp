package com.homechart.app.home.bean.searchfservice;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/9/14.
 */

public class SearchSCateBean implements Serializable {


    private SearchSCateInfoBean category_info;

    public SearchSCateBean(SearchSCateInfoBean category_info) {
        this.category_info = category_info;
    }

    public SearchSCateInfoBean getCategory_info() {
        return category_info;
    }

    public void setCategory_info(SearchSCateInfoBean category_info) {
        this.category_info = category_info;
    }

    @Override
    public String toString() {
        return "SearchSCateBean{" +
                "category_info=" + category_info +
                '}';
    }
}
