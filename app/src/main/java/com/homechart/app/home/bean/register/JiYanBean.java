package com.homechart.app.home.bean.register;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/15/015.
 */

public class JiYanBean implements Serializable{

    private int status;
    private String info;
    private JiYanDataBean data;

    public JiYanBean(int status, String info, JiYanDataBean data) {
        this.status = status;
        this.info = info;
        this.data = data;
    }

    public JiYanBean() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public JiYanDataBean getData() {
        return data;
    }

    public void setData(JiYanDataBean data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "JiYanBean{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }
}
