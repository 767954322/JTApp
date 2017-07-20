package com.homechart.app.home.bean.cailike;

/**
 * Created by gumenghao on 17/6/29.
 */

public class LikeDataBean {

    private LikeDataArrayBean data;

    public LikeDataBean(LikeDataArrayBean data) {
        this.data = data;
    }

    public LikeDataArrayBean getData() {
        return data;
    }

    public void setData(LikeDataArrayBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LikeDataBean{" +
                "data=" + data +
                '}';
    }
}
