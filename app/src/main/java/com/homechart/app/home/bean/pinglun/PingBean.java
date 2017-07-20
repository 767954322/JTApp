package com.homechart.app.home.bean.pinglun;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/28.
 */

public class PingBean implements Serializable{

    private PingDataBean data;


    public PingBean(PingDataBean data) {
        this.data = data;
    }

    public PingDataBean getData() {
        return data;
    }

    public void setData(PingDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PingBean{" +
                "data=" + data +
                '}';
    }
}
