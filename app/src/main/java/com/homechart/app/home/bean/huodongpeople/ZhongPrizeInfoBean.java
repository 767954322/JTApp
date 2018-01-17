package com.homechart.app.home.bean.huodongpeople;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/1/17.
 */

public class ZhongPrizeInfoBean implements Serializable {

    private String grade;
    private String name;
    private List<ZhongPrizeInfoItemBean> user_list;

    public ZhongPrizeInfoBean(String grade, String name, List<ZhongPrizeInfoItemBean> user_list) {
        this.grade = grade;
        this.name = name;
        this.user_list = user_list;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ZhongPrizeInfoItemBean> getUser_list() {
        return user_list;
    }

    public void setUser_list(List<ZhongPrizeInfoItemBean> user_list) {
        this.user_list = user_list;
    }

    @Override
    public String toString() {
        return "ZhongPrizeInfoBean{" +
                "grade='" + grade + '\'' +
                ", name='" + name + '\'' +
                ", user_list=" + user_list +
                '}';
    }
}
