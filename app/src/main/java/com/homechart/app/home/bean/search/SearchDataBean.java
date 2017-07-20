package com.homechart.app.home.bean.search;

import com.homechart.app.home.bean.shouye.SYActivityBean;
import com.homechart.app.home.bean.shouye.SYDataBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/14.
 */

public class SearchDataBean implements Serializable{

    private List<SearchItemDataBean> item_list;


    public SearchDataBean(List<SearchItemDataBean> item_list) {
        this.item_list = item_list;
    }

    public List<SearchItemDataBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<SearchItemDataBean> item_list) {
        this.item_list = item_list;
    }

    @Override
    public String toString() {
        return "SearchDataBean{" +
                "item_list=" + item_list +
                '}';
    }
}
