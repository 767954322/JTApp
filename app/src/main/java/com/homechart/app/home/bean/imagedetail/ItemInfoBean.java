package com.homechart.app.home.bean.imagedetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/27.
 */

public class ItemInfoBean implements Serializable{

    private String item_id;
    private String description;
    private String tag;
    private String add_time;
    private String is_liked;
    private String activity_id;
    private String is_collected;
    private String title;
    private String from_domain;
    private String from_url;
    private String is_product;
    private String price;
    private String get_way;
    private ItemInfoImageBean image;


    public ItemInfoBean(String item_id, String description, String tag, String add_time, String is_liked, String activity_id, String is_collected, String title, String from_domain, String from_url, String is_product, String price, String get_way, ItemInfoImageBean image) {
        this.item_id = item_id;
        this.description = description;
        this.tag = tag;
        this.add_time = add_time;
        this.is_liked = is_liked;
        this.activity_id = activity_id;
        this.is_collected = is_collected;
        this.title = title;
        this.from_domain = from_domain;
        this.from_url = from_url;
        this.is_product = is_product;
        this.price = price;
        this.get_way = get_way;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(String is_liked) {
        this.is_liked = is_liked;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(String is_collected) {
        this.is_collected = is_collected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrom_domain() {
        return from_domain;
    }

    public void setFrom_domain(String from_domain) {
        this.from_domain = from_domain;
    }

    public String getFrom_url() {
        return from_url;
    }

    public void setFrom_url(String from_url) {
        this.from_url = from_url;
    }

    public String getIs_product() {
        return is_product;
    }

    public void setIs_product(String is_product) {
        this.is_product = is_product;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGet_way() {
        return get_way;
    }

    public void setGet_way(String get_way) {
        this.get_way = get_way;
    }

    public ItemInfoImageBean getImage() {
        return image;
    }

    public void setImage(ItemInfoImageBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ItemInfoBean{" +
                "item_id='" + item_id + '\'' +
                ", description='" + description + '\'' +
                ", tag='" + tag + '\'' +
                ", add_time='" + add_time + '\'' +
                ", is_liked='" + is_liked + '\'' +
                ", activity_id='" + activity_id + '\'' +
                ", is_collected='" + is_collected + '\'' +
                ", title='" + title + '\'' +
                ", from_domain='" + from_domain + '\'' +
                ", from_url='" + from_url + '\'' +
                ", is_product='" + is_product + '\'' +
                ", price='" + price + '\'' +
                ", get_way='" + get_way + '\'' +
                ", image=" + image +
                '}';
    }
}
