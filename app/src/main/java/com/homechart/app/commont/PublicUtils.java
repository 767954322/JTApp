package com.homechart.app.commont;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.home.activity.LoginActivity;
import com.homechart.app.home.bean.login.LoginBean;
import com.homechart.app.upapk.FileUtils;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.DataCleanManager;
import com.homechart.app.utils.MPFileUtility;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by allen on 2017/6/1.
 */

public class PublicUtils {
    //1................................................................................................

    /**
     * 获取公共参数的map
     *
     * @param context
     * @return
     */
    public static Map<String, String> getPublicMap(Context context) {
        Map<String, String> map = new HashMap<>();
        map.put(ClassConstant.PublicKey.UUID, getPhoneImail(context));
        map.put(ClassConstant.PublicKey.T, getCurrentTime(context) + "");
        map.put(ClassConstant.PublicKey.APP_KEY, "app_android");
        return map;
    }
    //2................................................................................................

    /**
     * 获取公共header的map
     *
     * @param context
     * @return
     */
    public static Map<String, String> getPublicHeader(Context context) {
        Map<String, String> map = new HashMap<>();
        map.put(ClassConstant.PublicHeader.APP_VERSION, PublicUtils.getVersionName(context));
        map.put(ClassConstant.PublicHeader.APP_PLATFORM, "android");
        boolean login_status = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
        if (login_status) {
            map.put(ClassConstant.PublicHeader.APP_AUTH_TOKEN, SharedPreferencesUtils.readString(ClassConstant.LoginSucces.AUTH_TOKEN));
        }
        return map;
    }
    //3................................................................................................

    /**
     * @param context
     * @return 唯一标识
     */
    public static String getPhoneImail(Context context) {
        String phone_id = "";
        try {
            phone_id = ((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            phone_id = getPesudoUniqueID();
        }
        return phone_id;
    }

    public static String getPesudoUniqueID() {
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        return m_szDevIDShort;
    }
    //4................................................................................................

    /**
     * 获取当前时间戳
     *
     * @param context
     * @return
     */
    private static Long getCurrentTime(Context context) {
        Long currentdate = new Date().getTime();

        return currentdate;
    }
    //5................................................................................................

    /**
     * 获取加密参数key按照首字母的降序排序规则拼接的string
     *
     * @param map
     * @return
     */
    public static String getSinaString(Map<String, String> map) {

        List<String> list = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        for (String key : map.keySet()) {
            list.add(key);
            System.out.println("key= " + key + " and value= " + map.get(key));
        }
        Collections.sort(list);

        for (int i = 0; i < list.size(); i++) {
            stringBuffer.append(list.get(i) + map.get(list.get(i)));
        }
        return stringBuffer.toString();
    }
    //6................................................................................................

    /**
     * 判断网络状态
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    //7................................................................................................

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 添加权限
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
    //8................................................................................................

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    //9................................................................................................

    //友盟成功的回调接口
    public interface ILoginUmeng {
        void loginUmengBack(String openid, String token, String plat, String name, String iconurl);
    }

    //友盟登陆的回调
    public static class UmAuthListener implements UMAuthListener {

        Activity mActivity;
        ILoginUmeng mILoginUmeng;

        public UmAuthListener(Activity activity, ILoginUmeng iLoginUmeng) {
            this.mActivity = activity;
            this.mILoginUmeng = iLoginUmeng;
        }

        @Override
        public void onStart(SHARE_MEDIA share_media) {//授权开始的回调
            CustomProgress.show(mActivity, "授权中...", false, null);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int i, Map<String, String> data) {
            String openid = data.get("uid");
            String token = data.get("access_token");
            String name = data.get("name");
            String iconurl = data.get("iconurl");
            String plat = "";
            switch (platform.toString()) {
                case "SINA":
                    plat = "weibo";
                    //友盟统计
                    HashMap<String, String> map_weibo = new HashMap<String, String>();
                    map_weibo.put("evenname", "weibo登陆成功次数");
                    map_weibo.put("even", "登陆成功后+1");
                    MobclickAgent.onEvent(mActivity, "action28", map_weibo);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("登陆成功后+1")  //事件类别
                            .setAction("weibo登陆成功次数")      //事件操作
                            .build());
//                    PublicUtils.clearUMengOauth(mActivity);
                    break;
                case "QQ":
                    //友盟统计
                    HashMap<String, String> map_qq = new HashMap<String, String>();
                    map_qq.put("evenname", "qq登陆成功次数");
                    map_qq.put("even", "登陆成功后+1");
                    MobclickAgent.onEvent(mActivity, "action32", map_qq);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("登陆成功后+1")  //事件类别
                            .setAction("qq登陆成功次数")      //事件操作
                            .build());
                    plat = "qq";
                    PublicUtils.clearUMengOauth(mActivity);
                    break;
                case "WEIXIN":
                    //友盟统计
                    HashMap<String, String> map_weixin = new HashMap<String, String>();
                    map_weixin.put("evenname", "wechat登陆成功次数");
                    map_weixin.put("even", "登陆成功后+1");
                    MobclickAgent.onEvent(mActivity, "action30", map_weixin);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("登陆成功后+1")  //事件类别
                            .setAction("wechat登陆成功次数")      //事件操作
                            .build());
                    plat = "weixin";
                    PublicUtils.clearUMengOauth(mActivity);
                    break;
            }
//            PublicUtils.clearUMengOauth(mActivity);
            mILoginUmeng.loginUmengBack(openid, token, plat, name, iconurl);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            CustomProgress.cancelDialog();
            ToastUtils.showCenter(mActivity, mActivity.getString(R.string.um_auth_error));
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

            CustomProgress.cancelDialog();
            ToastUtils.showCenter(mActivity, mActivity.getString(R.string.um_auth_cancel));
        }
    }


    //10................................................................................................

    /**
     * 清除友盟授权
     *
     * @param activity
     */
    public static void clearUMengOauth(Activity activity) {
        UMShareAPI.get(activity).deleteOauth(activity, ClassConstant.UMengPlatform.platform_qq, umAuthListener);
        UMShareAPI.get(activity).deleteOauth(activity, ClassConstant.UMengPlatform.platform_weixin, umAuthListener);
        UMShareAPI.get(activity).deleteOauth(activity, ClassConstant.UMengPlatform.platform_sina, umAuthListener);
    }

    private static UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
        }
    };
    //11................................................................................................

    /**
     * 改变EditText的hint大小
     *
     * @param hint
     * @param editText
     * @param textSize
     */
    public static void changeEditTextHint(String hint, EditText editText, int textSize) {
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(hint);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(textSize, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

    //12................................................................................................

    /**
     * 登陆成功后，数据存储
     *
     * @param loginBean
     */
    public static void loginSucces(LoginBean loginBean) {

        SharedPreferencesUtils.writeBoolean(ClassConstant.LoginSucces.LOGIN_STATUS, true);
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.AUTH_TOKEN, loginBean.getAuth_token());
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.USER_ID, loginBean.getUser_info().getUser_id());
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.NIKE_NAME, loginBean.getUser_info().getNickname());
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.SLOGAN, loginBean.getUser_info().getSlogan());
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.BIG, loginBean.getUser_info().getAvatar().getBig());
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.THUMB, loginBean.getUser_info().getAvatar().getThumb());
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.EMAIL, loginBean.getUser_info().getEmail());
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.MOBILE, loginBean.getUser_info().getMobile());
        SharedPreferencesUtils.writeString(ClassConstant.LoginSucces.PROFESSION, loginBean.getUser_info().getProfession());

    }

    //12................................................................................................

    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }


    //13................................................................................................

    /**
     * 清除应用缓存
     *
     * @param context
     */
    public static void clearAppCache(Context context) {
        DataCleanManager.cleanInternalCache(UIUtils.getContext());
        DataCleanManager.cleanCustomCache(context.getCacheDir().getAbsolutePath());
        MPFileUtility.clearCacheContent(context);
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
    }
    //14................................................................................................

    /**
     * 清除SharedPreferencesUtils数据
     *
     * @param context
     */
    public static void clearShared(Context context) {
        SharedPreferencesUtils.writeBoolean(ClassConstant.LoginSucces.LOGIN_STATUS, false);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.AUTH_TOKEN);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.USER_ID);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.NIKE_NAME);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.SLOGAN);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.BIG);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.THUMB);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.EMAIL);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.MOBILE);
        SharedPreferencesUtils.clear(context, ClassConstant.LoginSucces.PROFESSION);
    }

    //14................................................................................................

    /**
     * 设置图片的高度
     *
     * @param mContext
     * @param bili
     * @param imageView
     */
    public static void setPicHeighAndWidth(Context mContext, float bili, ImageView imageView) {
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = (int) (div(imageView.getWidth(), bili, 2));
        imageView.setLayoutParams(layoutParams);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 取两个指定时间的日期差值
     *
     * @param begin  比较大的日期
     * @param end    比较小的日期
     * @param format 格式
     * @return
     */
    public static long diffDay(String begin, String end, String format) {
        long rday = 0l;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format.toString());
            Date cbegin = sdf.parse(begin);
            Date cend = sdf.parse(end);
            long l = cbegin.getTime() - cend.getTime();
            rday = l / (24 * 60 * 60 * 1000);
            if (l > 0 && rday * 24 * 60 * 60 * 1000 < l) {
                rday = rday + 1;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return rday;

    }

    /**
     * 设置tablayout的左右间距
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    /**
     * 获取状态栏高度
     * ！！这个方法来自StatusBarUtil,因为作者将之设为private，所以直接copy出来
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 安装App(支持6.0)
     *
     * @param context  上下文
     * @param filePath 文件路径
     */
    public static void installApp(Context context, String filePath) {
        installApp(context, FileUtils.getFileByPath(filePath));
    }

    /**
     * 安装App（支持6.0）
     *
     * @param context 上下文
     * @param file    文件
     */
    public static void installApp(Context context, File file) {
        if (!FileUtils.isFileExists(file)) return;
        context.startActivity(getInstallAppIntent(file));
    }

    /**
     * 获取安装App（支持6.0）的意图
     *
     * @param filePath 文件路径
     * @return intent
     */
    public static Intent getInstallAppIntent(String filePath) {
        return getInstallAppIntent(FileUtils.getFileByPath(filePath));
    }

    /**
     * 获取安装App(支持6.0)的意图
     *
     * @param file 文件
     * @return intent
     */
    public static Intent getInstallAppIntent(File file) {
        if (file == null) return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type;
        if (Build.VERSION.SDK_INT < 23) {
            type = "application/vnd.android.package-archive";
        } else {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(FileUtils.getFileExtension(file));
        }
        intent.setDataAndType(Uri.fromFile(file), type);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

}