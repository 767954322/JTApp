package com.homechart.app.home.bean.shoucangshop;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/9/12.
 */

public class SCItemShopInfoBean implements Serializable {

    private String spu_id;
    private String source;
    private String source_name;
    private String buy_url;
    private String price;
    private String title;
    private SCItemShopInfoImageBean image;

    public SCItemShopInfoBean(String spu_id, String source, String source_name, String buy_url, String price, String title, SCItemShopInfoImageBean image) {
        this.spu_id = spu_id;
        this.source = source;
        this.source_name = source_name;
        this.buy_url = buy_url;
        this.price = price;
        this.title = title;
        this.image = image;
    }

    public String getSpu_id() {
        return spu_id;
    }

    public void setSpu_id(String spu_id) {
        this.spu_id = spu_id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SCItemShopInfoImageBean getImage() {
        return image;
    }

    public void setImage(SCItemShopInfoImageBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "SCItemShopInfoBean{" +
                "spu_id='" + spu_id + '\'' +
                ", source='" + source + '\'' +
                ", source_name='" + source_name + '\'' +
                ", buy_url='" + buy_url + '\'' +
                ", price='" + price + '\'' +
                ", title='" + title + '\'' +
                ", image=" + image +
                '}';
    }
}
