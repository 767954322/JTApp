package com.homechart.app.home.bean.hddetails;

import java.io.Serializable;

/**
 * Created by gumenghao on 18/4/20.
 */

public class SharedIndoBean implements Serializable {

    private WeichartBean wechat_friend;
    private WeichartCicleBean wechat_circle;
    private WeiboBean weibo;
    private QQBean qq;


    public SharedIndoBean(WeichartBean wechat_friend, WeichartCicleBean wechat_circle, WeiboBean weibo, QQBean qq) {
        this.wechat_friend = wechat_friend;
        this.wechat_circle = wechat_circle;
        this.weibo = weibo;
        this.qq = qq;
    }

    public WeichartBean getWechat_friend() {
        return wechat_friend;
    }

    public void setWechat_friend(WeichartBean wechat_friend) {
        this.wechat_friend = wechat_friend;
    }

    public WeichartCicleBean getWechat_circle() {
        return wechat_circle;
    }

    public void setWechat_circle(WeichartCicleBean wechat_circle) {
        this.wechat_circle = wechat_circle;
    }

    public WeiboBean getWeibo() {
        return weibo;
    }

    public void setWeibo(WeiboBean weibo) {
        this.weibo = weibo;
    }

    public QQBean getQq() {
        return qq;
    }

    public void setQq(QQBean qq) {
        this.qq = qq;
    }

    @Override
    public String toString() {
        return "SharedIndoBean{" +
                "wechat_friend=" + wechat_friend +
                ", wechat_circle=" + wechat_circle +
                ", weibo=" + weibo +
                ", qq=" + qq +
                '}';
    }
}
