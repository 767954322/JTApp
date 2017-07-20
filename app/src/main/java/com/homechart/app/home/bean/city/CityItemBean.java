package com.homechart.app.home.bean.city;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/8.
 */

public class CityItemBean implements Serializable {

    private String id;
    private String city_id;
    private String city_name;
    private String short_name;
    private String pinyin;
    private String first_letter;
    private String father_id;

    public CityItemBean(String id, String city_id, String city_name, String short_name, String pinyin, String first_letter, String father_id) {
        this.id = id;
        this.city_id = city_id;
        this.city_name = city_name;
        this.short_name = short_name;
        this.pinyin = pinyin;
        this.first_letter = first_letter;
        this.father_id = father_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
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

    public String getFather_id() {
        return father_id;
    }

    public void setFather_id(String father_id) {
        this.father_id = father_id;
    }

    @Override
    public String toString() {
        return "CityItemBean{" +
                "id='" + id + '\'' +
                ", city_id='" + city_id + '\'' +
                ", city_name='" + city_name + '\'' +
                ", short_name='" + short_name + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", first_letter='" + first_letter + '\'' +
                ", father_id='" + father_id + '\'' +
                '}';
    }
}
