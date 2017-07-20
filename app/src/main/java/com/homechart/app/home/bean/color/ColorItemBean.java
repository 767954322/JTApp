package com.homechart.app.home.bean.color;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/19.
 */

public class ColorItemBean implements Serializable{


    private int color_id;
    private String color_name;
    private String color_value;
    private String color_class;

    public ColorItemBean(int color_id, String color_name, String color_value, String color_class) {
        this.color_id = color_id;
        this.color_name = color_name;
        this.color_value = color_value;
        this.color_class = color_class;
    }

    public int getColor_id() {
        return color_id;
    }

    public void setColor_id(int color_id) {
        this.color_id = color_id;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getColor_value() {
        return color_value;
    }

    public void setColor_value(String color_value) {
        this.color_value = color_value;
    }

    public String getColor_class() {
        return color_class;
    }

    public void setColor_class(String color_class) {
        this.color_class = color_class;
    }

    @Override
    public String toString() {
        return "ColorItemBean{" +
                "color_id=" + color_id +
                ", color_name='" + color_name + '\'' +
                ", color_value='" + color_value + '\'' +
                ", color_class='" + color_class + '\'' +
                '}';
    }
}
