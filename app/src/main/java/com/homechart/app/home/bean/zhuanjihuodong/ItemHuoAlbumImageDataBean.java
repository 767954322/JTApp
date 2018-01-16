package com.homechart.app.home.bean.zhuanjihuodong;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/16.
 */

public class ItemHuoAlbumImageDataBean implements Serializable {

   private String image_id;
   private String img0;

    public ItemHuoAlbumImageDataBean(String image_id, String img0) {
        this.image_id = image_id;
        this.img0 = img0;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getImg0() {
        return img0;
    }

    public void setImg0(String img0) {
        this.img0 = img0;
    }

    @Override
    public String toString() {
        return "ItemHuoAlbumImageDataBean{" +
                "image_id='" + image_id + '\'' +
                ", img0='" + img0 + '\'' +
                '}';
    }
}
