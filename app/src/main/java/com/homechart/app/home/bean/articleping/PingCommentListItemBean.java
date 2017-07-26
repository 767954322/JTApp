package com.homechart.app.home.bean.articleping;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/26.
 */

public class PingCommentListItemBean implements Serializable{


    private PingCommentInfoBean comment_info;

    public PingCommentListItemBean(PingCommentInfoBean comment_info) {
        this.comment_info = comment_info;
    }

    public PingCommentInfoBean getComment_info() {
        return comment_info;
    }

    public void setComment_info(PingCommentInfoBean comment_info) {
        this.comment_info = comment_info;
    }

    @Override
    public String toString() {
        return "PingCommentListItemBean{" +
                "comment_info=" + comment_info +
                '}';
    }
}
