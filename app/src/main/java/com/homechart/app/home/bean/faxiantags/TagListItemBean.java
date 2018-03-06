package com.homechart.app.home.bean.faxiantags;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/3/6.
 */

public class TagListItemBean implements Serializable {

    private TagInfoBean  tag_info;

    public TagListItemBean(TagInfoBean tag_info) {
        this.tag_info = tag_info;
    }

    public TagInfoBean getTag_info() {
        return tag_info;
    }

    public void setTag_info(TagInfoBean tag_info) {
        this.tag_info = tag_info;
    }

    @Override
    public String toString() {
        return "TagListItemBean{" +
                "tag_info=" + tag_info +
                '}';
    }
}
