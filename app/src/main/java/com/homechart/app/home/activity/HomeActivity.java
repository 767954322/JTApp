package com.homechart.app.home.activity;


import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.gson.JsonArray;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.fragment.HomeCenterFragment;
import com.homechart.app.home.fragment.HomePicFragment;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.myview.SelectPicPopupWindow;
import com.homechart.app.myview.UpApkPopupWindow;
import com.homechart.app.upapk.BroadcastUtil;
import com.homechart.app.upapk.DialogUtils;
import com.homechart.app.upapk.DownloadService;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.visearch.EditPhotoActivity;
import com.homechart.app.visearch.PhotoActivity;
import com.jaeger.library.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by allen on 2017/6/1.
 */
public class HomeActivity
        extends BaseActivity
        implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener, BroadcastUtil.IReceiver {

    private RadioButton radio_btn_center;
    private RadioGroup mRadioGroup;
    private int jumpPosition = 0;

    private HomePicFragment mHomePicFragment;
    private Fragment mHomeDesignerFragment;
    private Fragment mHomeCenterFragment;
    private FragmentTransaction transaction;
    private Fragment mTagFragment;
    private ImageView iv_add_icon;
    private SelectPicPopupWindow menuWindow;
    private String type;
    private String download_url;
    private String object_id;
    private UpApkPopupWindow upApkPopupWindow;
    private String photo_id;
    private String activity_id;
    private String article_id;
    private RadioButton radio_btn_designer;
    private Boolean loginStatus;
    private RoundImageView riv_round_five;
    private RoundImageView riv_round_four;
    private RoundImageView riv_round_three;
    private RoundImageView riv_round_two;
    private RoundImageView riv_round_one;
    private RelativeLayout rl_shitu;
    private RelativeLayout rl_yindao2;
    private RelativeLayout rl_yindao1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

//        if_first =  getIntent().getBooleanExtra("if_first",false);
        String data = getIntent().getDataString();
        if (!TextUtils.isEmpty(data) && data.contains("photo")) {
            String[] str = data.split("photo");
            if (str.length > 1) {
                photo_id = str[1].substring(1, str[1].length());
            }
        } else if (!TextUtils.isEmpty(data) && data.contains("activity")) {
            String[] str = data.split("activity");
            if (str.length > 1) {
                activity_id = str[1].substring(1, str[1].length());
            }
        } else if (!TextUtils.isEmpty(data) && data.contains("article")) {
            String[] str = data.split("article");
            if (str.length > 1) {
                article_id = str[1].substring(1, str[1].length());
            }
        }
        type = getIntent().getStringExtra("type");
        object_id = getIntent().getStringExtra("object_id");
        loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
    }

    @Override
    protected void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_home_radio_group);
        radio_btn_center = (RadioButton) findViewById(R.id.radio_btn_center);
        radio_btn_designer = (RadioButton) findViewById(R.id.radio_btn_designer);
        iv_add_icon = (ImageView) findViewById(R.id.iv_add_icon);
        riv_round_five = (RoundImageView) findViewById(R.id.riv_round_five);
        riv_round_four = (RoundImageView) findViewById(R.id.riv_round_four);
        riv_round_three = (RoundImageView) findViewById(R.id.riv_round_three);
        riv_round_two = (RoundImageView) findViewById(R.id.riv_round_two);
        riv_round_one = (RoundImageView) findViewById(R.id.riv_round_one);
        rl_shitu = (RelativeLayout) findViewById(R.id.rl_shitu);
        rl_yindao1 = (RelativeLayout) findViewById(R.id.rl_yindao1);
        rl_yindao2 = (RelativeLayout) findViewById(R.id.rl_yindao2);
    }

    @Override
    protected void initListener() {
        super.initListener();
        riv_round_five.setOnClickListener(this);
        riv_round_four.setOnClickListener(this);
        riv_round_three.setOnClickListener(this);
        riv_round_two.setOnClickListener(this);
        riv_round_one.setOnClickListener(this);
        rl_shitu.setOnClickListener(this);
        rl_yindao1.setOnClickListener(this);
        rl_yindao2.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

//        int permission = ActivityCompat.checkSelfPermission(HomeActivity.this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(HomeActivity.this, PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE);
//        } else {
        initFragmentData();
//        }
    }

    private void initFragmentData() {
        if (findViewById(R.id.main_content) != null) {
            if (null == mHomePicFragment) {
                mHomePicFragment = new HomePicFragment(getSupportFragmentManager());
            }
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content, mHomePicFragment).commitAllowingStateLoss();
//            FragmentManager.executePendingTransactions();
        }
        ((RadioButton) mRadioGroup.findViewById(R.id.radio_btn_pic)).setChecked(true);
//        mRadioGroup.check(R.id.radio_btn_pic);
        mRadioGroup.setAlpha(0.96f);
        menuWindow = new SelectPicPopupWindow(HomeActivity.this, HomeActivity.this);
        upApkPopupWindow = new UpApkPopupWindow(this, this);
        BroadcastUtil.registerReceiver(this, this, "DITing.action.download");
        //检测是否有新版本
        checkNewAPK();
        if (!TextUtils.isEmpty(object_id)) {
            Intent intent = new Intent(HomeActivity.this, ArticleDetailsActivity.class);
            intent.putExtra("article_id", object_id);
            startActivity(intent);
        }
//        if (!SharedPreferencesUtils.readBoolean("yindao")) {
//            rl_yindao1.setVisibility(View.VISIBLE);
//            rl_yindao2.setVisibility(View.GONE);
//        }
        int permission = ActivityCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
        switch (checkedId) {
            case R.id.radio_btn_pic:
                jumpPosition = 0;
                if (null == mHomePicFragment) {
                    mHomePicFragment = new HomePicFragment(getSupportFragmentManager());
                }
                if (mTagFragment != mHomePicFragment) {
                    mTagFragment = mHomePicFragment;
                    replaceFragment(mHomePicFragment);
                }
                break;
            case R.id.radio_btn_designer:
            case R.id.iv_add_icon:
                if (jumpPosition == 0) {
                    mRadioGroup.check(R.id.radio_btn_pic);
                } else if (jumpPosition == 2) {
                    mRadioGroup.check(R.id.radio_btn_center);
                }

                if (ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeActivity.this,
                            new String[]{Manifest.permission.CAMERA}, 3);
                } else {
                    //友盟统计
                    HashMap<String, String> map4 = new HashMap<String, String>();
                    map4.put("evenname", "识图入口");
                    map4.put("even", "首页识别入口－图片识别");
                    MobclickAgent.onEvent(HomeActivity.this, "shijian6", map4);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("首页识别入口－图片识别")  //事件类别
                            .setAction("识图入口")      //事件操作
                            .build());
                    Intent intent1 = new Intent(HomeActivity.this, PhotoActivity.class);
                    startActivity(intent1);
                }
//                menuWindow.showAtLocation(HomeActivity.this.findViewById(R.id.main),
//                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
//                        0,
//                        0); //设置layout在PopupWindow中显示的位置
                break;
            case R.id.radio_btn_center:

                if (loginStatus) {
                    jumpPosition = 2;
                    if (null == mHomeCenterFragment) {
                        mHomeCenterFragment = new HomeCenterFragment(getSupportFragmentManager());
                    }
                    if (mTagFragment != mHomeCenterFragment) {
                        mTagFragment = mHomeCenterFragment;
                        replaceFragment(mHomeCenterFragment);
                    }
                } else {
                    if (ifJump) {
                        mRadioGroup.check(R.id.radio_btn_pic);
                        //友盟统计
                        HashMap<String, String> map4 = new HashMap<String, String>();
                        map4.put("evenname", "登录入口");
                        map4.put("even", "未登录状态下点击底部栏我的");
                        MobclickAgent.onEvent(HomeActivity.this, "shijian20", map4);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("未登录状态下点击底部栏我的")  //事件类别
                                .setAction("登录入口")      //事件操作
                                .build());
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivityForResult(intent, 1);
                    }
                }

                break;
        }
    }

    private boolean ifJump = true;

    public void changeShowPic() {
        ifJump = false;
        jumpPosition = 0;
        mRadioGroup.check(R.id.radio_btn_pic);
        mHomeCenterFragment = null;
        ifJump = true;
    }

    public void replaceFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.commitAllowingStateLoss();
    }

    private void takePhoto() {
        GalleryFinal.openCamera(0, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList != null && resultList.size() > 0) {
                    Message message = new Message();
                    message.obj = resultList.get(0).getPhotoPath().toString();
                    handler.sendMessage(message);
                } else {
                    ToastUtils.showCenter(HomeActivity.this, "拍照资源获取失败");
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tv_takephoto:
//                menuWindow.dismiss();
//                //android 6.0权限问题
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
//                    ToastUtils.showCenter(this, "执行了权限请求");
//                } else {
//                    takePhoto();
//                }
//                break;
            case R.id.tv_pic:
                menuWindow.dismiss();
                GalleryFinal.openGallerySingle(0, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (resultList != null && resultList.size() > 0) {
                            Message message = new Message();
                            message.obj = resultList.get(0).getPhotoPath().toString();
                            handler.sendMessage(message);
                        } else {
                            ToastUtils.showCenter(HomeActivity.this, "图片资源获取失败");
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {
                    }
                });
                break;
            case R.id.rl_pop_main:
                menuWindow.dismiss();
                break;
            case R.id.iv_bufabu:
                menuWindow.dismiss();
                break;
            case R.id.tv_go_up:
                //TODO 区服务器下载
                Intent downloadIntent = new Intent(HomeActivity.this, DownloadService.class);
                downloadIntent.putExtra("downloadUrl", download_url);
                downloadIntent.putExtra("fileName", "JTApp");
                startService(downloadIntent);
                break;
            case R.id.iv_close_up:
                if (upApkPopupWindow.isShowing()) {
                    upApkPopupWindow.dismiss();
                }

                break;

            case R.id.riv_round_five:
            case R.id.riv_round_four:
            case R.id.riv_round_three:
            case R.id.riv_round_two:
            case R.id.riv_round_one:
            case R.id.rl_shitu:
            case R.id.tv_takephoto:

                if (jumpPosition == 0) {
                    ((RadioButton) mRadioGroup.findViewById(R.id.radio_btn_pic)).setChecked(true);
//                    mRadioGroup.check(R.id.radio_btn_pic);
                } else if (jumpPosition == 2) {
                    ((RadioButton) mRadioGroup.findViewById(R.id.radio_btn_center)).setChecked(true);
//                    mRadioGroup.check(R.id.radio_btn_center);
                }
                if (ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeActivity.this,
                            new String[]{Manifest.permission.CAMERA}, 3);
                } else {
                    //友盟统计
                    HashMap<String, String> map6 = new HashMap<String, String>();
                    map6.put("evenname", "识图入口");
                    map6.put("even", "首页识别入口－图片识别");
                    MobclickAgent.onEvent(HomeActivity.this, "shijian6", map6);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("首页识别入口－图片识别")  //事件类别
                            .setAction("识图入口")      //事件操作
                            .build());
                    Intent intent1 = new Intent(HomeActivity.this, PhotoActivity.class);
                    startActivity(intent1);
                }
                break;
            case R.id.rl_yindao1:
                rl_yindao1.setVisibility(View.GONE);
                rl_yindao2.setVisibility(View.VISIBLE);
                //友盟统计
                HashMap<String, String> map7 = new HashMap<String, String>();
                map7.put("evenname", "新手指引");
                map7.put("even", "新手指引-第一张点击");
                MobclickAgent.onEvent(HomeActivity.this, "shijian8", map7);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("新手指引-第一张点击")  //事件类别
                        .setAction("新手指引")      //事件操作
                        .build());
                break;
            case R.id.rl_yindao2:
                SharedPreferencesUtils.writeBoolean("yindao", true);
                rl_yindao2.setVisibility(View.GONE);
                rl_yindao1.setVisibility(View.GONE);
                //友盟统计
                HashMap<String, String> map6 = new HashMap<String, String>();
                map6.put("evenname", "新手指引");
                map6.put("even", "新手指引-第二张点击");
                MobclickAgent.onEvent(HomeActivity.this, "shijian8", map6);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("新手指引-第二张点击")  //事件类别
                        .setAction("新手指引")      //事件操作
                        .build());
                break;
        }
    }


    //退出时的时间
    private long mExitTime;

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 3000) {
            ToastUtils.showCenter(HomeActivity.this, "再次点击返回键退出");
            mExitTime = System.currentTimeMillis();
        } else {
            HomeActivity.this.finish();
            System.exit(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    private void checkNewAPK() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        JSONObject jsonObject1 = new JSONObject(data_msg);
                        String download_url = jsonObject1.getString("download_url");
                        String last_version = jsonObject1.getString("last_version");
                        JSONArray jsonArray = jsonObject1.getJSONArray("text_content");
                        String current_code = PublicUtils.getVersionName(HomeActivity.this);
                        if (!current_code.trim().equals(last_version.trim())) {
                            //1.谈框是否更新
                            //2.点击更新，去下载apk
                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++)
                                    list_up_toast.add((String) jsonArray.get(i));
                            }
                            shouPop(download_url);
                        } else {
                            if (!TextUtils.isEmpty(photo_id)) {
                                List<String> item_id_list = new ArrayList<>();
                                item_id_list.add(photo_id);
                                Intent intent = new Intent(HomeActivity.this, ImageDetailScrollActivity.class);
                                intent.putExtra("item_id", photo_id);
                                intent.putExtra("type", "single");
                                intent.putExtra("position", 0);
                                intent.putExtra("item_id_list", (Serializable) item_id_list);
                                startActivity(intent);
                            } else if (!TextUtils.isEmpty(activity_id)) {
                                Intent intent = new Intent(HomeActivity.this, NewHuoDongDetailsActivity.class);
                                intent.putExtra("activity_id", activity_id);
                                startActivity(intent);
                            } else if (!TextUtils.isEmpty(article_id)) {
                                Intent intent = new Intent(HomeActivity.this, ArticleDetailsActivity.class);
                                intent.putExtra("article_id", article_id);
                                startActivity(intent);
                            }
                        }
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().checkLastVersion(callBack);

    }

    private void shouPop(String download_url) {
        this.download_url = download_url;
        HomeActivity.this.findViewById(R.id.main).post(new Runnable() {
            @Override
            public void run() {
                upApkPopupWindow.setData(list_up_toast);
                upApkPopupWindow.showAtLocation(HomeActivity.this.findViewById(R.id.main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                        0,
                        0); //设置layout在PopupWindow中显示的位置
            }
        });

    }

    @Override
    public void onReceive(Context ctx, Intent intent) {
        int progress = intent.getIntExtra("progress", -1);
        String path = intent.getStringExtra("path");
        boolean rename = intent.getBooleanExtra("rename", false);
        if (progress == 500 || progress == -1) {
            return;
        }
        if (progress == 100) {
            if (!TextUtils.isEmpty(path) && rename) {
                upApkPopupWindow.dismiss();
                PublicUtils.installApp(this, path);
                finish();
            } else {
                mHandle.sendEmptyMessage(0);
            }
        } else {
            upApkPopupWindow.changUi(progress);
            Log.d("test", getString(R.string.download_size, progress + " %"));
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String url_Imag = (String) msg.obj;

//            Intent intent1 = new Intent(HomeActivity.this, EditPhotoActivity.class);
//            intent1.putExtra("image_url", url_Imag);
//            intent1.putExtra("type", "location");
//            startActivity(intent1);

            Intent intent = new Intent(HomeActivity.this, FaBuActvity.class);
            intent.putExtra("image_path", url_Imag);
            intent.putExtra("type", "home");
            startActivity(intent);

        }
    };
    Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            upApkPopupWindow.dismiss();
            ToastUtils.showCenter(HomeActivity.this, "版本更新失败！");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BroadcastUtil.unRegisterReceiver(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:

                break;
            case 3:
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //获取到了权限
                    HashMap<String, String> map6 = new HashMap<String, String>();
                    map6.put("evenname", "识图入口");
                    map6.put("even", "首页识别入口－图片识别");
                    MobclickAgent.onEvent(HomeActivity.this, "shijian6", map6);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("首页识别入口－图片识别")  //事件类别
                            .setAction("识图入口")      //事件操作
                            .build());
                    Intent intent1 = new Intent(HomeActivity.this, PhotoActivity.class);
                    startActivity(intent1);
                } else {
//                    getAppDetailSettingIntent(this);
                    ToastUtils.showCenter(HomeActivity.this, "您没有授权该权限，请在设置中打开授权");
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
            if (loginStatus) {
                jumpPosition = 2;
                mRadioGroup.check(R.id.radio_btn_center);
            }
        }

    }

    private List<String> list_up_toast = new ArrayList<>();

    public static void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }
}

