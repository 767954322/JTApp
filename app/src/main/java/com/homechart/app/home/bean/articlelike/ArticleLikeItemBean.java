package com.homechart.app.home.bean.articlelike;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/26.
 */

public class ArticleLikeItemBean implements Serializable {

    private ArticleLikeInfoBean article_info;

    public ArticleLikeItemBean(ArticleLikeInfoBean article_info) {
        this.article_info = article_info;
    }

    public ArticleLikeInfoBean getArticle_info() {
        return article_info;
    }

    public void setArticle_info(ArticleLikeInfoBean article_info) {
        this.article_info = article_info;
    }

    @Override
    public String toString() {
        return "ArticleLikeItemBean{" +
                "article_info=" + article_info +
                '}';
    }
}
