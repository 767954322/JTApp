package com.homechart.app.home.bean.faxianpingdao;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/1/30.
 */

public class PingDaoItemBean implements Serializable {

    private String tag_name;
    private List<String> relation_tag;

    public PingDaoItemBean(String tag_name, List<String> relation_tag) {
        this.tag_name = tag_name;
        this.relation_tag = relation_tag;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public List<String> getRelation_tag() {
        return relation_tag;
    }

    public void setRelation_tag(List<String> relation_tag) {
        this.relation_tag = relation_tag;
    }

    @Override
    public String toString() {
        return "PingDaoItemBean{" +
                "tag_name='" + tag_name + '\'' +
                ", relation_tag=" + relation_tag +
                '}';
    }
}
