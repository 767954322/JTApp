package com.homechart.app.utils.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

/**
 * Created by GPD on 2017/3/4.
 */

public class GlideImgManager {

    /**
     * 1. 不传  矩形
     * 2. 0.圆形
     *3.  1.圆角
     *
     *
     */
    /**
     * load normal  for img
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).into(iv);
    }

    /**
     * load normal  for  circle or round img
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     * @param tag
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv, int tag) {
        if (0 == tag) {
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideCircleTransform(context)).into(iv);
        } else if (1 == tag) {
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context, 10)).into(iv);
        }
    }

    public static void glideLoaderHigh(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).priority(Priority.HIGH).placeholder(emptyImg).error(erroImg).into(iv);
    }

    public static void glideLoaderHigh(Context context, String url, int erroImg, int emptyImg, ImageView iv, int tag) {
        if (0 == tag) {
            Glide.with(context).load(url).priority(Priority.HIGH).placeholder(emptyImg).error(erroImg).transform(new GlideCircleTransform(context)).into(iv);
        } else if (1 == tag) {
            Glide.with(context).load(url).priority(Priority.HIGH).placeholder(emptyImg).error(erroImg).transform(new GlideRoundTransform(context, 10)).into(iv);
        }
    }
}
