package com.homechart.app.home.bean.userinfo;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/7.
 */

public class CounterBean implements Serializable{

    private int fans_num;
    private int follow_num;
    private int collect_product_num;
    private int collect_album_num;
    private int album_num;
    private int item_num;


    public CounterBean(int fans_num, int follow_num, int collect_product_num, int collect_album_num, int album_num, int item_num) {
        this.fans_num = fans_num;
        this.follow_num = follow_num;
        this.collect_product_num = collect_product_num;
        this.collect_album_num = collect_album_num;
        this.album_num = album_num;
        this.item_num = item_num;
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

    public int getCollect_product_num() {
        return collect_product_num;
    }

    public void setCollect_product_num(int collect_product_num) {
        this.collect_product_num = collect_product_num;
    }

    public int getCollect_album_num() {
        return collect_album_num;
    }

    public void setCollect_album_num(int collect_album_num) {
        this.collect_album_num = collect_album_num;
    }

    public int getAlbum_num() {
        return album_num;
    }

    public void setAlbum_num(int album_num) {
        this.album_num = album_num;
    }

    public int getItem_num() {
        return item_num;
    }

    public void setItem_num(int item_num) {
        this.item_num = item_num;
    }

    @Override
    public String toString() {
        return "CounterBean{" +
                "fans_num=" + fans_num +
                ", follow_num=" + follow_num +
                ", collect_product_num=" + collect_product_num +
                ", collect_album_num=" + collect_album_num +
                ", album_num=" + album_num +
                ", item_num=" + item_num +
                '}';
    }
}
