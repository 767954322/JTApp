package com.homechart.app.home.bean.articlelike;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/7/26.
 */

public class ArticleLikeBean implements Serializable{

    private List<ArticleLikeItemBean> article_list;

    public ArticleLikeBean(List<ArticleLikeItemBean> article_list) {
        this.article_list = article_list;
    }

    public List<ArticleLikeItemBean> getArticle_list() {
        return article_list;
    }

    public void setArticle_list(List<ArticleLikeItemBean> article_list) {
        this.article_list = article_list;
    }

    @Override
    public String toString() {
        return "ArticleLikeBean{" +
                "article_list=" + article_list +
                '}';
    }
}
