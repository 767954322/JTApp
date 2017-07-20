package com.homechart.app.home.bean.fabu;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/21.
 */

public class ActivityItemDataBean implements Serializable{

    private ActivityItemInfoDataBean activity_info;

    public ActivityItemDataBean(ActivityItemInfoDataBean activity_info) {
        this.activity_info = activity_info;
    }

    public ActivityItemInfoDataBean getActivity_info() {
        return activity_info;
    }

    public void setActivity_info(ActivityItemInfoDataBean activity_info) {
        this.activity_info = activity_info;
    }

    @Override
    public String toString() {
        return "ActivityItemDataBean{" +
                "activity_info=" + activity_info +
                '}';
    }
}
