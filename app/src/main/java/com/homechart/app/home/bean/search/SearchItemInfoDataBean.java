package com.homechart.app.home.bean.search;

import com.homechart.app.home.bean.shouye.SYDataObjectImgBean;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/15.
 */

public class SearchItemInfoDataBean implements Serializable{


    private String item_id;
    private String tag;
    private String description;
    private SearchDataObjectImgBean image;
    private String like_num;
    private String collect_num;
    private String comment_num;
    private String is_collected;

    public SearchItemInfoDataBean() {
    }

    public SearchItemInfoDataBean(String item_id, String tag, String description, SearchDataObjectImgBean image, String like_num, String collect_num, String comment_num, String is_collected) {
        this.item_id = item_id;
        this.tag = tag;
        this.description = description;
        this.image = image;
        this.like_num = like_num;
        this.collect_num = collect_num;
        this.comment_num = comment_num;
        this.is_collected = is_collected;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SearchDataObjectImgBean getImage() {
        return image;
    }

    public void setImage(SearchDataObjectImgBean image) {
        this.image = image;
    }

    public String getLike_num() {
        return like_num;
    }

    public void setLike_num(String like_num) {
        this.like_num = like_num;
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

    @Override
    public String toString() {
        return "SearchItemInfoDataBean{" +
                "item_id='" + item_id + '\'' +
                ", tag='" + tag + '\'' +
                ", description='" + description + '\'' +
                ", image=" + image +
                ", like_num='" + like_num + '\'' +
                ", collect_num='" + collect_num + '\'' +
                ", comment_num='" + comment_num + '\'' +
                ", is_collected='" + is_collected + '\'' +
                '}';
    }
}
