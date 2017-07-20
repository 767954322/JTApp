package com.homechart.app.home.bean.color;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/19.
 */

public class ColorBean implements Serializable{

    private List<ColorItemBean>  color_list;

    public ColorBean(List<ColorItemBean> color_list) {
        this.color_list = color_list;
    }

    public List<ColorItemBean> getColor_list() {
        return color_list;
    }

    public void setColor_list(List<ColorItemBean> color_list) {
        this.color_list = color_list;
    }

    @Override
    public String toString() {
        return "ColorBean{" +
                "color_list=" + color_list +
                '}';
    }
}
