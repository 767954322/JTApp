package com.homechart.app.home.bean.huodong;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/29.
 */

public class HuoDongDataBean implements Serializable{

   private ActivityDataBean data;

    public HuoDongDataBean(ActivityDataBean data) {
        this.data = data;
    }

    public ActivityDataBean getData() {
        return data;
    }

    public void setData(ActivityDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HuoDongDataBean{" +
                "data=" + data +
                '}';
    }
}
