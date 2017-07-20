package com.homechart.app.home.bean.shouye;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/14.
 */

public class DataBean implements Serializable{

    private List<SYActivityBean> activity_list;
    private List<SYDataBean> object_list;


    public DataBean(List<SYActivityBean> activity_list, List<SYDataBean> object_list) {
        this.activity_list = activity_list;
        this.object_list = object_list;
    }

    public List<SYActivityBean> getActivity_list() {
        return activity_list;
    }

    public void setActivity_list(List<SYActivityBean> activity_list) {
        this.activity_list = activity_list;
    }

    public List<SYDataBean> getObject_list() {
        return object_list;
    }

    public void setObject_list(List<SYDataBean> object_list) {
        this.object_list = object_list;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "activity_list=" + activity_list +
                ", object_list=" + object_list +
                '}';
    }
}
