package com.homechart.app.home.bean.shouye;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/14.
 */

public class SYActivityInfoBean implements Serializable{

    private String id;
    private String title;
    private SYActivityInfoImgBean image;


    public SYActivityInfoBean(String id, String title, SYActivityInfoImgBean image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SYActivityInfoImgBean getImage() {
        return image;
    }

    public void setImage(SYActivityInfoImgBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "SYActivityInfoBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", image=" + image +
                '}';
    }
}
