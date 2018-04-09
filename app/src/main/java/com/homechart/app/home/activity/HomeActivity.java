package com.homechart.app.home.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.fragment.HomeCenterFragment;
import com.homechart.app.home.fragment.HomeFaXianFragment;
import com.homechart.app.home.fragment.HomePicFragment1;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.myview.SelectPicPopupWindow;
import com.homechart.app.myview.UpApkPopupWindow;
import com.homechart.app.upapk.BroadcastUtil;
import com.homechart.app.upapk.DownloadService;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.visearch.PhotoActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by allen on 2017/6/1.
 */
public class HomeActivity
        extends BaseActivity
        implements View.OnClickListener, BroadcastUtil.IReceiver {

    private RadioButton radio_btn_center;
    private int jumpPosition = 0;

    private HomePicFragment1 mHomePicFragment;
    private HomeCenterFragment mHomeCenterFragment;
    private FragmentTransaction transaction;
    private Fragment mTagFragment;
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
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private HomeFaXianFragment mHomeFaXianFragment;
    private RelativeLayout rl_btn_pic;
    private RelativeLayout rl_btn_faxian;
    private RelativeLayout rl_btn_shibie;
    private RelativeLayout rl_btn_center;
    private ImageView iv_pic;
    private ImageView iv_faxian;
    private ImageView iv_shibie;
    private ImageView iv_center;
    private TextView tv_pic;
    private TextView tv_faxian;
    private TextView tv_shibie;
    private TextView tv_center;
    private int position = 1;
    //退出时的时间
    private long mExitTime;
    private LinearLayout rl_bottom_test;
    private RoundImageView riv_unreader_msg;
    private RelativeLayout rl_copy;
    private TextView tv_url;
    private ClipboardManager cm;
    boolean ifHide = true;
    private Timer timer_copy = new Timer(true);
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
        radio_btn_center = (RadioButton) findViewById(R.id.radio_btn_center);
        radio_btn_designer = (RadioButton) findViewById(R.id.radio_btn_designer);


        rl_bottom_test = (LinearLayout) findViewById(R.id.rl_bottom_test);
        rl_btn_pic = (RelativeLayout) findViewById(R.id.rl_btn_pic);
        rl_btn_faxian = (RelativeLayout) findViewById(R.id.rl_btn_faxian);
        rl_btn_shibie = (RelativeLayout) findViewById(R.id.rl_btn_shibie);
        rl_btn_center = (RelativeLayout) findViewById(R.id.rl_btn_center);

        rl_copy = (RelativeLayout) findViewById(R.id.rl_copy);
        tv_url = (TextView) findViewById(R.id.tv_url);

        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        iv_faxian = (ImageView) findViewById(R.id.iv_faxian);
        iv_shibie = (ImageView) findViewById(R.id.iv_shibie);
        iv_center = (ImageView) findViewById(R.id.iv_center);

        tv_pic = (TextView) findViewById(R.id.tv_pic);
        tv_faxian = (TextView) findViewById(R.id.tv_faxian);
        tv_shibie = (TextView) findViewById(R.id.tv_shibie);
        tv_center = (TextView) findViewById(R.id.tv_center);

        riv_unreader_msg = (RoundImageView) findViewById(R.id.riv_unreader_msg);
    }

    @Override
    protected void initListener() {
        super.initListener();
        rl_btn_pic.setOnClickListener(this);
        rl_btn_faxian.setOnClickListener(this);
        rl_btn_shibie.setOnClickListener(this);
        rl_btn_center.setOnClickListener(this);
        rl_copy.setOnClickListener(this);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {

        loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
        switch (v.getId()) {
            case R.id.rl_btn_pic:
                if (position != 1) {
                    position = 1;
                    changeBottomUI(1);
                    getSupportFragmentManager().beginTransaction().hide(mHomeCenterFragment).hide(mHomeFaXianFragment).show(mHomePicFragment).commit();
                } else {
                    int before = ClassConstant.HomeStatus.IMAGE_STATUS;
                    FragmentManager fragmentManager = mHomePicFragment.getChildFragmentManager();
                    List<Fragment> list = fragmentManager.getFragments();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            try {
                                if (null != list.get(i).getFragmentManager()) {
                                    list.get(i).getFragmentManager().popBackStack();
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                    if (before == 0) {
                        mHomePicFragment.scrollRecyclerView();
                    } else {
                        ClassConstant.HomeStatus.IMAGE_STATUS = 0;
                    }
                }
                break;
            case R.id.rl_btn_faxian:
                if (position != 2) {
                    position = 2;
                    changeBottomUI(2);
                    getSupportFragmentManager().beginTransaction().hide(mHomePicFragment).hide(mHomeCenterFragment).show(mHomeFaXianFragment).commit();
                } else {
                    int before = ClassConstant.HomeStatus.FAXIAN_STATUS;
                    FragmentManager fragmentManager = mHomeFaXianFragment.getChildFragmentManager();
                    List<Fragment> list = fragmentManager.getFragments();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            try {
                                if (null != list.get(i).getFragmentManager()) {
                                    list.get(i).getFragmentManager().popBackStack();
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                    if (before == 0) {
                        mHomeFaXianFragment.scrollRecyclerView();
                    } else {
                        ClassConstant.HomeStatus.FAXIAN_STATUS = 0;
                    }
                }
                break;
            case R.id.rl_btn_shibie:
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
                break;
            case R.id.rl_btn_center:
                if (position != 4) {
                    if (loginStatus) {
                        position = 4;
                        changeBottomUI(4);
                        mHomeCenterFragment.flushData();
                        getSupportFragmentManager().beginTransaction().hide(mHomePicFragment).hide(mHomeFaXianFragment).show(mHomeCenterFragment).commit();
                    } else {
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
                } else {
                    FragmentManager fragmentManager = mHomeCenterFragment.getChildFragmentManager();
                    List<Fragment> list = fragmentManager.getFragments();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            try {
                                if (null != list.get(i).getFragmentManager()) {
                                    list.get(i).getFragmentManager().popBackStack();
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                }
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
            case R.id.rl_copy:
                rl_copy.setVisibility(View.GONE);
                String urlWeb = cm.getText().toString().trim();
                cm.setText("");
                ifHide = true;
                Intent intentcopy = new Intent(HomeActivity.this, MyWebViewActivity.class);
                intentcopy.putExtra("weburl", urlWeb);
                startActivity(intentcopy);
                break;
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (findViewById(R.id.main_content) != null) {
            if (null == mHomePicFragment) {
                mHomePicFragment = new HomePicFragment1(getSupportFragmentManager());
            }
            if (null == mHomeFaXianFragment) {
                mHomeFaXianFragment = new HomeFaXianFragment(getSupportFragmentManager());
            }
            if (null == mHomeCenterFragment) {
                mHomeCenterFragment = new HomeCenterFragment(getSupportFragmentManager());
            }
            transaction = getSupportFragmentManager().beginTransaction();
            getSupportFragmentManager().beginTransaction().add(R.id.main_content, mHomePicFragment).
                    add(R.id.main_content, mHomeFaXianFragment).add(R.id.main_content, mHomeCenterFragment).commit();
            getSupportFragmentManager().beginTransaction().hide(mHomeCenterFragment).hide(mHomeFaXianFragment).show(mHomePicFragment).commit();
        }
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
        int permission = ActivityCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
        timer_copy.schedule(task_copy, 100, 500);
    }
    //任务
    private TimerTask task_copy = new TimerTask() {
        public void run() {
            try {
                String textCopy = cm.getText().toString();
                Message message = new Message();
                message.obj = textCopy;
                message.what = 1;
                mHandler.sendMessage(message);
            } catch (Exception e) {
                rl_copy.setVisibility(View.GONE);
            }
        }
    };
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code = msg.what;

            switch (code) {
                case 1:
                    String textMsg = (String) msg.obj;
                    if (!TextUtils.isEmpty(textMsg)) {
                        if (PublicUtils.isValidUrl(textMsg)) {
                            //显示
                            rl_copy.setVisibility(View.VISIBLE);
                            tv_url.setText(textMsg.trim());
                            if (ifHide) {
                                if(PublicUtils.isForeground(HomeActivity.this)){
                                    ifHide = false;
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Message message = new Message();
                                            message.what = 2;
                                            mHandler.sendMessage(message);
                                        }
                                    },5000);
                                }
                            }
                        } else {
                            //不显示
                            rl_copy.setVisibility(View.GONE);
                        }
                    } else {
                        //不显示
                        rl_copy.setVisibility(View.GONE);
                        ifHide = true;
                    }
                    break;
                case 2:
                    cm.setText("");
                    //不显示
                    rl_copy.setVisibility(View.GONE);
                    ifHide = true;
                    break;
            }

        }
    };

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
            case 24:
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   mHomeCenterFragment.paizhaoCaiJi();
                } else {
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

                position = 4;
                changeBottomUI(4);
                mHomeCenterFragment.flushData();
                getSupportFragmentManager().beginTransaction().hide(mHomePicFragment).hide(mHomeFaXianFragment).show(mHomeCenterFragment).commit();

            }
        }
    }

    private List<String> list_up_toast = new ArrayList<>();

    private void changeBottomUI(int clickNum) {
        switch (clickNum) {
            case 1:
                iv_pic.setImageResource(R.drawable.shouye);
                iv_faxian.setImageResource(R.drawable.faxian1);
                iv_center.setImageResource(R.drawable.wode1);
                tv_pic.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                tv_faxian.setTextColor(UIUtils.getColor(R.color.bg_262626));
                tv_center.setTextColor(UIUtils.getColor(R.color.bg_262626));
                break;
            case 2:

                iv_pic.setImageResource(R.drawable.shouye1);
                iv_faxian.setImageResource(R.drawable.faxian);
                iv_center.setImageResource(R.drawable.wode1);
                tv_pic.setTextColor(UIUtils.getColor(R.color.bg_262626));
                tv_faxian.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                tv_center.setTextColor(UIUtils.getColor(R.color.bg_262626));
                break;
            case 4:

                iv_pic.setImageResource(R.drawable.shouye1);
                iv_faxian.setImageResource(R.drawable.faxian1);
                iv_center.setImageResource(R.drawable.wode);

                tv_pic.setTextColor(UIUtils.getColor(R.color.bg_262626));
                tv_faxian.setTextColor(UIUtils.getColor(R.color.bg_262626));
                tv_center.setTextColor(UIUtils.getColor(R.color.bg_e79056));
                break;
        }
    }

    public void backPic() {
        if (position != 1) {
            position = 1;
            changeBottomUI(1);
            getSupportFragmentManager().beginTransaction().hide(mHomeCenterFragment).hide(mHomeFaXianFragment).show(mHomePicFragment).commit();
        } else {

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String url_Imag = (String) msg.obj;
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


    public void hideBottom(boolean boo) {
        if(boo){
            rl_bottom_test.setVisibility(View.GONE);
        }else {
            rl_bottom_test.setVisibility(View.VISIBLE);
        }
    }


   public void visiableUnRedIcon(boolean bool){
        if(bool){
            riv_unreader_msg.setVisibility(View.VISIBLE);
        }else {
            riv_unreader_msg.setVisibility(View.GONE);
        }
    }

}

