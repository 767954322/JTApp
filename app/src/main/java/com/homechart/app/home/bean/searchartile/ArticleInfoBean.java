package com.homechart.app.home.bean.searchartile;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/19.
 */

public class ArticleInfoBean implements Serializable {

    private String article_id;
    private String title;
    private String view_num;
    private String summary;
    private ArticleInfoImageBean image;

    public ArticleInfoBean(String article_id, String title, String view_num, String summary, ArticleInfoImageBean image) {
        this.article_id = article_id;
        this.title = title;
        this.view_num = view_num;
        this.summary = summary;
        this.image = image;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getView_num() {
        return view_num;
    }

    public void setView_num(String view_num) {
        this.view_num = view_num;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ArticleInfoImageBean getImage() {
        return image;
    }

    public void setImage(ArticleInfoImageBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ArticleInfoBean{" +
                "article_id='" + article_id + '\'' +
                ", title='" + title + '\'' +
                ", view_num='" + view_num + '\'' +
                ", summary='" + summary + '\'' +
                ", image=" + image +
                '}';
    }
}
