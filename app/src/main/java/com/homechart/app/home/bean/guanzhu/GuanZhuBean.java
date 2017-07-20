package com.homechart.app.home.bean.guanzhu;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/7.
 */

public class GuanZhuBean implements Serializable{


    private List<GuanZhuUserListBean> user_list;

    public GuanZhuBean(List<GuanZhuUserListBean> user_list) {
        this.user_list = user_list;
    }

    public List<GuanZhuUserListBean> getUser_list() {
        return user_list;
    }

    public void setUser_list(List<GuanZhuUserListBean> user_list) {
        this.user_list = user_list;
    }

    @Override
    public String toString() {
        return "GuanZhuBean{" +
                "user_list=" + user_list +
                '}';
    }
}
