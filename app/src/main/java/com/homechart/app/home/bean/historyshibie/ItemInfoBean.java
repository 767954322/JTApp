package com.homechart.app.home.bean.historyshibie;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/9/26.
 */

public class ItemInfoBean implements Serializable {

    private String user_id;
    private String image_id;
    private String image_url;
    private String big_image_url;

    public ItemInfoBean(String user_id, String image_id, String image_url, String big_image_url) {
        this.user_id = user_id;
        this.image_id = image_id;
        this.image_url = image_url;
        this.big_image_url = big_image_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getBig_image_url() {
        return big_image_url;
    }

    public void setBig_image_url(String big_image_url) {
        this.big_image_url = big_image_url;
    }

    @Override
    public String toString() {
        return "ItemInfoBean{" +
                "user_id='" + user_id + '\'' +
                ", image_id='" + image_id + '\'' +
                ", image_url='" + image_url + '\'' +
                ", big_image_url='" + big_image_url + '\'' +
                '}';
    }
}
