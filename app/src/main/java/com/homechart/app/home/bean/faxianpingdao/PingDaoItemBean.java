package com.homechart.app.home.bean.faxianpingdao;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/1/30.
 */

public class PingDaoItemBean implements Serializable {

    private String channel_name;
    private List<String> relation_tag;


    public PingDaoItemBean(String channel_name, List<String> relation_tag) {
        this.channel_name = channel_name;
        this.relation_tag = relation_tag;
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

    @Override
    public String toString() {
        return "PingDaoItemBean{" +
                "channel_name='" + channel_name + '\'' +
                ", relation_tag=" + relation_tag +
                '}';
    }
}
