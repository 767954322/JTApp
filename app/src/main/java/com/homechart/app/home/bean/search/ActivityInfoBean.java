package com.homechart.app.home.bean.search;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/17.
 */

public class ActivityInfoBean implements Serializable{

    private String type;
    private String object_id;
    private SmallImageBean small_image;
    private BigImageBean big_image;


    public ActivityInfoBean(String type, String object_id, SmallImageBean small_image, BigImageBean big_image) {
        this.type = type;
        this.object_id = object_id;
        this.small_image = small_image;
        this.big_image = big_image;
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

    public SmallImageBean getSmall_image() {
        return small_image;
    }

    public void setSmall_image(SmallImageBean small_image) {
        this.small_image = small_image;
    }

    public BigImageBean getBig_image() {
        return big_image;
    }

    public void setBig_image(BigImageBean big_image) {
        this.big_image = big_image;
    }

    @Override
    public String toString() {
        return "ActivityInfoBean{" +
                "type='" + type + '\'' +
                ", object_id='" + object_id + '\'' +
                ", small_image=" + small_image +
                ", big_image=" + big_image +
                '}';
    }
}
