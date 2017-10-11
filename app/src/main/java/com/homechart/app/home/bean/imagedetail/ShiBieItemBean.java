package com.homechart.app.home.bean.imagedetail;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/10/11.
 */

public class ShiBieItemBean implements Serializable{


    private ShiBieObjectItemBean object_info;


    public ShiBieItemBean(ShiBieObjectItemBean object_info) {
        this.object_info = object_info;
    }

    public ShiBieObjectItemBean getObject_info() {
        return object_info;
    }

    public void setObject_info(ShiBieObjectItemBean object_info) {
        this.object_info = object_info;
    }

    @Override
    public String toString() {
        return "ShiBieItemBean{" +
                "object_info=" + object_info +
                '}';
    }
}
