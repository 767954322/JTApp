package com.homechart.app.home.bean.articleping;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/26.
 */

public class PingReplyCommentBean implements Serializable {


    private String comment_id;
    private String content;
    private String add_time;
    private PingReplyCommentUserinfoBean user_info;

    public PingReplyCommentBean(String comment_id, String content, String add_time, PingReplyCommentUserinfoBean user_info) {
        this.comment_id = comment_id;
        this.content = content;
        this.add_time = add_time;
        this.user_info = user_info;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public PingReplyCommentUserinfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(PingReplyCommentUserinfoBean user_info) {
        this.user_info = user_info;
    }

    @Override
    public String toString() {
        return "PingReplyCommentBean{" +
                "comment_id='" + comment_id + '\'' +
                ", content='" + content + '\'' +
                ", add_time='" + add_time + '\'' +
                ", user_info=" + user_info +
                '}';
    }
}
