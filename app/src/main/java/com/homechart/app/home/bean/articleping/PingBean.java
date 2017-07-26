package com.homechart.app.home.bean.articleping;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/7/26.
 */

public class PingBean implements Serializable {

    private PingArticleInfoBean article_info;
    private List<PingCommentListItemBean> comment_list;

    public PingBean(PingArticleInfoBean article_info, List<PingCommentListItemBean> comment_list) {
        this.article_info = article_info;
        this.comment_list = comment_list;
    }

    public PingArticleInfoBean getArticle_info() {
        return article_info;
    }

    public void setArticle_info(PingArticleInfoBean article_info) {
        this.article_info = article_info;
    }

    public List<PingCommentListItemBean> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<PingCommentListItemBean> comment_list) {
        this.comment_list = comment_list;
    }

    @Override
    public String toString() {
        return "PingBean{" +
                "article_info=" + article_info +
                ", comment_list=" + comment_list +
                '}';
    }
}
