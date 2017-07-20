package com.homechart.app.home.bean.imagedetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/28.
 */

public class ColorInfoBean implements Serializable {

    private int color_id;
    private String color_value;
    private String color_percent;
    private String color_class;
    private String color_name;

    public ColorInfoBean(int color_id, String color_value, String color_percent, String color_class, String color_name) {
        this.color_id = color_id;
        this.color_value = color_value;
        this.color_percent = color_percent;
        this.color_class = color_class;
        this.color_name = color_name;
    }

    public int getColor_id() {
        return color_id;
    }

    public void setColor_id(int color_id) {
        this.color_id = color_id;
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

    public String getColor_class() {
        return color_class;
    }

    public void setColor_class(String color_class) {
        this.color_class = color_class;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }


    @Override
    public String toString() {
        return "ColorInfoBean{" +
                "color_id=" + color_id +
                ", color_value='" + color_value + '\'' +
                ", color_percent='" + color_percent + '\'' +
                ", color_class='" + color_class + '\'' +
                ", color_name='" + color_name + '\'' +
                '}';
    }
}
