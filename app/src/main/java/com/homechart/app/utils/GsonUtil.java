package com.homechart.app.utils;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;


public class GsonUtil {
    public static final String UTF_8 = "UTF-8";
    public static final String ISO_8859_1 = "ISO-8859-1";

    private GsonUtil() {
    }

    private static String userInfo;

    public static <T> T jsonToBean(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    public static <T, V> T jsonToBean(@NonNull String json, @NonNull Class<T> clazz,
                                      @NonNull Class<V> multiTypeClazz, @NonNull TypeAdapter<V> typeAdapter) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(multiTypeClazz, typeAdapter)
                .create();

        return gson.fromJson(json, clazz);
    }

    public static <T> T jsonToBean(@NonNull String json, @NonNull Class<T> clazz,
                                   @NonNull List<Class<?>> multiTypeList, List<TypeAdapter<?>> typeAdapterList) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        for (int i = 0; i < multiTypeList.size(); i++) {
            gsonBuilder.registerTypeAdapter(multiTypeList.get(i), typeAdapterList.get(i));
        }

        return gsonBuilder.create().fromJson(json, clazz);
    }


    public static String jsonToString(JSONObject jsonObject) {
        try {
            if (null != jsonObject) {
                userInfo = new String(jsonObject.toString().getBytes(ISO_8859_1), UTF_8);
            } else {
                userInfo = "";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public static <T> JSONObject beanToJSONObject(T bean) throws JSONException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(bean);
        return new JSONObject(jsonString);
    }

    public static <T> String beanToString(T bean) {
        Gson gson = new Gson();
        return gson.toJson(bean);
    }
}
