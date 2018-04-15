package com.homechart.app.home.bean.faxianpingdao;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/4/15.
 */

public class PingDaoUrlItemBean implements Serializable {

    private String tag_name;
    private String image_url;


    public PingDaoUrlItemBean(String tag_name, String image_url) {
        this.tag_name = tag_name;
        this.image_url = image_url;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public String toString() {
        return "PingDaoUrlItemBean{" +
                "tag_name='" + tag_name + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }
}

