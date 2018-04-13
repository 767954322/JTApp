package com.homechart.app.commont;

/**
 * Created by gumenghao on 18/3/14.
 */

public class CaiJi {

    /**
     * var desc=img.getAttribute('alt');
     * <p>
     * desc=desc?desc:img.parentNode.getAttribute('title');
     * <p>
     * desc=desc?desc:document.title;
     */

    public static final String tagItem = "!!!@@@";
    public static final String tagDesc = "@@@!!!";

    //********通用********
    public static final String PUBLICK = "javascript:(function(){" +
            "var objs = document.getElementsByTagName(\"img\"); " +
            "var imgUrl = \"\";" +
            "var filter = [\"img//EventHead.png\",\"img//kong.png\",\"hdtz//button.png\"];" +
            "var isShow = true;" +
            "for(var i=0;i<objs.length;i++){" +
            "if(objs[i].width>80){" +
            "var desc=objs[i].getAttribute('alt');" +
            "desc=desc?desc:objs[i].parentNode.getAttribute('title');" +
            "desc=desc?desc:document.title;" +
            "imgUrl += objs[i].src   +  '@@@!!!' +  desc + '!!!@@@';isShow = true;" +
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
            "var desc=objs[i].getAttribute('alt');" +
            "desc=desc?desc:objs[i].parentNode.getAttribute('title');" +
            "desc=desc?desc:document.title;" +
            "imgUrl += objs[i].getAttribute('data-src')?objs[i].getAttribute('data-src'):objs[i].src+  +  '@@@!!!' +  desc + '!!!@@@' ;isShow = true;" +
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
            "var desc=objs[i].getAttribute('alt');" +
            "desc=desc?desc:objs[i].parentNode.getAttribute('title');" +
            "desc=desc?desc:document.title;" +
            "imgUrl += (objs[i].getAttribute('data-original')?objs[i].getAttribute('data-original'):objs[i].src)  +  '@@@!!!' +  desc + '!!!@@@';isShow = true;" +
            "}" +
            "}" +
            "window.imageListener.openImage(imgUrl,'');" +
            "})()";


    public static final String PUBLICK1 = "javascript:(function(){" +
            "var objs = document.getElementsByTagName(\"img\"); " +
            "var imgUrl = \"\";" +
            "var filter = [\"img//EventHead.png\",\"img//kong.png\",\"hdtz//button.png\"];" +
            "var isShow = true;" +
            "for(var i=0;i<objs.length;i++){" +
            "if(objs[i].width>80){" +
            "var desc=objs[i].getAttribute('alt');" +
            "desc=desc?desc:objs[i].parentNode.getAttribute('title');" +
            "desc=desc?desc:document.title;" +
            "imgUrl += objs[i].src   +  '@@@!!!' +  desc + '!!!@@@';isShow = true;" +
            "}" +
            "}" +
            "window.imageListener.finishLoad(imgUrl,'');" +
            "})()";

    //********微信********
    public static final String WEIXIN1 = "javascript:(function(){" +
            "var objs = document.getElementsByTagName(\"img\"); " +
            "var imgUrl = \"\";" +
            "var filter = [\"img//EventHead.png\",\"img//kong.png\",\"hdtz//button.png\"];" +
            "var isShow = true;" +
            "for(var i=0;i<objs.length;i++){" +
            "if(objs[i].width>80){" +
            "var desc=objs[i].getAttribute('alt');" +
            "desc=desc?desc:objs[i].parentNode.getAttribute('title');" +
            "desc=desc?desc:document.title;" +
            "imgUrl += objs[i].getAttribute('data-src')?objs[i].getAttribute('data-src'):objs[i].src+  +  '@@@!!!' +  desc + '!!!@@@' ;isShow = true;" +
            "}" +
            "}" +
            "window.imageListener.finishLoad(imgUrl,'');" +
            "})()";

    //********设计本********
    public static final String SHEJIBEN1 = "javascript:(function(){" +
            "var objs = document.getElementsByTagName(\"img\"); " +
            "var imgUrl = \"\";" +
            "var filter = [\"img//EventHead.png\",\"img//kong.png\",\"hdtz//button.png\"];" +
            "var isShow = true;" +
            "for(var i=0;i<objs.length;i++){" +
            "if(objs[i].width>80){" +
            "var desc=objs[i].getAttribute('alt');" +
            "desc=desc?desc:objs[i].parentNode.getAttribute('title');" +
            "desc=desc?desc:document.title;" +
            "imgUrl += (objs[i].getAttribute('data-original')?objs[i].getAttribute('data-original'):objs[i].src)  +  '@@@!!!' +  desc + '!!!@@@';isShow = true;" +
            "}" +
            "}" +
            "window.imageListener.finishLoad(imgUrl,'');" +
            "})()";

}
