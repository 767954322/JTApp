package com.homechart.app.home.bean.searchshops;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/11/10.
 */

public class SearchFacetsPriceBean implements Serializable {

    private float min;
    private float max;

    public SearchFacetsPriceBean(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "SearchFacetsPriceBean{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
