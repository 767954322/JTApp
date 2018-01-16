package com.homechart.app.home.bean.hddetails;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/1/16.
 */

public class ActivityPrizeItemBean implements Serializable{

    private String grade;
    private String name;

    public ActivityPrizeItemBean(String grade, String name) {
        this.grade = grade;
        this.name = name;
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

    @Override
    public String toString() {
        return "ActivityPrizeItemBean{" +
                "grade='" + grade + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
