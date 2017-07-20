package com.homechart.app.home.bean.login;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/6/5.
 */

public class LoginBean implements Serializable{

    private String auth_token;
    private LoginUserInfo user_info;


    public LoginBean(String auth_token, LoginUserInfo user_info) {
        this.auth_token = auth_token;
        this.user_info = user_info;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public LoginUserInfo getUser_info() {
        return user_info;
    }

    public void setUser_info(LoginUserInfo user_info) {
        this.user_info = user_info;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "auth_token='" + auth_token + '\'' +
                ", user_info=" + user_info +
                '}';
    }
}
