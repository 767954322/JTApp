package com.homechart.app.home.bean.pictag;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/15.
 */

public class TagDataBean implements Serializable{

    private List<TagItemDataBean>  tag_id;

    public TagDataBean(List<TagItemDataBean> tag_id) {
        this.tag_id = tag_id;
    }

    public List<TagItemDataBean> getTag_id() {
        return tag_id;
    }

    public void setTag_id(List<TagItemDataBean> tag_id) {
        this.tag_id = tag_id;
    }

    @Override
    public String toString() {
        return "TagDataBean{" +
                "tag_id=" + tag_id +
                '}';
    }
}
