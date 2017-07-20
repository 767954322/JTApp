package com.homechart.app.home.bean.pinglun;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/28.
 */

public class CommentListBean implements Serializable {

    private CommentInfoBean comment_info;

    public CommentListBean(CommentInfoBean comment_info) {
        this.comment_info = comment_info;
    }

    public CommentInfoBean getComment_info() {
        return comment_info;
    }

    public void setComment_info(CommentInfoBean comment_info) {
        this.comment_info = comment_info;
    }

    @Override
    public String toString() {
        return "CommentListBean{" +
                "comment_info=" + comment_info +
                '}';
    }
}
