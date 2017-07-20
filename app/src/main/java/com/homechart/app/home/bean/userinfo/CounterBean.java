package com.homechart.app.home.bean.userinfo;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/7.
 */

public class CounterBean implements Serializable{

    private int fans_num;
    private int follow_num;
    private int collect_single_num;
    private int collect_article_num;
    private int single_num;
    private int article_num;

    public CounterBean(int fans_num, int follow_num, int collect_single_num, int collect_article_num, int single_num, int article_num) {
        this.fans_num = fans_num;
        this.follow_num = follow_num;
        this.collect_single_num = collect_single_num;
        this.collect_article_num = collect_article_num;
        this.single_num = single_num;
        this.article_num = article_num;
    }

    public int getFans_num() {
        return fans_num;
    }

    public void setFans_num(int fans_num) {
        this.fans_num = fans_num;
    }

    public int getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(int follow_num) {
        this.follow_num = follow_num;
    }

    public int getCollect_single_num() {
        return collect_single_num;
    }

    public void setCollect_single_num(int collect_single_num) {
        this.collect_single_num = collect_single_num;
    }

    public int getCollect_article_num() {
        return collect_article_num;
    }

    public void setCollect_article_num(int collect_article_num) {
        this.collect_article_num = collect_article_num;
    }

    public int getSingle_num() {
        return single_num;
    }

    public void setSingle_num(int single_num) {
        this.single_num = single_num;
    }

    public int getArticle_num() {
        return article_num;
    }

    public void setArticle_num(int article_num) {
        this.article_num = article_num;
    }

    @Override
    public String toString() {
        return "CounterBean{" +
                "fans_num=" + fans_num +
                ", follow_num=" + follow_num +
                ", collect_single_num=" + collect_single_num +
                ", collect_article_num=" + collect_article_num +
                ", single_num=" + single_num +
                ", article_num=" + article_num +
                '}';
    }
}
