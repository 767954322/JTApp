package com.homechart.app.utils.volley;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.homechart.app.MyApplication;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.KeyConstans;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.UrlConstants;
import com.homechart.app.home.bean.color.ColorItemBean;
import com.homechart.app.utils.Md5Util;

import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;

/**
 * @author allen .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file MyHttpManager.java .
 * @brief 请求工具类 .
 */
public class MyHttpManager {

    public CookieManager cookieManager;

    private static MyHttpManager mpServerHttpManager = new MyHttpManager();
    private RequestQueue queue = MyApplication.getInstance().queue;

    private MyHttpManager() {
    }

    public static MyHttpManager getInstance() {

        return mpServerHttpManager;
    }

    /**
     * 用户登录
     * Collections.sort(list);升序排列
     *
     * @param username
     * @param password
     * @param callback
     */
    public void userLogin(final String username, final String password, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.USER_LOGIN, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.LoginConstant.USERNAME, username);
                map.put(ClassConstant.LoginConstant.PASSWORD, password);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 判断手机号码是否可以发送短信
     *
     * @param mobile
     * @param callback
     */
    public void judgeMobile(final String type, final String mobile, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.JUDGE_MOBILE, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.JiYan.MOBILE, mobile);
                map.put(ClassConstant.JiYan.TYPE, type);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                try {
                    Map<String, String> responseHeaders = response.headers;
                    String rawCookies = responseHeaders.get("Set-Cookie");
                    cookieManager = new CookieManager();
                    cookieManager.getCookieStore().add(null, HttpCookie.parse(rawCookies).get(0));
                    String dataString = new String(response.data, "UTF-8");
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 从服务器获取极验证需要的三个参数
     *
     * @param callback
     */
    public void getParamsFromMyServiceJY(OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.JIYAN_GETPARAM, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 发送验证码
     *
     * @param type
     * @param mobile
     * @param challenge
     * @param validate
     * @param seccode
     * @param callback
     */
    public void sendMessageByJY(final String type, final String mobile, final String challenge, final String validate, final String seccode, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.SEND_MOBILE, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.JiYan.MOBILE, mobile);
                map.put(ClassConstant.JiYan.TYPE, type);
                map.put(ClassConstant.JiYan.CHALLENGE, challenge);
                map.put(ClassConstant.JiYan.VALIDATE, validate);
                map.put(ClassConstant.JiYan.SECCODE, seccode);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 手机号码组册接口
     *
     * @param mobile
     * @param captcha
     * @param nickname
     * @param password
     * @param callback
     */
    public void registerByMobile(final String mobile, final String captcha, final String nickname, final String password, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.REGISTER_MOBILE, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.RegisterMobile.MOBILE, mobile);
                map.put(ClassConstant.RegisterMobile.CAPTCHA, captcha);
                map.put(ClassConstant.RegisterMobile.NIKENAME, nickname);
                map.put(ClassConstant.RegisterMobile.PASSWORD, password);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 第三方登陆后，调用自己的后台接口
     *
     * @param platform
     * @param token
     * @param openid
     * @param nickname
     * @param callback
     */
    public void userLoginByYouMeng(final String platform, final String token, final String openid, final String nickname, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.USER_LOGIN_BYYOUMENG, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.LoginByYouMeng.PLATFORM, platform);
                map.put(ClassConstant.LoginByYouMeng.TOKEN, token);
                map.put(ClassConstant.LoginByYouMeng.OPENID, openid);
                map.put(ClassConstant.LoginByYouMeng.NIKENAME, nickname);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }


    /**
     * 重设密码
     *
     * @param mobile
     * @param captcha
     * @param password
     * @param callback
     */
    public void resetPassWord(final String mobile, final String captcha, final String password, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.RESET_PASSWORD, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.ResetPassword.MOBILE, mobile);
                map.put(ClassConstant.ResetPassword.CAPTCHA, captcha);
                map.put(ClassConstant.ResetPassword.PASSWORD, password);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 获取用户信息
     *
     * @param user_id
     * @param callback
     */
    public void getUserInfo(final String user_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.USER_INFO, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.LoginSucces.USER_ID, user_id);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 关注用户
     *
     * @param user_id
     * @param callback
     */
    public void goGuanZhu(final String user_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.GUANZHU, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.LoginSucces.USER_ID, user_id);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 取消关注用户
     *
     * @param user_id
     * @param callback
     */
    public void goQuXiaoGuanZhu(final String user_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.QUXIAO_GUANZHU, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.LoginSucces.USER_ID, user_id);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 获取粉丝列表
     *
     * @param user_id
     * @param last_id
     * @param n
     * @param callback
     */
    public void getFensiList(final String user_id, final String last_id, final String n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.FENSI_LIST, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.FenSiList.USER_ID, user_id);
                map.put(ClassConstant.FenSiList.LAST_ID, last_id);
                map.put(ClassConstant.FenSiList.N, n);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 获取关注列表
     *
     * @param user_id
     * @param last_id
     * @param n
     * @param callback
     */
    public void getGuanZuList(final String user_id, final String last_id, final String n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.GUANZHU_LIST, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.FenSiList.USER_ID, user_id);
                map.put(ClassConstant.FenSiList.LAST_ID, last_id);
                map.put(ClassConstant.FenSiList.N, n);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 获取省市区列表
     *
     * @param callback
     */
    public void getCityList(OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.CITY_LIST, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 获取收藏列表
     *
     * @param user_id
     * @param callback
     */
    public void getShouCangList(final String user_id, final int s, final String n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.SHOUCANG_LIST, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.ShouCangList.USER_ID, user_id);
                map.put(ClassConstant.ShouCangList.S, s + "");
                map.put(ClassConstant.ShouCangList.N, n);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 获取收藏列表
     *
     * @param user_id
     * @param callback
     */
    public void getShouCangArticleList(final String user_id, final int s, final String n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.SHOUCANG_ARTICLE_LIST, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.ShouCangList.USER_ID, user_id);
                map.put(ClassConstant.ShouCangList.S, s + "");
                map.put(ClassConstant.ShouCangList.N, n);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 获取收藏的商品列表
     * @param user_id
     * @param s
     * @param n
     * @param callback
     */
    public void getShouCangShopList(final String user_id, final int s, final String n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.SHOUCANG_SHOP_LIST, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.ShouCangList.USER_ID, user_id);
                map.put(ClassConstant.ShouCangList.S, s + "");
                map.put(ClassConstant.ShouCangList.N, n);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 获取晒家列表
     *
     * @param user_id
     * @param callback
     */
    public void getShaiJiaList(final String user_id, final int s, final String n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.SHAIJIA_LIST, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.ShouCangList.USER_ID, user_id);
                map.put(ClassConstant.ShouCangList.S, s + "");
                map.put(ClassConstant.ShouCangList.N, n);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 获取晒家列表
     *
     * @param user_id
     * @param callback
     */
    public void getShaiJiaArticleList(final String user_id, final int s, final String n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.SHAIJIA_ARTICLE_LIST, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.ShouCangList.USER_ID, user_id);
                map.put(ClassConstant.ShouCangList.S, s + "");
                map.put(ClassConstant.ShouCangList.N, n);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 更改用户信息
     *
     * @param map_can
     * @param callback
     */
    public void saveUserInfo(final Map<String, String> map_can, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.SAVE_INFO, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());

                for (String key : map_can.keySet()) {
                    map.put(key, map_can.get(key));
                }
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }


    /**
     * 绑定手机号码
     *
     * @param mobile
     * @param captcha
     * @param password
     * @param callback
     */
    public void bundleMobile(final String mobile, final String captcha, final String password, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.BUNDLE_MOBILE, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.BundleMobile.MOBILE, mobile);
                map.put(ClassConstant.BundleMobile.CAPTCHA, captcha);
                map.put(ClassConstant.BundleMobile.PASSWORD, password);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 删除收藏列表
     *
     * @param item_id
     * @param callback
     */
    public void deleteShouCang(final String item_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.DELETE_SHOUCANG, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.ShouCangList.ITEM_ID, item_id);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 删除晒家列表
     *
     * @param item_id
     * @param callback
     */
    public void deleteShaiJia(final String item_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.DELETE_SHAIJIA, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.ShouCangList.ITEM_ID, item_id);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }


    /**
     * 删除晒家文章列表
     *
     * @param item_id
     * @param callback
     */
    public void deleteShaiJiaArticle(final String item_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.DELETE_ARTICLE_SHAIJIA, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.ShouCangList.ARTICLE_ID, item_id);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 删除收藏的文章
     *
     * @param item_id
     * @param callback
     */
    public void deleteShouCangArticle(final String item_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.DELETE_ARTICLE_SHOUCANG, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.ShouCangList.ARTICLE_ID, item_id);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 删除收藏的商品
     *
     * @param item_id
     * @param callback
     */
    public void deleteShouCangShop(final String item_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.DELETE_SHOP_SHOUCANG, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("spu_id", item_id);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 添加收藏的文章
     *
     * @param article_id
     * @param callback
     */
    public void addShouCangArticle(final String article_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.ADD_ARTICLE_SHOUCANG, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.ShouCangList.ARTICLE_ID, article_id);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 删除收藏文章
     *
     * @param article_id
     * @param callback
     */
    public void removeShouCangArticle(final String article_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.REMOVE_ARTICLE_SHOUCANG, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.ShouCangList.ARTICLE_ID, article_id);

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }


    /**
     * 问题反馈
     *
     * @param mobile
     * @param content
     * @param image_id
     * @param callback
     */
    public void issueBack(final String mobile, final String content, final String image_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.ISSUE_BACK, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                if (!TextUtils.isEmpty(mobile)) {
                    map.put(ClassConstant.IssueBack.MOBILE, mobile);
                }
                if (!TextUtils.isEmpty(content)) {
                    map.put(ClassConstant.IssueBack.CONTENT, content);
                }
                if (!TextUtils.isEmpty(image_id)) {
                    map.put(ClassConstant.IssueBack.IMAGE_ID, image_id);
                }

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 系统消息列表
     *
     * @param page_num
     * @param n
     * @param callback
     */
    public void xitongMSGList(final int page_num, final int n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.MESSAGE_LIST, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.MessageList.S, (page_num - 1) * n + "");
                map.put(ClassConstant.MessageList.N, n + "");

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 关注消息列表
     *
     * @param page_num
     * @param n
     * @param callback
     */
    public void guanzhuMSGList(final int page_num, final int n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.GUANZHU_MSG, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.MessageList.S, (page_num - 1) * n + "");
                map.put(ClassConstant.MessageList.N, n + "");

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 收藏消息列表
     *
     * @param page_num
     * @param n
     * @param callback
     */
    public void shoucangMSGList(final int page_num, final int n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.SHOUCANG_MSG, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.MessageList.S, (page_num - 1) * n + "");
                map.put(ClassConstant.MessageList.N, n + "");

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 评论消息列表
     *
     * @param page_num
     * @param n
     * @param callback
     */
    public void pinglunMSGList(final int page_num, final int n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.PINGLUN_MSG, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.MessageList.S, (page_num - 1) * n + "");
                map.put(ClassConstant.MessageList.N, n + "");

                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 获取搜索的热词
     *
     * @param callback
     */
    public void getSearchHotWords(OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.HOT_WORDS, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 获取未读消息数
     *
     * @param callback
     */
    public void getUnReadMsg(OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.UNREADER_MSG, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 获取首页推荐列表
     *
     * @param s
     * @param n
     * @param callback
     */
    public void getRecommendList(final String s, final String n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.RECOMMEND_LIST, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.MessageList.S, s);
                map.put(ClassConstant.MessageList.N, n);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 获取搜索结果
     *
     * @param s
     * @param n
     * @param callback
     */
    public void getSearchList(final Map<Integer, ColorItemBean> mSelectListData, final String q, final String tag_name, final String s, final String n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.SEARCH_LIST, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                if (!TextUtils.isEmpty(q.trim())) {
                    map.put(ClassConstant.SearchList.Q, q);
                }
                if (!TextUtils.isEmpty(tag_name.trim())) {
                    map.put(ClassConstant.SearchList.TAG_NAME, tag_name);
                }


                if (null != mSelectListData && mSelectListData.size() > 0) {
                    StringBuffer sb = new StringBuffer();
                    for (Integer key : mSelectListData.keySet()) {
                        sb.append(key + ",");
                    }
                    String color = sb.toString();
                    color = color.substring(0, sb.length() - 1);
                    map.put(ClassConstant.SearchList.COLOR_ID, color);
                }

                map.put(ClassConstant.SearchList.S, s);
                map.put(ClassConstant.SearchList.N, n);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 获取搜索的热词
     *
     * @param callback
     */
    public void getPicTagData(OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.TAG_DATA, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 获取推荐的标签
     *
     * @param tag
     * @param callback
     */
    public void getTuiJianTagData(final String tag, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.TUIJIAN_TAG, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("tag", tag);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 获取筛选颜色列表
     *
     * @param callback
     */
    public void getColorListData(OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.COLOR_LIST, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 获取筛选颜色列表
     *
     * @param callback
     */
    public void getDoingActivityData(OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.DOING_ACTIVITY, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }


    /**
     * 获取推荐的标签
     *
     * @param tag
     * @param callback
     */
    public void doFaBu(final String image_id, final String description, final String tag, final String activity_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.FABU, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("image_id", image_id);
                map.put("description", description);
                map.put("tag", tag);
                if (!TextUtils.isEmpty(activity_id)) {
                    map.put("activity_id", activity_id);
                }
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 获取发布详情
     *
     * @param item_id
     * @param callback
     */
    public void itemDetailsFaBu(final String item_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.ITEM_FABU, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("item_id", item_id);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 点赞图片
     *
     * @param item_id
     * @param callback
     */
    public void addZan(final String item_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.ADD_ZAN_PIC, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("item_id", item_id);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 点赞文章
     *
     * @param article_id
     * @param callback
     */
    public void addZanArticle(final String article_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.ADD_ZAN_ARTICLE, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("article_id", article_id);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 取消点赞图片
     *
     * @param item_id
     * @param callback
     */
    public void removeZan(final String item_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.REMOVE_ZAN_PIC, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("item_id", item_id);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 取消点赞文章
     *
     * @param article_id
     * @param callback
     */
    public void removeZanArticle(final String article_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.REMOVE_ZAN_ARTICLE, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("article_id", article_id);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 收藏
     *
     * @param item_id
     * @param callback
     */
    public void addShouCang(final String item_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.ADD_SHOUCANG, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("item_id", item_id);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 删除收藏单图
     *
     * @param item_id
     * @param callback
     */
    public void removeShouCang(final String item_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.REMOVE_SHOUCANG, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("item_id", item_id);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 编辑晒家图片
     *
     * @param item_id
     * @param description
     * @param tag
     * @param callback
     */
    public void imageEdit(final String item_id, final String description, final String tag, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.IMAGE_EDIT, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("item_id", item_id);
                map.put("description", description);
                map.put("tag", tag);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /*
     * 获取单图评论列表
     * @param item_id
     * @param last_id
     * @param n
     * @param callback
     */
    public void getPingList(final String item_id, final String last_id, final String n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.PING_LIST, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("item_id", item_id);
                map.put("last_id", last_id);
                map.put("n", n);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 回复单图评论
     *
     * @param reply_id
     * @param content
     * @param callback
     */
    public void pingReply(final String reply_id, final String content, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.PING_REPLY, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("reply_id", reply_id);
                map.put("content", content);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 单图评论
     *
     * @param item_id
     * @param content
     * @param callback
     */
    public void pingImage(final String item_id, final String content, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.PING_IMAGE, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("item_id", item_id);
                map.put("content", content);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 猜你喜欢
     *
     * @param item_id
     * @param s
     * @param n
     * @param callback
     */
    public void caiLikeImage(final String item_id, final String s, final String n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.LIKE_CAI, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("item_id", item_id);
                map.put("s", s);
                map.put("n", n);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 参与活动的图片列表   sort..排序 hot：热度 new:最新
     *
     * @param activity_id
     * @param s
     * @param n
     * @param sort
     * @param callback
     */
    public void huoDongImageList(final String activity_id, final String s, final String n, final String sort, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.ACTIVITY_IMAGE, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("activity_id", activity_id);
                map.put("s", s);
                map.put("n", n);
                map.put("sort", sort);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 活动详情
     *
     * @param activity_id
     * @param callback
     */
    public void activityDetails(final String activity_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.ACTIVITY_DETAILS, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("activity_id", activity_id);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 增加分享数
     *
     * @param object_id
     * @param type
     * @param callback
     */
    public void addShared(final String object_id, final String type, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.ADD_SHARED, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("object_id", object_id);
                map.put("type", type);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 获取文章搜索结果
     *
     * @param s
     * @param n
     * @param callback
     */
    public void getArticleSearchList(final String q, final String s, final String n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.SEARCH_ARTICLE, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put(ClassConstant.SearchList.Q, q);
                map.put(ClassConstant.SearchList.S, s);
                map.put(ClassConstant.SearchList.N, n);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 获取文章详情
     *
     * @param article_id
     * @param callback
     */
    public void getArticleDetails(final String article_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.DETAILS_ARTICLE, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("article_id", article_id);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 获取文章详情评论列表
     *
     * @param article_id
     * @param s
     * @param n
     * @param callback
     */
    public void getArticlePingList(final String article_id, final int s, final int n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.PINGLIST_ARTICLE, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("article_id", article_id);
                map.put("s", s + "");
                map.put("n", n + "");
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 对文章发表评论
     *
     * @param article_id
     * @param content
     * @param callback
     */
    public void pingArticleAll(final String article_id, final String content, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.PING_ARTICLE_ALL, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("article_id", article_id);
                map.put("content", content);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 回复对文章发表的评论
     *
     * @param reply_id
     * @param content
     * @param callback
     */
    public void pingArticleSingle(final String reply_id, final String content, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.PING_ARTICLE_SINGLE, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("reply_id", reply_id);
                map.put("content", content);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 文章详情-猜你喜欢
     *
     * @param article_id
     * @param s
     * @param n
     * @param callback
     */
    public void getLikeArticleList(final String article_id, final int s, final int n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.LIKE_ARTICLE, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("article_id", article_id);
                map.put("s", s + "");
                map.put("n", n + "");
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }


    /**
     * 对评论点赞
     *
     * @param comment_id
     * @param callback
     */
    public void addPingZan(final String comment_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.PING_ADDZAN, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("comment_id", comment_id);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 取消对评论点赞
     *
     * @param comment_id
     * @param callback
     */
    public void removePingZan(final String comment_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.PING_REMOVEZAN, callback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("comment_id", comment_id);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }

    /**
     * 获取最新版本号
     *
     * @param callback
     */
    public void checkLastVersion(OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.LAST_VERSION, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }

        };
        queue.add(okStringRequest);
    }


    /**
     * 下载apk
     *
     * @param url
     * @param callback
     */
    public void downLoadApk(final String url, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, url, callback) {


        };
        queue.add(okStringRequest);
    }


    /**
     * 获取往期活动列表
     *
     * @param s
     * @param n
     * @param callback
     */
    public void getHuoDongiList(final String s, final String n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.HUODONG_LIST, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("s", s);
                map.put("n", n);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 获取商品详情
     *
     * @param spu_id
     * @param callback
     */
    public void getShopDetails(final String spu_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.DETAIL_SHOP, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("spu_id", spu_id);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 获取相似商品
     *
     * @param spu_id
     * @param num
     * @param callback
     */
    public void getLikeShop(final String spu_id,final String num, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.MORELIKE_SHOP, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("spu_id", spu_id);
                map.put("num", num);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     *
     * 检测图片中的物体
     * @param image_id
     * @param callback
     */
    public void checkImage(final String image_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.DETAIL_SHOP, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("image_id", image_id);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 检测图片中的物体
     * @param image_id
     * @param loc
     * @param category_id
     * @param callback
     */
    public void searchShopImage(final String image_id,final String loc,final String category_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.SEARCH_SHOP_IMAGE, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("image_id", image_id);
                map.put("loc", loc);
                map.put("category_id", category_id);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 收藏商品
     * @param spu_id
     * @param callback
     */
    public void shoucangShop(final String spu_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.SHOUCANG_ADD_SHOP, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("spu_id", spu_id);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 删除收藏的商品
     * @param spu_id
     * @param callback
     */
    public void deleteShop(final String spu_id, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.SHOUCANG_REMOVE_SHOP, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("spu_id", spu_id);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }

    /**
     * 检测历史记录
     * @param user_id
     * @param s
     * @param n
     * @param callback
     */
    public void historyShiBie(final String user_id,final String s,final String n, OkStringRequest.OKResponseCallback callback) {
        OkStringRequest okStringRequest = new OkStringRequest(Request.Method.POST, UrlConstants.HISTORY_SHIBIE, callback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                map.put("user_id", user_id);
                map.put("s", s);
                map.put("n", n);
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                return map;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PublicUtils.getPublicHeader(MyApplication.getInstance());
            }
        };
        queue.add(okStringRequest);
    }


}
