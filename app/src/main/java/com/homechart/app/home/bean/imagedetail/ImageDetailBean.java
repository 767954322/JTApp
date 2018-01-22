package com.homechart.app.home.bean.imagedetail;

import com.homechart.app.home.bean.fabu.ActivityItemInfoDataBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/27.
 */

public class ImageDetailBean implements Serializable {

    private ItemInfoBean item_info;
    private CounterBean counter;
    private AlbumInfoBean album_info;
    private UserInfoBean user_info;
    private List<ColorInfoBean> color_info;
    private List<RelatedAlbumsBean> related_albums;
    private ActivityItemInfoDataBean activity_info;
    private List<ShiBieItemBean> object_list;


    public ImageDetailBean(ItemInfoBean item_info, CounterBean counter, AlbumInfoBean album_info, UserInfoBean user_info, List<ColorInfoBean> color_info, List<RelatedAlbumsBean> related_albums, ActivityItemInfoDataBean activity_info, List<ShiBieItemBean> object_list) {
        this.item_info = item_info;
        this.counter = counter;
        this.album_info = album_info;
        this.user_info = user_info;
        this.color_info = color_info;
        this.related_albums = related_albums;
        this.activity_info = activity_info;
        this.object_list = object_list;
    }

    public ItemInfoBean getItem_info() {
        return item_info;
    }

    public void setItem_info(ItemInfoBean item_info) {
        this.item_info = item_info;
    }

    public CounterBean getCounter() {
        return counter;
    }

    public void setCounter(CounterBean counter) {
        this.counter = counter;
    }

    public AlbumInfoBean getAlbum_info() {
        return album_info;
    }

    public void setAlbum_info(AlbumInfoBean album_info) {
        this.album_info = album_info;
    }

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public List<ColorInfoBean> getColor_info() {
        return color_info;
    }

    public void setColor_info(List<ColorInfoBean> color_info) {
        this.color_info = color_info;
    }

    public List<RelatedAlbumsBean> getRelated_albums() {
        return related_albums;
    }

    public void setRelated_albums(List<RelatedAlbumsBean> related_albums) {
        this.related_albums = related_albums;
    }

    public ActivityItemInfoDataBean getActivity_info() {
        return activity_info;
    }

    public void setActivity_info(ActivityItemInfoDataBean activity_info) {
        this.activity_info = activity_info;
    }

    public List<ShiBieItemBean> getObject_list() {
        return object_list;
    }

    public void setObject_list(List<ShiBieItemBean> object_list) {
        this.object_list = object_list;
    }

    @Override
    public String toString() {
        return "ImageDetailBean{" +
                "item_info=" + item_info +
                ", counter=" + counter +
                ", album_info=" + album_info +
                ", user_info=" + user_info +
                ", color_info=" + color_info +
                ", related_albums=" + related_albums +
                ", activity_info=" + activity_info +
                ", object_list=" + object_list +
                '}';
    }
}
