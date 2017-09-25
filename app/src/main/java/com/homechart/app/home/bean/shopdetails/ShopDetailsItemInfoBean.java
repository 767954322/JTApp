package com.homechart.app.home.bean.shopdetails;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/9/13.
 */

public class ShopDetailsItemInfoBean implements Serializable {


    private String spu_id;
    private String title;
    private String price;
    private String collect_num;
    private String is_collected;
    private String source;
    private String source_name;
    private String buy_url;
    private ShopDetailsItemImageBean image;


    public ShopDetailsItemInfoBean(String spu_id, String title, String price, String collect_num, String is_collected, String source, String source_name, String buy_url, ShopDetailsItemImageBean image) {
        this.spu_id = spu_id;
        this.title = title;
        this.price = price;
        this.collect_num = collect_num;
        this.is_collected = is_collected;
        this.source = source;
        this.source_name = source_name;
        this.buy_url = buy_url;
        this.image = image;
    }

    public String getSpu_id() {
        return spu_id;
    }

    public void setSpu_id(String spu_id) {
        this.spu_id = spu_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(String collect_num) {
        this.collect_num = collect_num;
    }

    public String getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(String is_collected) {
        this.is_collected = is_collected;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getBuy_url() {
        return buy_url;
    }

    public void setBuy_url(String buy_url) {
        this.buy_url = buy_url;
    }

    public ShopDetailsItemImageBean getImage() {
        return image;
    }

    public void setImage(ShopDetailsItemImageBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ShopDetailsItemInfoBean{" +
                "spu_id='" + spu_id + '\'' +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", collect_num='" + collect_num + '\'' +
                ", is_collected='" + is_collected + '\'' +
                ", source='" + source + '\'' +
                ", source_name='" + source_name + '\'' +
                ", buy_url='" + buy_url + '\'' +
                ", image=" + image +
                '}';
    }
}
