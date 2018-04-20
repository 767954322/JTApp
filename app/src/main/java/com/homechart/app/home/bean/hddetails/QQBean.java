package com.homechart.app.home.bean.hddetails;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/4/20.
 */

public class QQBean implements Serializable {

    private  String image_url;
    private  String title;
    private  String description;

    public QQBean(String image_url, String title, String description) {
        this.image_url = image_url;
        this.title = title;
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "QQBean{" +
                "image_url='" + image_url + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
