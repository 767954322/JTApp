package com.homechart.app.home.bean.province;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/8.
 */

public class MyCityBean implements Serializable{

    private String cityName ;
    private String cityid ;
    private String provinceid ;

    public MyCityBean(String cityName, String cityid, String provinceid) {
        this.cityName = cityName;
        this.cityid = cityid;
        this.provinceid = provinceid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    @Override
    public String toString() {
        return "MyCityBean{" +
                "cityName='" + cityName + '\'' +
                ", cityid='" + cityid + '\'' +
                ", provinceid='" + provinceid + '\'' +
                '}';
    }
}
