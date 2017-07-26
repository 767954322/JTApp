package com.homechart.app.home.bean.articleping;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/26.
 */

public class PingArticleInfoBean implements Serializable {

    private String article_id;
    private String user_id;

    public PingArticleInfoBean(String article_id, String user_id) {
        this.article_id = article_id;
        this.user_id = user_id;
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

    @Override
    public String toString() {
        return "PingArticleInfoBean{" +
                "article_id='" + article_id + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
