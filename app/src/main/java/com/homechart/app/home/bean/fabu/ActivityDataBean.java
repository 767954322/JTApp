package com.homechart.app.home.bean.fabu;

import com.homechart.app.home.bean.shouye.SYActivityBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/21.
 */

public class ActivityDataBean implements Serializable{


    private List<ActivityItemDataBean> activity_list;

    public ActivityDataBean(List<ActivityItemDataBean> activity_list) {
        this.activity_list = activity_list;
    }

    public List<ActivityItemDataBean> getActivity_list() {
        return activity_list;
    }

    public void setActivity_list(List<ActivityItemDataBean> activity_list) {
        this.activity_list = activity_list;
    }

    @Override
    public String toString() {
        return "ActivityDataBean{" +
                "activity_list=" + activity_list +
                '}';
    }
}
