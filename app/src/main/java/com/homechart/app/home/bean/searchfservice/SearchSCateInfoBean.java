package com.homechart.app.home.bean.searchfservice;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/9/14.
 */

public class SearchSCateInfoBean implements Serializable {

    private int category_id;
    private String category_name;

    public SearchSCateInfoBean(int category_id, String category_name) {
        this.category_id = category_id;
        this.category_name = category_name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @Override
    public String toString() {
        return "SearchSCateInfoBean{" +
                "category_id=" + category_id +
                ", category_name='" + category_name + '\'' +
                '}';
    }
}
