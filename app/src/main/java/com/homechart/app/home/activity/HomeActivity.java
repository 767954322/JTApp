package com.homechart.app.home.activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import com.homechart.app.myview.SelectPicPopupWindow;
import com.homechart.app.myview.UpApkPopupWindow;
import com.homechart.app.upapk.BroadcastUtil;
import com.homechart.app.upapk.DialogUtils;
import com.homechart.app.upapk.DownloadService;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.jaeger.library.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        photo_id = getIntent().getStringExtra("photo_id");
        activity_id = getIntent().getStringExtra("activity_id");
        article_id = getIntent().getStringExtra("article_id");

        type = getIntent().getStringExtra("type");
        object_id = getIntent().getStringExtra("object_id");
    }

    @Override
    protected void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_home_radio_group);
        radio_btn_center = (RadioButton) findViewById(R.id.radio_btn_center);
        iv_add_icon = (ImageView) findViewById(R.id.iv_add_icon);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (findViewById(R.id.main_content) != null) {
            if (null == mHomePicFragment) {
                mHomePicFragment = new HomePicFragment(getSupportFragmentManager());
            }
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content, mHomePicFragment).commitAllowingStateLoss();
        }
        mRadioGroup.check(R.id.radio_btn_pic);
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
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.radio_btn_pic:
                if (null == mHomePicFragment) {
                    mHomePicFragment = new HomePicFragment(getSupportFragmentManager());
                }
                if (mTagFragment != mHomePicFragment) {
                    mTagFragment = mHomePicFragment;
                    replaceFragment(mHomePicFragment);
                }
                jumpPosition = 0;
                break;
            case R.id.radio_btn_designer:
            case R.id.iv_add_icon:
//                if (null == mHomeDesignerFragment) {
//                    mHomeDesignerFragment = new HomeDesignerFragment(getSupportFragmentManager());
//                }
//                if (mTagFragment != mHomeDesignerFragment) {
//                    mTagFragment = mHomeDesignerFragment;
//                    replaceFragment(mHomeDesignerFragment);
//                }
//                jumpPosition = 1;

                //友盟统计
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("evenname", "发布入口");
                map.put("even", "首页");
                MobclickAgent.onEvent(HomeActivity.this, "jtaction1", map);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("首页")  //事件类别
                        .setAction("发布入口")      //事件操作
                        .build());

                if (jumpPosition == 0) {
                    mRadioGroup.check(R.id.radio_btn_pic);
                } else if (jumpPosition == 2) {
                    mRadioGroup.check(R.id.radio_btn_center);
                }
                menuWindow.showAtLocation(HomeActivity.this.findViewById(R.id.main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                        0,
                        0); //设置layout在PopupWindow中显示的位置
                break;
            case R.id.radio_btn_center:
                if (null == mHomeCenterFragment) {
                    mHomeCenterFragment = new HomeCenterFragment(getSupportFragmentManager());
                }
                if (mTagFragment != mHomeCenterFragment) {
                    mTagFragment = mHomeCenterFragment;
                    replaceFragment(mHomeCenterFragment);
                }
                jumpPosition = 2;
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_takephoto:
                menuWindow.dismiss();
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


                break;
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
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;

        }
        return super.dispatchTouchEvent(ev);
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
                                Intent intent = new Intent(HomeActivity.this, ImageDetailLongActivity.class);
                                intent.putExtra("item_id", photo_id);
                                startActivity(intent);
                            }else if(!TextUtils.isEmpty(activity_id)){
                                Intent intent = new Intent(HomeActivity.this, HuoDongDetailsActivity.class);
                                intent.putExtra("activity_id", activity_id);
                                startActivity(intent);
                            }else if(!TextUtils.isEmpty(article_id)){
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
            Intent intent = new Intent(HomeActivity.this, FaBuActvity.class);
            intent.putExtra("image_path", url_Imag);
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

    private List<String> list_up_toast = new ArrayList<>();
}

