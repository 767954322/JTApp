package com.homechart.app.home.bean.huodonglist;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/8/7.
 */

public class HDItemInfoBean implements Serializable {

    private String activity_id;
    private String title;
    private String description;
    private String start_time;
    private String end_time;
    private String join_user_num;
    private String state_id;
    private HDItemImageBean image;

    public HDItemInfoBean(String activity_id, String title, String description, String start_time, String end_time, String join_user_num, String state_id, HDItemImageBean image) {
        this.activity_id = activity_id;
        this.title = title;
        this.description = description;
        this.start_time = start_time;
        this.end_time = end_time;
        this.join_user_num = join_user_num;
        this.state_id = state_id;
        this.image = image;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getJoin_user_num() {
        return join_user_num;
    }

    public void setJoin_user_num(String join_user_num) {
        this.join_user_num = join_user_num;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public HDItemImageBean getImage() {
        return image;
    }

    public void setImage(HDItemImageBean image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "HDItemInfoBean{" +
                "activity_id='" + activity_id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", join_user_num='" + join_user_num + '\'' +
                ", state_id='" + state_id + '\'' +
                ", image=" + image +
                '}';
    }
}
