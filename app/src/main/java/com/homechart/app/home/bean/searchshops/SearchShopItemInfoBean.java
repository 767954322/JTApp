package com.homechart.app.home.bean.searchshops;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/9/15.
 */

public class SearchShopItemInfoBean implements Serializable {

    private String spu_id;
    private String title;
    private String price;
    private String buy_url;
    private String source;
    private String source_name;
    private String product_id;
    private String is_collected;
    private SearchShopItemInfoImageBean image;

    public SearchShopItemInfoBean(String spu_id, String title, String price, String buy_url, String source, String source_name, String product_id, String is_collected, SearchShopItemInfoImageBean image) {
        this.spu_id = spu_id;
        this.title = title;
        this.price = price;
        this.buy_url = buy_url;
        this.source = source;
        this.source_name = source_name;
        this.product_id = product_id;
        this.is_collected = is_collected;
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

    public String getBuy_url() {
        return buy_url;
    }

    public void setBuy_url(String buy_url) {
        this.buy_url = buy_url;
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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(String is_collected) {
        this.is_collected = is_collected;
    }

    public SearchShopItemInfoImageBean getImage() {
        return image;
    }

    public void setImage(SearchShopItemInfoImageBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "SearchShopItemInfoBean{" +
                "spu_id='" + spu_id + '\'' +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", buy_url='" + buy_url + '\'' +
                ", source='" + source + '\'' +
                ", source_name='" + source_name + '\'' +
                ", product_id='" + product_id + '\'' +
                ", is_collected='" + is_collected + '\'' +
                ", image=" + image +
                '}';
    }
}
