package com.homechart.app.home.bean.zhuanjihuodong;

import com.homechart.app.home.bean.huodong.ActivityDataBean;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/16.
 */

public class HuoDataBean implements Serializable {

    private HuoListDataBean data;

    public HuoDataBean(HuoListDataBean data) {
        this.data = data;
    }

    public HuoListDataBean getData() {
        return data;
    }

    public void setData(HuoListDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HuoDataBean{" +
                "data=" + data +
                '}';
    }
}
