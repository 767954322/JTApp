package com.homechart.app.commont;

/**
 * Created by gumenghao on 18/3/14.
 */

public class CaiJi {

    //********通用********
    public static final String PUBLICK = "javascript:(function(){" +
            "var objs = document.getElementsByTagName(\"img\"); " +
            "var imgUrl = \"\";" +
            "var filter = [\"img//EventHead.png\",\"img//kong.png\",\"hdtz//button.png\"];" +
            "var isShow = true;" +
            "for(var i=0;i<objs.length;i++){" +
            "if(objs[i].width>80){" +
            "imgUrl += objs[i].src + ',';isShow = true;" +
            "}" +
            "}" +
            "window.imageListener.openImage(imgUrl,'');" +
            "})()";

    //********微信********
    public static final String WEIXIN = "javascript:(function(){" +
            "var objs = document.getElementsByTagName(\"img\"); " +
            "var imgUrl = \"\";" +
            "var filter = [\"img//EventHead.png\",\"img//kong.png\",\"hdtz//button.png\"];" +
            "var isShow = true;" +
            "for(var i=0;i<objs.length;i++){" +
            "if(objs[i].width>80){" +
            "imgUrl += objs[i].getAttribute('data-src')?objs[i].getAttribute('data-src'):objs[i].src + '';isShow = true;" +
            "}" +
            "}" +
            "window.imageListener.openImage(imgUrl,'');" +
            "})()";

    //********设计本********
    public static final String SHEJIBEN = "javascript:(function(){" +
            "var objs = document.getElementsByTagName(\"img\"); " +
            "var imgUrl = \"\";" +
            "var filter = [\"img//EventHead.png\",\"img//kong.png\",\"hdtz//button.png\"];" +
            "var isShow = true;" +
            "for(var i=0;i<objs.length;i++){" +
            "if(objs[i].width>80){" +
            "imgUrl += (objs[i].getAttribute('data-original')?objs[i].getAttribute('data-original'):objs[i].src) + ',';isShow = true;" +
            "}" +
            "}" +
            "window.imageListener.openImage(imgUrl,'');" +
            "})()";

}
