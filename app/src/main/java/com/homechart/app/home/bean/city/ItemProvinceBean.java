package com.homechart.app.home.bean.city;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 17/6/8.
 */

public class ItemProvinceBean implements Serializable{

    private String id;
    private String province_id;
    private String province_name;
    private String short_name;
    private String pinyin;
    private String first_letter;
    private List<CityItemBean> citys;

    public ItemProvinceBean(String id, String province_id, String province_name, String short_name, String pinyin, String first_letter, List<CityItemBean> citys) {
        this.id = id;
        this.province_id = province_id;
        this.province_name = province_name;
        this.short_name = short_name;
        this.pinyin = pinyin;
        this.first_letter = first_letter;
        this.citys = citys;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirst_letter() {
        return first_letter;
    }

    public void setFirst_letter(String first_letter) {
        this.first_letter = first_letter;
    }

    public List<CityItemBean> getCitys() {
        return citys;
    }

    public void setCitys(List<CityItemBean> citys) {
        this.citys = citys;
    }

    @Override
    public String toString() {
        return "ItemProvinceBean{" +
                "id='" + id + '\'' +
                ", province_id='" + province_id + '\'' +
                ", province_name='" + province_name + '\'' +
                ", short_name='" + short_name + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", first_letter='" + first_letter + '\'' +
                ", citys=" + citys +
                '}';
    }
}
