package com.homechart.app.home.bean.search;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/30.
 */

public class RecommendItemDataBean implements Serializable {

    private RecommendInfoItemDataBean recommend_info;

    public RecommendItemDataBean(RecommendInfoItemDataBean recommend_info) {
        this.recommend_info = recommend_info;
    }

    public RecommendInfoItemDataBean getRecommend_info() {
        return recommend_info;
    }

    public void setRecommend_info(RecommendInfoItemDataBean recommend_info) {
        this.recommend_info = recommend_info;
    }

    @Override
    public String toString() {
        return "RecommendItemDataBean{" +
                "recommend_info=" + recommend_info +
                '}';
    }
}
