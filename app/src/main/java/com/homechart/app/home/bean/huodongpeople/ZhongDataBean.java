package com.homechart.app.home.bean.huodongpeople;

import android.widget.ListView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/1/17.
 */

public class ZhongDataBean implements Serializable{


    private List<ZhongItemDataBean> prize_list;

    public ZhongDataBean(List<ZhongItemDataBean> prize_list) {
        this.prize_list = prize_list;
    }

    public List<ZhongItemDataBean> getPrize_list() {
        return prize_list;
    }

    public void setPrize_list(List<ZhongItemDataBean> prize_list) {
        this.prize_list = prize_list;
    }

    @Override
    public String toString() {
        return "ZhongDataBean{" +
                "prize_list=" + prize_list +
                '}';
    }
}
