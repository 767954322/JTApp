package com.homechart.app.lingganji.common.entity.inspirationdetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/4.
 */

public class InspirationDetailBean implements Serializable {

    private InsDetailsInfoBean info;

    public InspirationDetailBean(InsDetailsInfoBean info) {
        this.info = info;
    }

    public InsDetailsInfoBean getInfo() {
        return info;
    }

    public void setInfo(InsDetailsInfoBean info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "InspirationDetailBean{" +
                "info=" + info +
                '}';
    }
}
