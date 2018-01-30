package com.homechart.app.home.bean.search;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/30.
 */

public class RecommendInfoItemDataBean implements Serializable {

    private String type;
    private String object_id;
    private String title;
    private String image_url;

    public RecommendInfoItemDataBean(String type, String object_id, String title, String image_url) {
        this.type = type;
        this.object_id = object_id;
        this.title = title;
        this.image_url = image_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public String toString() {
        return "RecommendInfoItemDataBean{" +
                "type='" + type + '\'' +
                ", object_id='" + object_id + '\'' +
                ", title='" + title + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }
}
