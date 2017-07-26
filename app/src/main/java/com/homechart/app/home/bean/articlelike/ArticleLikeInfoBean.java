package com.homechart.app.home.bean.articlelike;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/26.
 */

public class ArticleLikeInfoBean implements Serializable {

    private String article_id;
    private String user_id;
    private String title;
    private String summary;
    private String view_num;
    private ArticleLikeImageBean image;

    public ArticleLikeInfoBean(String article_id, String user_id, String title, String summary, String view_num, ArticleLikeImageBean image) {
        this.article_id = article_id;
        this.user_id = user_id;
        this.title = title;
        this.summary = summary;
        this.view_num = view_num;
        this.image = image;
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

    public ArticleLikeImageBean getImage() {
        return image;
    }

    public void setImage(ArticleLikeImageBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ArticleLikeInfoBean{" +
                "article_id='" + article_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", view_num='" + view_num + '\'' +
                ", image=" + image +
                '}';
    }
}
