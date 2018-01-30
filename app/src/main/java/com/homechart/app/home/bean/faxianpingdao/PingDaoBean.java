package com.homechart.app.home.bean.faxianpingdao;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/1/30.
 */

public class PingDaoBean implements Serializable{

    private List<PingDaoItemBean> channel_list;

    public PingDaoBean(List<PingDaoItemBean> channel_list) {
        this.channel_list = channel_list;
    }

    public List<PingDaoItemBean> getChannel_list() {
        return channel_list;
    }

    public void setChannel_list(List<PingDaoItemBean> channel_list) {
        this.channel_list = channel_list;
    }

    @Override
    public String toString() {
        return "PingDaoBean{" +
                "channel_list=" + channel_list +
                '}';
    }
}
