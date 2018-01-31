package com.homechart.app.home.bean.guanliantags;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/1/31.
 */

public class GuanLianTagBean implements Serializable {

   private GuanLianTagInfoBean tag_info;

    public GuanLianTagBean(GuanLianTagInfoBean tag_info) {
        this.tag_info = tag_info;
    }

    public GuanLianTagInfoBean getTag_info() {
        return tag_info;
    }

    public void setTag_info(GuanLianTagInfoBean tag_info) {
        this.tag_info = tag_info;
    }

    @Override
    public String toString() {
        return "GuanLianTagBean{" +
                "tag_info=" + tag_info +
                '}';
    }
}
