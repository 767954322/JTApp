package com.homechart.app.home.bean.zhuanjihuodong;

import com.homechart.app.home.bean.huodong.ItemActivityDataBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/29.
 */

public class HuoListDataBean implements Serializable {

    private List<ItemHuoDataBean>  album_list;

    public HuoListDataBean(List<ItemHuoDataBean> album_list) {
        this.album_list = album_list;
    }

    public List<ItemHuoDataBean> getAlbum_list() {
        return album_list;
    }

    public void setAlbum_list(List<ItemHuoDataBean> album_list) {
        this.album_list = album_list;
    }

    @Override
    public String toString() {
        return "HuoListDataBean{" +
                "album_list=" + album_list +
                '}';
    }
}
