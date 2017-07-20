package com.homechart.app.home.bean.shouye;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/14.
 */

public class SYDataBean implements Serializable {

    private SYDataObjectBean object_info;
    private List<SYDataColorBean> color_info;
    private SYDataUserBean user_info;

    public SYDataBean(SYDataObjectBean object_info, List<SYDataColorBean> color_info, SYDataUserBean user_info) {
        this.object_info = object_info;
        this.color_info = color_info;
        this.user_info = user_info;
    }

    public SYDataObjectBean getObject_info() {
        return object_info;
    }

    public void setObject_info(SYDataObjectBean object_info) {
        this.object_info = object_info;
    }

    public List<SYDataColorBean> getColor_info() {
        return color_info;
    }

    public void setColor_info(List<SYDataColorBean> color_info) {
        this.color_info = color_info;
    }

    public SYDataUserBean getUser_info() {
        return user_info;
    }

    public void setUser_info(SYDataUserBean user_info) {
        this.user_info = user_info;
    }

    @Override
    public String toString() {
        return "SYDataBean{" +
                "object_info=" + object_info +
                ", color_info=" + color_info +
                ", user_info=" + user_info +
                '}';
    }
}
