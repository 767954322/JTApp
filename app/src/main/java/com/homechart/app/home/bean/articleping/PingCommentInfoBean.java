package com.homechart.app.home.bean.articleping;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/26.
 */

public class PingCommentInfoBean implements Serializable {

    private String comment_id;
    private String content;
    private String add_time;
    private int is_liked;
    private PingUserInfoBean user_info;
    private PingReplyCommentBean reply_comment;

    public PingCommentInfoBean(String comment_id, String content, String add_time, int is_liked, PingUserInfoBean user_info, PingReplyCommentBean reply_comment) {
        this.comment_id = comment_id;
        this.content = content;
        this.add_time = add_time;
        this.is_liked = is_liked;
        this.user_info = user_info;
        this.reply_comment = reply_comment;
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

    public int getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(int is_liked) {
        this.is_liked = is_liked;
    }

    public PingUserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(PingUserInfoBean user_info) {
        this.user_info = user_info;
    }

    public PingReplyCommentBean getReply_comment() {
        return reply_comment;
    }

    public void setReply_comment(PingReplyCommentBean reply_comment) {
        this.reply_comment = reply_comment;
    }

    @Override
    public String toString() {
        return "PingCommentInfoBean{" +
                "comment_id='" + comment_id + '\'' +
                ", content='" + content + '\'' +
                ", add_time='" + add_time + '\'' +
                ", is_liked=" + is_liked +
                ", user_info=" + user_info +
                ", reply_comment=" + reply_comment +
                '}';
    }
}
