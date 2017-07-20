package com.homechart.app.home.bean.city;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/8.
 */

public class ProvinceBean implements Serializable{


    private List<ItemProvinceBean> data;

    public ProvinceBean(List<ItemProvinceBean> data) {
        this.data = data;
    }

    public List<ItemProvinceBean> getData() {
        return data;
    }

    public void setData(List<ItemProvinceBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ProvinceBean{" +
                "data=" + data +
                '}';
    }
}
