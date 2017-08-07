package com.homechart.app.home.bean.huodonglist;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/8/7.
 */

public class HDListBean implements Serializable{

    private List<HDItemBean> activity_list;

    public HDListBean(List<HDItemBean> activity_list) {
        this.activity_list = activity_list;
    }

    public List<HDItemBean> getActivity_list() {
        return activity_list;
    }

    public void setActivity_list(List<HDItemBean> activity_list) {
        this.activity_list = activity_list;
    }

    @Override
    public String toString() {
        return "HDListBean{" +
                "activity_list=" + activity_list +
                '}';
    }
}
