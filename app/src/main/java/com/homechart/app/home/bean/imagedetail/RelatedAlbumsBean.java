package com.homechart.app.home.bean.imagedetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/22.
 */

public class RelatedAlbumsBean implements Serializable {

    private RelatedAlbumsDataBean data_info;

    public RelatedAlbumsBean(RelatedAlbumsDataBean data_info) {
        this.data_info = data_info;
    }

    public RelatedAlbumsDataBean getData_info() {
        return data_info;
    }

    public void setData_info(RelatedAlbumsDataBean data_info) {
        this.data_info = data_info;
    }

    @Override
    public String toString() {
        return "RelatedAlbumsBean{" +
                "data_info=" + data_info +
                '}';
    }
}
