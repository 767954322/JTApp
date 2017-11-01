package com.homechart.app.home.bean.searchfservice;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/11/1.
 */

public class TypeNewBean implements Serializable{

    private List<ItemTypeNewBean> category_list;


    public TypeNewBean(List<ItemTypeNewBean> category_list) {
        this.category_list = category_list;
    }

    public List<ItemTypeNewBean> getCategory_list() {
        return category_list;
    }

    public void setCategory_list(List<ItemTypeNewBean> category_list) {
        this.category_list = category_list;
    }

    @Override
    public String toString() {
        return "TypeNewBean{" +
                "category_list=" + category_list +
                '}';
    }
}
