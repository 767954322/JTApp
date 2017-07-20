package com.homechart.app.home.bean.pinglun;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/28.
 */

public class CommentInfoBean implements Serializable{

    private String comment_id;
    private String content;
    private String add_time;
    private PingUserInfoBean user_info;
    private ReplyCommentBean reply_comment;

    public CommentInfoBean(String comment_id, String content, String add_time, PingUserInfoBean user_info, ReplyCommentBean reply_comment) {
        this.comment_id = comment_id;
        this.content = content;
        this.add_time = add_time;
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

    public PingUserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(PingUserInfoBean user_info) {
        this.user_info = user_info;
    }

    public ReplyCommentBean getReply_comment() {
        return reply_comment;
    }

    public void setReply_comment(ReplyCommentBean reply_comment) {
        this.reply_comment = reply_comment;
    }

    @Override
    public String toString() {
        return "CommentInfoBean{" +
                "comment_id='" + comment_id + '\'' +
                ", content='" + content + '\'' +
                ", add_time='" + add_time + '\'' +
                ", user_info=" + user_info +
                ", reply_comment=" + reply_comment +
                '}';
    }
}
