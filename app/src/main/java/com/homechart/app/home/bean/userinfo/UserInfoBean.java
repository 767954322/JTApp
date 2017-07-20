package com.homechart.app.home.bean.userinfo;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/7.
 */

public class UserInfoBean implements Serializable{

    private String user_id;
    private String nickname;
    private String avatar_id;
    private String gender;
    private String age_group;
    private String province;
    private String city;
    private String location;
    private String slogan;
    private String email;
    private String relation;
    private String mobile;
    private String profession;
    private AvatarBean avatar;

    public UserInfoBean(String user_id, String nickname, String avatar_id, String gender, String age_group, String province, String city, String location, String slogan, String email, String relation, String mobile, String profession, AvatarBean avatar) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.avatar_id = avatar_id;
        this.gender = gender;
        this.age_group = age_group;
        this.province = province;
        this.city = city;
        this.location = location;
        this.slogan = slogan;
        this.email = email;
        this.relation = relation;
        this.mobile = mobile;
        this.profession = profession;
        this.avatar = avatar;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar_id() {
        return avatar_id;
    }

    public void setAvatar_id(String avatar_id) {
        this.avatar_id = avatar_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge_group() {
        return age_group;
    }

    public void setAge_group(String age_group) {
        this.age_group = age_group;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public AvatarBean getAvatar() {
        return avatar;
    }

    public void setAvatar(AvatarBean avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar_id='" + avatar_id + '\'' +
                ", gender='" + gender + '\'' +
                ", age_group='" + age_group + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", location='" + location + '\'' +
                ", slogan='" + slogan + '\'' +
                ", email='" + email + '\'' +
                ", relation='" + relation + '\'' +
                ", mobile='" + mobile + '\'' +
                ", profession='" + profession + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
