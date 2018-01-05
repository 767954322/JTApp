package com.homechart.app.lingganji.common.entity.inspirationpics;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/4.
 */

public class InsPicItemInfoBean implements Serializable {

    private String item_id;
    private String tag;
    private String description;
    private String update_time;
    private InsPicItemInfoImageBean image;


    public InsPicItemInfoBean(String item_id, String tag, String description, String update_time, InsPicItemInfoImageBean image) {
        this.item_id = item_id;
        this.tag = tag;
        this.description = description;
        this.update_time = update_time;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public InsPicItemInfoImageBean getImage() {
        return image;
    }

    public void setImage(InsPicItemInfoImageBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "InsPicItemInfoBean{" +
                "item_id='" + item_id + '\'' +
                ", tag='" + tag + '\'' +
                ", description='" + description + '\'' +
                ", update_time='" + update_time + '\'' +
                ", image=" + image +
                '}';
    }
}
