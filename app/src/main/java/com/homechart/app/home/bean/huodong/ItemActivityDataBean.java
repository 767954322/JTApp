package com.homechart.app.home.bean.huodong;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/29.
 */

public class ItemActivityDataBean implements Serializable{

    private ItemInfoBean item_info;
    private UserInfoBean user_info;
    private List<ColorInfoBean> color_info;

    public ItemActivityDataBean(ItemInfoBean item_info, UserInfoBean user_info, List<ColorInfoBean> color_info) {
        this.item_info = item_info;
        this.user_info = user_info;
        this.color_info = color_info;
    }

    public ItemInfoBean getItem_info() {
        return item_info;
    }

    public void setItem_info(ItemInfoBean item_info) {
        this.item_info = item_info;
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

    @Override
    public String toString() {
        return "ItemActivityDataBean{" +
                "item_info=" + item_info +
                ", user_info=" + user_info +
                ", color_info=" + color_info +
                '}';
    }
}
