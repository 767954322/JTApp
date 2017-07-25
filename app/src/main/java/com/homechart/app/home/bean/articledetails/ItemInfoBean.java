package com.homechart.app.home.bean.articledetails;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/7/25.
 */

public class ItemInfoBean implements Serializable{

    private String item_id;
    private String description;
    private ItemImageBean image;

    public ItemInfoBean(String item_id, String description, ItemImageBean image) {
        this.item_id = item_id;
        this.description = description;
        this.image = image;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemImageBean getImage() {
        return image;
    }

    public void setImage(ItemImageBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ItemInfoBean{" +
                "item_id='" + item_id + '\'' +
                ", description='" + description + '\'' +
                ", image=" + image +
                '}';
    }
}
