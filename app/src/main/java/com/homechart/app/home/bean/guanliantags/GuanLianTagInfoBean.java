package com.homechart.app.home.bean.guanliantags;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/1/31.
 */

public class GuanLianTagInfoBean implements Serializable {

    private String tag_id;
    private String tag_name;
    private String subscribe_show;
    private String is_subscribed;
    private List<String> relation_tag;


    public GuanLianTagInfoBean(String tag_id, String tag_name, String subscribe_show, String is_subscribed, List<String> relation_tag) {
        this.tag_id = tag_id;
        this.tag_name = tag_name;
        this.subscribe_show = subscribe_show;
        this.is_subscribed = is_subscribed;
        this.relation_tag = relation_tag;
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

    public String getSubscribe_show() {
        return subscribe_show;
    }

    public void setSubscribe_show(String subscribe_show) {
        this.subscribe_show = subscribe_show;
    }

    public String getIs_subscribed() {
        return is_subscribed;
    }

    public void setIs_subscribed(String is_subscribed) {
        this.is_subscribed = is_subscribed;
    }

    public List<String> getRelation_tag() {
        return relation_tag;
    }

    public void setRelation_tag(List<String> relation_tag) {
        this.relation_tag = relation_tag;
    }

    @Override
    public String toString() {
        return "GuanLianTagInfoBean{" +
                "tag_id='" + tag_id + '\'' +
                ", tag_name='" + tag_name + '\'' +
                ", subscribe_show='" + subscribe_show + '\'' +
                ", is_subscribed='" + is_subscribed + '\'' +
                ", relation_tag=" + relation_tag +
                '}';
    }
}
