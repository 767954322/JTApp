package com.homechart.app.home.bean.search;

import com.homechart.app.home.bean.shouye.SYActivityBean;
import com.homechart.app.home.bean.shouye.SYDataBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/14.
 */

public class SearchDataBean implements Serializable{

    private List<RecommendItemDataBean> recommend_list;
    private List<SearchItemDataBean> item_list;
    private ActivityInfoBean ad_info;


    public SearchDataBean(List<RecommendItemDataBean> recommend_list, List<SearchItemDataBean> item_list, ActivityInfoBean ad_info) {
        this.recommend_list = recommend_list;
        this.item_list = item_list;
        this.ad_info = ad_info;
    }

    public List<RecommendItemDataBean> getRecommend_list() {
        return recommend_list;
    }

    public void setRecommend_list(List<RecommendItemDataBean> recommend_list) {
        this.recommend_list = recommend_list;
    }

    public List<SearchItemDataBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<SearchItemDataBean> item_list) {
        this.item_list = item_list;
    }

    public ActivityInfoBean getAd_info() {
        return ad_info;
    }

    public void setAd_info(ActivityInfoBean ad_info) {
        this.ad_info = ad_info;
    }

    @Override
    public String toString() {
        return "SearchDataBean{" +
                "recommend_list=" + recommend_list +
                ", item_list=" + item_list +
                ", ad_info=" + ad_info +
                '}';
    }
}
