package com.homechart.app.home.bean.jubaobean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/1/23.
 */

public class JuBaoBean implements Serializable{


    private List<JuBaoItemBean> data;

    public JuBaoBean(List<JuBaoItemBean> data) {
        this.data = data;
    }

    public List<JuBaoItemBean> getData() {
        return data;
    }

    public void setData(List<JuBaoItemBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JuBaoBean{" +
                "data=" + data +
                '}';
    }
}
