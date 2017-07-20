package com.homechart.app.home.bean.hddetails;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/29.
 */

public class HDDetailsBean implements Serializable{

    private HDDetailsDataBean data;


    public HDDetailsBean(HDDetailsDataBean data) {
        this.data = data;
    }

    public HDDetailsDataBean getData() {
        return data;
    }

    public void setData(HDDetailsDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HDDetailsBean{" +
                "data=" + data +
                '}';
    }
}
