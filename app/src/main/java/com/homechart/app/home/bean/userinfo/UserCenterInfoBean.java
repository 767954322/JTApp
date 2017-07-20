package com.homechart.app.home.bean.userinfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/7.
 */

public class UserCenterInfoBean implements Serializable {

    private UserInfoBean user_info;
    private CounterBean counter;
    private ProInfo pro_info;

    public UserCenterInfoBean(UserInfoBean user_info, CounterBean counter, ProInfo pro_info) {
        this.user_info = user_info;
        this.counter = counter;
        this.pro_info = pro_info;
    }

    public UserCenterInfoBean() {
    }

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public CounterBean getCounter() {
        return counter;
    }

    public void setCounter(CounterBean counter) {
        this.counter = counter;
    }

    public ProInfo getPro_info() {
        return pro_info;
    }

    public void setPro_info(ProInfo pro_info) {
        this.pro_info = pro_info;
    }

    @Override
    public String toString() {
        return "UserCenterInfoBean{" +
                "user_info=" + user_info +
                ", counter=" + counter +
                ", pro_info=" + pro_info +
                '}';
    }
}
