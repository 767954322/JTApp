package com.homechart.app.home.bean.imagedetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/10/11.
 */

public class ShiBieObjectItemBean implements Serializable {


    private String object_sign;
    private String x;
    private String y;
    private String width;
    private String height;
    private String category_id;
    private String category_name;

    public ShiBieObjectItemBean(String object_sign, String x, String y, String width, String height, String category_id, String category_name) {
        this.object_sign = object_sign;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.category_id = category_id;
        this.category_name = category_name;
    }

    public String getObject_sign() {
        return object_sign;
    }

    public void setObject_sign(String object_sign) {
        this.object_sign = object_sign;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
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
        return "ShiBieObjectItemBean{" +
                "object_sign='" + object_sign + '\'' +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", category_id='" + category_id + '\'' +
                ", category_name='" + category_name + '\'' +
                '}';
    }
}
