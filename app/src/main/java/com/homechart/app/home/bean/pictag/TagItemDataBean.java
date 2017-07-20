package com.homechart.app.home.bean.pictag;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/15.
 */

public class TagItemDataBean implements Serializable{

    private String tag_id;
    private String tag_name;
    private List<TagItemDataChildBean>  children;

    public TagItemDataBean(String tag_id, String tag_name, List<TagItemDataChildBean> children) {
        this.tag_id = tag_id;
        this.tag_name = tag_name;
        this.children = children;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public List<TagItemDataChildBean> getChildren() {
        return children;
    }

    public void setChildren(List<TagItemDataChildBean> children) {
        this.children = children;
    }
}
