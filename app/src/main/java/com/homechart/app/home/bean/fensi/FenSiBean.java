package com.homechart.app.home.bean.fensi;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/7.
 */

public class FenSiBean implements Serializable{


    private List<UserListBean> user_list;

    public FenSiBean(List<UserListBean> user_list) {
        this.user_list = user_list;
    }

    public List<UserListBean> getUser_list() {
        return user_list;
    }

    public void setUser_list(List<UserListBean> user_list) {
        this.user_list = user_list;
    }

    @Override
    public String toString() {
        return "FenSiBean{" +
                "user_list=" + user_list +
                '}';
    }
}
