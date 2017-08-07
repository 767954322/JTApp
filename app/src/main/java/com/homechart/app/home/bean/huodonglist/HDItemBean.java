package com.homechart.app.home.bean.huodonglist;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/8/7.
 */

public class HDItemBean implements Serializable {

   private HDItemInfoBean activity_info;

    public HDItemBean(HDItemInfoBean activity_info) {
        this.activity_info = activity_info;
    }

    public HDItemInfoBean getActivity_info() {
        return activity_info;
    }

    public void setActivity_info(HDItemInfoBean activity_info) {
        this.activity_info = activity_info;
    }

    @Override
    public String toString() {
        return "HDItemBean{" +
                "activity_info=" + activity_info +
                '}';
    }
}
