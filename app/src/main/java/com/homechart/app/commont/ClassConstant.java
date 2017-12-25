package com.homechart.app.commont;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @author allen .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file ClassConstant.java .
 * @brief 固定常量或Key值整合 .
 */
public class ClassConstant {

    public static class PublicKey {
        public static final String SIGN = "sign";//md5签名
        public static final String UUID = "uuid";//唯一标识码
        public static final String T = "t";//请求的时间戳，精确到秒
        public static final String APP_KEY = "app_key";//由服务端颁发的app_key,app_android
    }

    public static class PublicHeader {
        public static final String APP_VERSION = "X-App-Version";//应用版本号
        public static final String APP_PLATFORM = "X-Platform";//请求的终端系统，如ios android等
        public static final String APP_AUTH_TOKEN = "X-Auth-Token";//请求的终端系统，如ios android等

    }

    public static class LoginConstant {
        public static final String USERNAME = "username";//用户名
        public static final String PASSWORD = "password";//密码
    }

    public static class UMengPlatform {
        public static final SHARE_MEDIA platform_qq = SHARE_MEDIA.QQ;
        public static final SHARE_MEDIA platform_weixin = SHARE_MEDIA.WEIXIN;
        public static final SHARE_MEDIA platform_sina = SHARE_MEDIA.SINA;
        public static final SHARE_MEDIA platform_qzone = SHARE_MEDIA.QZONE;
    }

    public static class JiYan {
        public static final String MOBILE = "mobile";
        public static final String TYPE = "type";
        public static final String CHALLENGE = "challenge";
        public static final String VALIDATE = "validate";
        public static final String SECCODE = "seccode";

        //几种type
        public static final String SIGNUP = "signup";
        public static final String FINDPWD = "findpwd";
        public static final String BING = "bind";

    }

    public static class RegisterMobile {
        public static final String MOBILE = "mobile";
        public static final String CAPTCHA = "captcha";
        public static final String NIKENAME = "nickname";
        public static final String PASSWORD = "password";
    }

    public static class ResetPassword {
        public static final String MOBILE = "mobile";
        public static final String CAPTCHA = "captcha";
        public static final String PASSWORD = "password";
    }

    public static class BundleMobile {
        public static final String MOBILE = "mobile";
        public static final String CAPTCHA = "captcha";
        public static final String PASSWORD = "password";
    }

    public static class Parame {
        public static final String ERROR_CODE = "error_code";
        public static final String ERROR_MSG = "error_msg";
        public static final String DATA = "data";

    }


    //登陆成功后
    public static class LoginSucces {
        public static final String LOGIN_STATUS = "login_status";
        public static final String AUTH_TOKEN = "auth_token";
        public static final String USER_ID = "user_id";
        public static final String NIKE_NAME = "nickname";
        public static final String SLOGAN = "slogan";
        public static final String BIG = "big";
        public static final String THUMB = "thumb";
        public static final String EMAIL = "email";
        public static final String MOBILE = "mobile";
        public static final String PROFESSION = "profession";
    }

    //友盟登陆后，调用自己后台接口登陆
    public static class LoginByYouMeng {
        public static final String PLATFORM = "platform";
        public static final String TOKEN = "token";
        public static final String OPENID = "openid";
        public static final String NIKENAME = "nickname";
        public static final String ICONURL = "iconurl";
    }

    //获取粉丝列表
    public static class FenSiList {
        public static final String USER_ID = "user_id";
        public static final String LAST_ID = "last_id";
        public static final String N = "n";
    }

    //获取收藏列表
    public static class ShouCangList {
        public static final String USER_ID = "user_id";
        public static final String S = "s";
        public static final String N = "n";
        public static final String ITEM_ID = "item_id";
        public static final String ARTICLE_ID = "article_id";
    }    //获取收藏列表

    public static class IssueBack {
        public static final String IMMAGE_ID = "immage_id";
        public static final String MOBILE = "mobile";
        public static final String CONTENT = "content";
        public static final String IMAGE_ID = "image_id";


    }

    public static class MessageList {
        public static final String S = "s";
        public static final String N = "n";
    }

    public static class SearchList {
        public static final String Q = "q";
        public static final String TAG_NAME = "tag";
        public static final String S = "s";
        public static final String N = "n";
        public static final String COLOR_ID = "color_id";
    }

    public static class PicListType {
        //single:单图；project:案例； article:文章
        public static final String SINGLE = "single";
        public static final String PROJECT = "project";
        public static final String ARTICLE = "article";
    }

    public static class JuBao {
        //single:单图；project:案例； article:文章
        public static final String PRODUCT = "product";
        public static final String ITEM = "item";
        public static final String ALBUM = "album";
    }

}
