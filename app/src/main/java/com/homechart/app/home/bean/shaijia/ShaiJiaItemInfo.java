package com.homechart.app.home.bean.shaijia;

import com.homechart.app.home.bean.shoucang.ShouCangItemImageBean;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/8.
 */

public class ShaiJiaItemInfo implements Serializable {

    private String item_id;
    private String tag;
    private String collect_num;
    private String comment_num;
    private String is_collected;
    private String add_time;
    private ShaiJiaItemImageBean image;


    public ShaiJiaItemInfo(String item_id, String tag, String collect_num, String comment_num, String is_collected, String add_time, ShaiJiaItemImageBean image) {
        this.item_id = item_id;
        this.tag = tag;
        this.collect_num = collect_num;
        this.comment_num = comment_num;
        this.is_collected = is_collected;
        this.add_time = add_time;
        this.image = image;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(String collect_num) {
        this.collect_num = collect_num;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(String is_collected) {
        this.is_collected = is_collected;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public ShaiJiaItemImageBean getImage() {
        return image;
    }

    public void setImage(ShaiJiaItemImageBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ShaiJiaItemInfo{" +
                "item_id='" + item_id + '\'' +
                ", tag='" + tag + '\'' +
                ", collect_num='" + collect_num + '\'' +
                ", comment_num='" + comment_num + '\'' +
                ", is_collected='" + is_collected + '\'' +
                ", add_time='" + add_time + '\'' +
                ", image=" + image +
                '}';
    }
}
