package com.homechart.app.home.bean.caijiimg;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/4/13.
 */

public class CaiJiImageBean implements Serializable {

    private String des;
    private String url;
    private boolean hasCaiJi = false;

    public CaiJiImageBean() {
    }

    public CaiJiImageBean(String des, String url) {
        this.des = des;
        this.url = url;
    }


    public CaiJiImageBean(String des, String url, boolean hasCaiJi) {
        this.des = des;
        this.url = url;
        this.hasCaiJi = hasCaiJi;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isHasCaiJi() {
        return hasCaiJi;
    }

    public void setHasCaiJi(boolean hasCaiJi) {
        this.hasCaiJi = hasCaiJi;
    }




    @Override
    public String toString() {
        return "CaiJiImageBean{" +
                "des='" + des + '\'' +
                ", url='" + url + '\'' +
                ", hasCaiJi=" + hasCaiJi +
                '}';
    }
}
