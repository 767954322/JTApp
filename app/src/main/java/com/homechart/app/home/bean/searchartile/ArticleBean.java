package com.homechart.app.home.bean.searchartile;

import com.homechart.app.home.bean.articledetails.ItemDetailsBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/7/19.
 */

public class ArticleBean implements Serializable {

    private ArticleInfoBean article_info;
    private List<ItemDetailsBean> item_list;


    public ArticleBean(ArticleInfoBean article_info, List<ItemDetailsBean> item_list) {
        this.article_info = article_info;
        this.item_list = item_list;
    }

    public ArticleInfoBean getArticle_info() {
        return article_info;
    }

    public void setArticle_info(ArticleInfoBean article_info) {
        this.article_info = article_info;
    }

    public List<ItemDetailsBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ItemDetailsBean> item_list) {
        this.item_list = item_list;
    }

    @Override
    public String toString() {
        return "ArticleBean{" +
                "article_info=" + article_info +
                ", item_list=" + item_list +
                '}';
    }
}
