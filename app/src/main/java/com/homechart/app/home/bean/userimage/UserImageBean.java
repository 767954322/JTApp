package com.homechart.app.home.bean.userimage;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/20.
 */

public class UserImageBean implements Serializable {

   private ImageListBean data;

    public UserImageBean(ImageListBean data) {
        this.data = data;
    }

    public ImageListBean getData() {
        return data;
    }

    public void setData(ImageListBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UserImageBean{" +
                "data=" + data +
                '}';
    }
}
