package com.homechart.app.home.bean.pinglun;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/28.
 */

public class PingDataBean implements Serializable{

    private List<CommentListBean>  comment_list;
    private ItemInfoBean  item_info;


    public List<CommentListBean> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<CommentListBean> comment_list) {
        this.comment_list = comment_list;
    }

    public ItemInfoBean getItem_info() {
        return item_info;
    }

    public void setItem_info(ItemInfoBean item_info) {
        this.item_info = item_info;
    }

    public PingDataBean(List<CommentListBean> comment_list, ItemInfoBean item_info) {
        this.comment_list = comment_list;
        this.item_info = item_info;
    }

    @Override
    public String toString() {
        return "PingDataBean{" +
                "comment_list=" + comment_list +
                ", item_info=" + item_info +
                '}';
    }
}
