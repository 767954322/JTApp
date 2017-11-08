package com.homechart.app.home.bean.newhistory;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/11/6.
 */

public class HistoryDataBean implements Serializable {


    private String image_id;
    private String image_url;
    private String big_image_url;

    public HistoryDataBean(String image_id, String image_url, String big_image_url) {
        this.image_id = image_id;
        this.image_url = image_url;
        this.big_image_url = big_image_url;
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
        return "HistoryDataBean{" +
                "image_id='" + image_id + '\'' +
                ", image_url='" + image_url + '\'' +
                ", big_image_url='" + big_image_url + '\'' +
                '}';
    }
}
