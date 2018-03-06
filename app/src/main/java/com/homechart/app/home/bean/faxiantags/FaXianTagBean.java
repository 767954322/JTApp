package com.homechart.app.home.bean.faxiantags;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/3/6.
 */

public class FaXianTagBean implements Serializable {

    private TagDataBean data;

    public FaXianTagBean(TagDataBean data) {
        this.data = data;
    }

    public TagDataBean getData() {
        return data;
    }

    public void setData(TagDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FaXianTagBean{" +
                "data=" + data +
                '}';
    }
}
