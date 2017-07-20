package com.homechart.app.home.bean.shaixuan;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/15.
 */

public class ShaiXuanBean implements Serializable{

    private List<String> tag_list;

    public ShaiXuanBean(List<String> tag_list) {
        this.tag_list = tag_list;
    }

    public List<String> getTag_list() {
        return tag_list;
    }

    public void setTag_list(List<String> tag_list) {
        this.tag_list = tag_list;
    }

    @Override
    public String toString() {
        return "ShaiXuanBean{" +
                "tag_list=" + tag_list +
                '}';
    }
}
