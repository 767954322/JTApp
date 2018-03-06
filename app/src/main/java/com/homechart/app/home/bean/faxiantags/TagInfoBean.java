package com.homechart.app.home.bean.faxiantags;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/3/6.
 */

public class TagInfoBean implements Serializable {

    private String tag_name;
    private String tag_id;

    public TagInfoBean(String tag_name, String tag_id) {
        this.tag_name = tag_name;
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    @Override
    public String toString() {
        return "TagInfoBean{" +
                "tag_name='" + tag_name + '\'' +
                ", tag_id='" + tag_id + '\'' +
                '}';
    }
}
