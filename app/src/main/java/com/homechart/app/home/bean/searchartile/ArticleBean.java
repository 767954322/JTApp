package com.homechart.app.home.bean.searchartile;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/19.
 */

public class ArticleBean implements Serializable {

    private ArticleInfoBean article_info;


    public ArticleBean(ArticleInfoBean article_info) {
        this.article_info = article_info;
    }

    public ArticleInfoBean getArticle_info() {
        return article_info;
    }

    public void setArticle_info(ArticleInfoBean article_info) {
        this.article_info = article_info;
    }

    @Override
    public String toString() {
        return "ArticleBean{" +
                "article_info=" + article_info +
                '}';
    }
}
