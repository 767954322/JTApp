package com.homechart.app.home.bean.huodongpeople;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/17.
 */

public class ZhongBean implements Serializable {

    private ZhongDataBean data;

    public ZhongBean(ZhongDataBean data) {
        this.data = data;
    }

    public ZhongDataBean getData() {
        return data;
    }

    public void setData(ZhongDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ZhongBean{" +
                "data=" + data +
                '}';
    }
}
