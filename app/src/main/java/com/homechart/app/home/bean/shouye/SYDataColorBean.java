package com.homechart.app.home.bean.shouye;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/14.
 */

public class SYDataColorBean implements Serializable{

    private String color_value;
    private String color_percent;

    public SYDataColorBean(String color_value, String color_percent) {
        this.color_value = color_value;
        this.color_percent = color_percent;
    }

    public String getColor_value() {
        return color_value;
    }

    public void setColor_value(String color_value) {
        this.color_value = color_value;
    }

    public String getColor_percent() {
        return color_percent;
    }

    public void setColor_percent(String color_percent) {
        this.color_percent = color_percent;
    }

    @Override
    public String toString() {
        return "SYDataColorBean{" +
                "color_value='" + color_value + '\'' +
                ", color_percent='" + color_percent + '\'' +
                '}';
    }
}
