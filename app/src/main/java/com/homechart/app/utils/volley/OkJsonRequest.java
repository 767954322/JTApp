package com.homechart.app.utils.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.homechart.app.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author allen .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file MyHttpManager.java .
 * @brief JSON对象请求 .
 */
public class OkJsonRequest extends JsonObjectRequest {
    /**
     * 请求网络
     *
     * @param method          请求方式
     * @param url             请求的地址
     * @param jsonRequest     请求体
     * @param successListener 成功的回调
     * @param errorListener   失败的回调
     */
    public OkJsonRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, successListener, errorListener);
        this.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
    }

    /**
     * @param method
     * @param url
     * @param jsonRequest
     * @param callback
     */
    public OkJsonRequest(int method, String url, JSONObject jsonRequest, OKResponseCallback callback) {
        this(method, url, jsonRequest, callback, callback);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-type", "application/json;charset=UTF-8");
        return header;
    }

    /**
     * 请求网络的回调
     */
    public interface OKResponseCallback extends Response.Listener<JSONObject>, Response
            .ErrorListener {
    }

    @Override
    public Request<?> setCacheEntry(Cache.Entry entry) {
        return super.setCacheEntry(entry);
    }


    /**
     * 增加response为null情况判断，防止走失败方法
     *
     * @param response
     * @return
     */
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            //Allow null
            if (jsonString == null || jsonString.length() == 0) {
                return Response.success(null,
                        HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.success(new JSONObject(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    /**
     * 取消请求
     *
     * @param tag 请求TAG
     */
    public static void cancelRequest(Object tag) {

        if (MyApplication.getInstance().queue != null) {
            MyApplication.getInstance().queue.cancelAll(tag);//从请求队列中取消对应的任务
        }
    }
}
