package com.homechart.app.home.bean.searchfservice;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/9/14.
 */

public class SearchSObjectInfoBean implements Serializable {

    private float x;
    private float y;
    private float width;
    private float height;
    private int category_id;
    private String category_name;

    public SearchSObjectInfoBean(float x, float y, float width, float height, int category_id, String category_name) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.category_id = category_id;
        this.category_name = category_name;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @Override
    public String toString() {
        return "SearchSObjectInfoBean{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", category_id=" + category_id +
                ", category_name='" + category_name + '\'' +
                '}';
    }
}
