package com.homechart.app.home.bean.searchartile;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/7/19.
 */

public class ArticleListBean implements Serializable{

    private List<ArticleBean> article_list;


    public ArticleListBean(List<ArticleBean> article_list) {
        this.article_list = article_list;
    }

    public List<ArticleBean> getArticle_list() {
        return article_list;
    }

    public void setArticle_list(List<ArticleBean> article_list) {
        this.article_list = article_list;
    }

    @Override
    public String toString() {
        return "ArticleListBean{" +
                "article_list=" + article_list +
                '}';
    }
}
