package com.homechart.app.home.bean.msgguanzhu;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/8/17.
 */

public class MsgGZBean implements Serializable{

    private List<MsgNoticeBean>  notice_list;

    public MsgGZBean(List<MsgNoticeBean> notice_list) {
        this.notice_list = notice_list;
    }

    public List<MsgNoticeBean> getNotice_list() {
        return notice_list;
    }

    public void setNotice_list(List<MsgNoticeBean> notice_list) {
        this.notice_list = notice_list;
    }

    @Override
    public String toString() {
        return "MsgGZBean{" +
                "notice_list=" + notice_list +
                '}';
    }
}
