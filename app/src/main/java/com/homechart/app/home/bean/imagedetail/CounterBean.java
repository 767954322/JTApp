package com.homechart.app.home.bean.imagedetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/27.
 */

public class CounterBean implements Serializable{

    private int like_num;
    private int collect_num;
    private int comment_num;
    private int share_num;

    public CounterBean(int like_num, int collect_num, int comment_num, int share_num) {
        this.like_num = like_num;
        this.collect_num = collect_num;
        this.comment_num = comment_num;
        this.share_num = share_num;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public int getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(int collect_num) {
        this.collect_num = collect_num;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public int getShare_num() {
        return share_num;
    }

    public void setShare_num(int share_num) {
        this.share_num = share_num;
    }

    @Override
    public String toString() {
        return "CounterBean{" +
                "like_num=" + like_num +
                ", collect_num=" + collect_num +
                ", comment_num=" + comment_num +
                ", share_num=" + share_num +
                '}';
    }
}
