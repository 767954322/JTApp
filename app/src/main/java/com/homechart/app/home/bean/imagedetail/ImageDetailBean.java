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
    private UserInfoBean user_info;
    private List<ColorInfoBean> color_info;
    private ActivityItemInfoDataBean activity_info;
    private List<ShiBieItemBean> object_list;


    public ImageDetailBean(ItemInfoBean item_info, CounterBean counter, UserInfoBean user_info, List<ColorInfoBean> color_info, ActivityItemInfoDataBean activity_info, List<ShiBieItemBean> object_list) {
        this.item_info = item_info;
        this.counter = counter;
        this.user_info = user_info;
        this.color_info = color_info;
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
                ", user_info=" + user_info +
                ", color_info=" + color_info +
                ", activity_info=" + activity_info +
                ", object_list=" + object_list +
                '}';
    }
}
