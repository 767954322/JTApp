package com.homechart.app.home.bean.jubaobean;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/23.
 */

public class JuBaoItemBean implements Serializable {

    private int report_id;
    private String report_value;

    public JuBaoItemBean(int report_id, String report_value) {
        this.report_id = report_id;
        this.report_value = report_value;
    }

    public int getReport_id() {
        return report_id;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public String getReport_value() {
        return report_value;
    }

    public void setReport_value(String report_value) {
        this.report_value = report_value;
    }

    @Override
    public String toString() {
        return "JuBaoItemBean{" +
                "report_id=" + report_id +
                ", report_value='" + report_value + '\'' +
                '}';
    }
}
