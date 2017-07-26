package com.homechart.app.home.bean.articledetails;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/25.
 */

public class ArticleInfoBean implements Serializable{

    private String article_id;
    private String user_id;
    private String title;
    private String summary;
    private String view_num;
    private String collect_num;
    private String comment_num;
    private String like_num;
    private String share_num;
    private String add_time;
    private int is_liked;
    private int is_collected;


    public ArticleInfoBean(String article_id, String user_id, String title, String summary, String view_num, String collect_num, String comment_num, String like_num, String share_num, String add_time, int is_liked, int is_collected) {
        this.article_id = article_id;
        this.user_id = user_id;
        this.title = title;
        this.summary = summary;
        this.view_num = view_num;
        this.collect_num = collect_num;
        this.comment_num = comment_num;
        this.like_num = like_num;
        this.share_num = share_num;
        this.add_time = add_time;
        this.is_liked = is_liked;
        this.is_collected = is_collected;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getView_num() {
        return view_num;
    }

    public void setView_num(String view_num) {
        this.view_num = view_num;
    }

    public String getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(String collect_num) {
        this.collect_num = collect_num;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getLike_num() {
        return like_num;
    }

    public void setLike_num(String like_num) {
        this.like_num = like_num;
    }

    public String getShare_num() {
        return share_num;
    }

    public void setShare_num(String share_num) {
        this.share_num = share_num;
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

    public int getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(int is_collected) {
        this.is_collected = is_collected;
    }

    @Override
    public String toString() {
        return "ArticleInfoBean{" +
                "article_id='" + article_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", view_num='" + view_num + '\'' +
                ", collect_num='" + collect_num + '\'' +
                ", comment_num='" + comment_num + '\'' +
                ", like_num='" + like_num + '\'' +
                ", share_num='" + share_num + '\'' +
                ", add_time='" + add_time + '\'' +
                ", is_liked=" + is_liked +
                ", is_collected=" + is_collected +
                '}';
    }
}
