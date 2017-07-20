package com.homechart.app.home.bean.message;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/12.
 */

public class MessageBean implements Serializable{


    private List<ItemMessageBean>  notice_list;


    public MessageBean(List<ItemMessageBean> notice_list) {
        this.notice_list = notice_list;
    }

    public List<ItemMessageBean> getNotice_list() {
        return notice_list;
    }

    public void setNotice_list(List<ItemMessageBean> notice_list) {
        this.notice_list = notice_list;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "notice_list=" + notice_list +
                '}';
    }
}
