package com.homechart.app.home.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.adapter.WelcomePagerAdapter;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.widget.SwipeViewPager;
import com.jaeger.library.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;

/**
 * @author allen .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file WelcomeActivity.java .
 * @brief 首次进入滑动页 .
 */
public class WelcomeActivity extends BaseActivity implements WelcomePagerAdapter.OnClickJump {

    private ViewPager mWelcomeViewPager;

    protected int getLayoutResId() {
        return R.layout.activity_welcome;
    }

    protected void initView() {
        // 全屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = this.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mWelcomeViewPager = (ViewPager) findViewById(R.id.welcome_view_pager);
    }

    protected void initData(Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
        if (getNextActivityToLaunch()) {
            //跳转到广告页
            Intent intent = new Intent(this, AdvertisementActivity.class);
            startActivity(intent);
            finish();
        } else {
            //友盟统计
            HashMap<String, String> map5 = new HashMap<String, String>();
            map5.put("evenname", "第一次打开app展示启动页");
            map5.put("even", "第一次打开app展示启动页");
            MobclickAgent.onEvent(WelcomeActivity.this, "newaction1", map5);
            //ga统计
            MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("第一次打开app展示启动页")  //事件类别
                    .setAction("第一次打开app展示启动页")  //事件操作
                    .build());
            WelcomePagerAdapter adapter = new WelcomePagerAdapter(WelcomeActivity.this, getAdData(), this);
//            //初始化轮播图下面小点
//            mWelcomeViewPager.updateIndicatorView(getAdData().size());
            mWelcomeViewPager.setAdapter(adapter);
            //设置权限
            PublicUtils.verifyStoragePermissions(WelcomeActivity.this);
        }
    }


    private boolean isLastPage = false;
    private boolean isDragPage = false;
    private boolean canJumpPage = true;
    @Override
    protected void initListener() {
        super.initListener();

        mWelcomeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (isLastPage && isDragPage && positionOffsetPixels == 0){   //当前页是最后一页，并且是拖动状态，并且像素偏移量为0
                    if (canJumpPage){
                        canJumpPage = false;
                        onClickThreeJump();
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                isLastPage = position == 2;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                isDragPage = state == 1;
            }
        });
    }

    public List<Integer> getAdData() {
        List<Integer> adList = new ArrayList<>();
        adList.add(R.drawable.a1);
        adList.add(R.drawable.a2);
        adList.add(R.drawable.a3);
        return adList;
    }

    private boolean getNextActivityToLaunch() {
        Boolean isfirst = SharedPreferencesUtils.readBoolean(ISFIRST);
        return isfirst;
    }

    /**
     * 权限的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断请求码
        switch (requestCode) {
            case 1:
                if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
                }
                break;
            case 2:
                //TODO 最后的权限回调
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
            ToastUtils.showCenter(WelcomeActivity.this, "再次点击返回键退出");
            mExitTime = System.currentTimeMillis();
        } else {
            WelcomeActivity.this.finish();
            System.exit(0);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!getNextActivityToLaunch()) {
            // Get tracker.
            Tracker t = MyApplication.getInstance().getDefaultTracker();
            // Set screen name.
            t.setScreenName("引导页");
            // Send a screen view.
            t.send(new HitBuilders.ScreenViewBuilder().build());
            MobclickAgent.onPageStart("引导页");
            MobclickAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("引导页");
        MobclickAgent.onPause(this);
        if (!getNextActivityToLaunch()) {
            MobclickAgent.onPageEnd("引导页");
            MobclickAgent.onPause(this);
        }
    }

    @Override
    public void onClickThreeJump() {
        //友盟统计
        HashMap<String, String> map5 = new HashMap<String, String>();
        map5.put("evenname", "点击启动页第三页进入app");
        map5.put("even", "引导页");
        MobclickAgent.onEvent(WelcomeActivity.this, "jtaction44", map5);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("引导页")  //事件类别
                .setAction("点击启动页第三页进入app")  //事件操作
                .build());
        SharedPreferencesUtils.writeBoolean(ISFIRST, true);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("if_first", true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClickButtonJump() {
        //友盟统计
        HashMap<String, String> map5 = new HashMap<String, String>();
        map5.put("evenname", "点击启动页跳过按钮进入app");
        map5.put("even", "引导页");
        MobclickAgent.onEvent(WelcomeActivity.this, "jtaction45", map5);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("引导页")  //事件类别
                .setAction("点击启动页跳过按钮进入app")  //事件操作
                .build());
        SharedPreferencesUtils.writeBoolean(ISFIRST, true);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("if_first", true);
        startActivity(intent);
        finish();
    }

    public final String ISFIRST = "isfirst";
}
