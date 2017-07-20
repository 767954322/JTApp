package com.homechart.app.home.bean.hddetails;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/29.
 */

public class HDDetailsDataBean implements Serializable {

    private ActivityInfoBean activity_info;
    private List<ItemUserBean> user_list;

    public HDDetailsDataBean(ActivityInfoBean activity_info, List<ItemUserBean> user_list) {
        this.activity_info = activity_info;
        this.user_list = user_list;
    }

    public ActivityInfoBean getActivity_info() {
        return activity_info;
    }

    public void setActivity_info(ActivityInfoBean activity_info) {
        this.activity_info = activity_info;
    }

    public List<ItemUserBean> getUser_list() {
        return user_list;
    }

    public void setUser_list(List<ItemUserBean> user_list) {
        this.user_list = user_list;
    }

    @Override
    public String toString() {
        return "HDDetailsDataBean{" +
                "activity_info=" + activity_info +
                ", user_list=" + user_list +
                '}';
    }
}
