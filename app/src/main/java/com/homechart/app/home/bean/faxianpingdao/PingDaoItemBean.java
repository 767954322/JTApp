package com.homechart.app.home.bean.faxianpingdao;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/1/30.
 */

public class PingDaoItemBean implements Serializable {

    private String channel_name;
    private List<String> relation_tag;
    private List<PingDaoUrlItemBean> rel_tag_data;


    public PingDaoItemBean(String channel_name, List<String> relation_tag, List<PingDaoUrlItemBean> rel_tag_data) {
        this.channel_name = channel_name;
        this.relation_tag = relation_tag;
        this.rel_tag_data = rel_tag_data;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public List<String> getRelation_tag() {
        return relation_tag;
    }

    public void setRelation_tag(List<String> relation_tag) {
        this.relation_tag = relation_tag;
    }

    public List<PingDaoUrlItemBean> getRel_tag_data() {
        return rel_tag_data;
    }

    public void setRel_tag_data(List<PingDaoUrlItemBean> rel_tag_data) {
        this.rel_tag_data = rel_tag_data;
    }

    @Override
    public String toString() {
        return "PingDaoItemBean{" +
                "channel_name='" + channel_name + '\'' +
                ", relation_tag=" + relation_tag +
                ", rel_tag_data=" + rel_tag_data +
                '}';
    }
}
