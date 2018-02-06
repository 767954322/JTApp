package com.homechart.app;

import android.app.Application;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.commont.ActivityManager;
import com.homechart.app.utils.UILImageLoader;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by gumenghao on 17/5/17.
 */

public class MyApplication extends MultiDexApplication {

    private static MyApplication myApplication;
    public static RequestQueue queue;
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        myApplication = this;
        queue = Volley.newRequestQueue(this);
        initJPush();
        initImageLoader();
        initPike();
        initYouMeng();
        //禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.openActivityDurationTrack(false);
    }

    private void initJPush() {

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
//        JPushInterface.setAlias(MyApplication.this,0,"测试");

        //取消极光服务，推出登陆状态，关闭推送
//        JPushInterface.stopPush(MyApplication.this);
    }

    private void initPike() {

        //黑色主题
        ThemeConfig DARK = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.rgb(0x38, 0x42, 0x48))
                .setFabNornalColor(Color.rgb(0x38, 0x42, 0x48))
                .setFabPressedColor(Color.rgb(0x20, 0x25, 0x28))
                .setCheckSelectedColor(Color.rgb(0x38, 0x42, 0x48))
                .setCropControlColor(Color.rgb(0x38, 0x42, 0x48))
                .setEditPhotoBgTexture(new ColorDrawable(0xd0000000))
                .build();

        ThemeConfig themeConfig = DARK;
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(1)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setCropSquare(true).build();
        ImageLoader imageLoader = new UILImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig).build();
        GalleryFinal.init(coreConfig);
    }

    private void initImageLoader() {
        ImageUtils.initImageLoader(this);
    }

    private void initYouMeng() {
        //初始化友盟sdk
        UMShareAPI.get(this.getApplicationContext());
        //配置三方平台的appkey
        PlatformConfig.setWeixin("wx9f5d6adeded62a61", "a32cd3c376f0881d7f6a5679a473a5bc");
        PlatformConfig.setQQZone("101164104", "28e0959303e8960b06bfa217f959f1d7");
        PlatformConfig.setSinaWeibo("3994674331", "7450446dd1d555532d14982a13f70408", "http://sns.whalecloud.com/sina2/callback");
        Config.DEBUG = true;
    }

    public static synchronized MyApplication getInstance() {
        if (myApplication == null) {
            myApplication = new MyApplication();
        }
        return myApplication;
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

}
