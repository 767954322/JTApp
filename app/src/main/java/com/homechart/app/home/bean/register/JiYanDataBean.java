package com.homechart.app.home.bean.register;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/15/015.
 */

public class JiYanDataBean implements Serializable{

    private int success;
    private String gt;
    private String challenge;

    public JiYanDataBean(int success, String gt, String challenge) {
        this.success = success;
        this.gt = gt;
        this.challenge = challenge;
    }

    public JiYanDataBean() {
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getGt() {
        return gt;
    }

    public void setGt(String gt) {
        this.gt = gt;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    @Override
    public String toString() {
        return "JiYanDataBean{" +
                "success=" + success +
                ", gt='" + gt + '\'' +
                ", challenge='" + challenge + '\'' +
                '}';
    }
}
