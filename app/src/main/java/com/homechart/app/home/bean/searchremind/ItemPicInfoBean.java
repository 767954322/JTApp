package com.homechart.app.home.bean.searchremind;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/3/19.
 */

public class ItemPicInfoBean implements Serializable {

    private String image_url;
    private String item_id;

    public ItemPicInfoBean(String image_url, String item_id) {
        this.image_url = image_url;
        this.item_id = item_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    @Override
    public String toString() {
        return "ItemPicInfoBean{" +
                "image_url='" + image_url + '\'' +
                ", item_id='" + item_id + '\'' +
                '}';
    }
}
