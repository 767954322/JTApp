package com.homechart.app.home.bean.msgdingyue;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/1/8.
 */

public class MsgDingYue implements Serializable {

    private List<DingYueItemBean>  notice_list;

    public MsgDingYue(List<DingYueItemBean> notice_list) {
        this.notice_list = notice_list;
    }

    public List<DingYueItemBean> getNotice_list() {
        return notice_list;
    }

    public void setNotice_list(List<DingYueItemBean> notice_list) {
        this.notice_list = notice_list;
    }

    @Override
    public String toString() {
        return "MsgDingYue{" +
                "notice_list=" + notice_list +
                '}';
    }
}
