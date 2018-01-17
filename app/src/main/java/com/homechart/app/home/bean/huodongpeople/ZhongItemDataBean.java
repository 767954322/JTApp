package com.homechart.app.home.bean.huodongpeople;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/17.
 */

public class ZhongItemDataBean implements Serializable {

    private ZhongPrizeInfoBean prize_info;

    public ZhongItemDataBean(ZhongPrizeInfoBean prize_info) {
        this.prize_info = prize_info;
    }

    public ZhongPrizeInfoBean getPrize_info() {
        return prize_info;
    }

    public void setPrize_info(ZhongPrizeInfoBean prize_info) {
        this.prize_info = prize_info;
    }

    @Override
    public String toString() {
        return "ZhongItemDataBean{" +
                "prize_info=" + prize_info +
                '}';
    }
}
