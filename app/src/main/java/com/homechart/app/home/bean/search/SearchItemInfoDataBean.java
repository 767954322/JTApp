package com.homechart.app.home.bean.search;

import com.homechart.app.home.bean.shouye.SYDataObjectImgBean;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/15.
 */

public class SearchItemInfoDataBean implements Serializable{


    private String item_id;
    private String tag;
    private SearchDataObjectImgBean image;

    public SearchItemInfoDataBean(String item_id, String tag, SearchDataObjectImgBean image) {
        this.item_id = item_id;
        this.tag = tag;
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

    public SearchDataObjectImgBean getImage() {
        return image;
    }

    public void setImage(SearchDataObjectImgBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "SearchItemInfoDataBean{" +
                "item_id='" + item_id + '\'' +
                ", tag='" + tag + '\'' +
                ", image=" + image +
                '}';
    }
}
