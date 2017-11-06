package com.homechart.app.home.bean.newhistory;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/11/6.
 */

public class HistoryBean implements Serializable{

    private List<HistoryDataBean> data ;

    public HistoryBean(List<HistoryDataBean> data) {
        this.data = data;
    }

    public List<HistoryDataBean> getData() {
        return data;
    }

    public void setData(List<HistoryDataBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HistoryBean{" +
                "data=" + data +
                '}';
    }
}
