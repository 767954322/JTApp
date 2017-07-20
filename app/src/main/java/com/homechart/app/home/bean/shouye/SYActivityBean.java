package com.homechart.app.home.bean.shouye;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/14.
 */

public class SYActivityBean implements Serializable{

    private SYActivityInfoBean activity_info;

    public SYActivityBean(SYActivityInfoBean activity_info) {
        this.activity_info = activity_info;
    }

    public SYActivityInfoBean getActivity_info() {
        return activity_info;
    }

    public void setActivity_info(SYActivityInfoBean activity_info) {
        this.activity_info = activity_info;
    }

    @Override
    public String toString() {
        return "SYActivityBean{" +
                "activity_info=" + activity_info +
                '}';
    }
}
