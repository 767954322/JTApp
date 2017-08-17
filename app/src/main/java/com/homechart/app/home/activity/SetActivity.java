package com.homechart.app.home.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ActivityManager;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.myview.HomeSharedPopWin;
import com.homechart.app.myview.HomeSharedPopWinPublic;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.DataCleanManager;
import com.homechart.app.utils.MPFileUtility;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.alertview.AlertView;
import com.homechart.app.utils.alertview.OnItemClickListener;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/7.
 */

public class SetActivity
        extends BaseActivity
        implements View.OnClickListener,
        OnItemClickListener,
        HomeSharedPopWinPublic.ClickInter {
    private ImageButton mIBBack;
    private TextView mTVTital;
    private RelativeLayout rl_set_guanyu;
    private RelativeLayout rl_set_clear;
    private RelativeLayout rl_set_fankui;
    private RelativeLayout rl_set_tuijian;
    private AlertView mAlertView;
    private File cacheDir;
    private File filesDir;
    private File externalCacheDir;
    private TextView tv_clear_num;
    private String cacheSize;
    private Button btn_outlogin;
    private boolean ifClear = false;

    private HomeSharedPopWinPublic homeSharedPopWinPublic;
    private RelativeLayout rl_set_haoping;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set;
    }

    @Override
    protected void initView() {

        homeSharedPopWinPublic = new HomeSharedPopWinPublic(SetActivity.this, SetActivity.this);
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        tv_clear_num = (TextView) findViewById(R.id.tv_clear_num);
        rl_set_guanyu = (RelativeLayout) findViewById(R.id.rl_set_guanyu);
        rl_set_clear = (RelativeLayout) findViewById(R.id.rl_set_clear);
        rl_set_fankui = (RelativeLayout) findViewById(R.id.rl_set_fankui);
        rl_set_tuijian = (RelativeLayout) findViewById(R.id.rl_set_tuijian);
        btn_outlogin = (Button) findViewById(R.id.btn_outlogin);
        rl_set_haoping = (RelativeLayout) findViewById(R.id.rl_set_haoping);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mIBBack.setOnClickListener(this);
        rl_set_guanyu.setOnClickListener(this);
        rl_set_clear.setOnClickListener(this);
        rl_set_fankui.setOnClickListener(this);
        rl_set_tuijian.setOnClickListener(this);
        btn_outlogin.setOnClickListener(this);
        rl_set_haoping.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTVTital.setText("系统设置");

        cacheDir = getCacheDir();
        filesDir = getFilesDir();
        externalCacheDir = getExternalCacheDir();
        tv_clear_num.setText(getCacheSize());
        showDialog();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                SetActivity.this.finish();
                break;
            case R.id.rl_set_guanyu:
                Intent intent_guanyu = new Intent(SetActivity.this, GuanYuActivity.class);
                startActivity(intent_guanyu);

                break;
            case R.id.rl_set_clear:
                if (!ifClear) {
                    if (!tv_clear_num.getText().equals("0.0B")) {
                        PublicUtils.clearAppCache(this);
                        tv_clear_num.setText("0.0B");
                        ToastUtils.showCenter(SetActivity.this, "缓存已清完");

                    } else {
                        ToastUtils.showCenter(SetActivity.this, "暂无缓存数据");
                    }
                    ifClear = true;
                }

                break;
            case R.id.rl_set_fankui:
                PublicUtils.clearAppCache(SetActivity.this);
                Intent intent = new Intent(this, IssueBackActivity.class);
                startActivity(intent);

                break;
            case R.id.rl_set_tuijian:
                homeSharedPopWinPublic.showAtLocation(SetActivity.this.findViewById(R.id.main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                        0,
                        0); //设置layout在PopupWindow中显示的位置
                break;
            case R.id.btn_outlogin:
                //清除登陆数据
                PublicUtils.clearShared(SetActivity.this);
                //清除所有显示Activity
                ActivityManager.getInstance().exit(SetActivity.this);
                //清除友盟授权
                clearUMengOauth();

                Intent intent1 = new Intent(SetActivity.this, LoginActivity.class);
                startActivity(intent1);
                SetActivity.this.finish();
                break;
            case R.id.rl_set_haoping:
                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent2);
                } catch (Exception e) {
                    ToastUtils.showCenter(SetActivity.this, "您的手机没有安装Android应用市场");
                    e.printStackTrace();
                }

                break;
        }
    }

    /***
     * 提示框
     */
    private void showDialog() {
        mAlertView = new AlertView
                ("",
                        "确认清理缓存", UIUtils.getString(R.string.cancel), new String[]{UIUtils.getString(R.string.clear_cath_sure)},
                        null, this, AlertView.Style.Alert, this);
    }

    @Override
    public void onItemClick(Object object, int position) {
        if (object == mAlertView && position != AlertView.CANCELPOSITION) {
            if (getCacheSize().equals("0.0B")) {
                ToastUtils.showCenter(this, UIUtils.getString(R.string.no_cath));
            } else {
                CustomProgress.show(SetActivity.this, "清除缓存中...", false, null);
                PublicUtils.clearAppCache(this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_clear_num.setText("0.0B");
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(SetActivity.this, "缓存已清完");
                    }
                }, 1000);
            }
        } else {
            return;
        }
        return;
    }

    /**
     * file-普通的文件存储
     * database-数据库文件（.db文件）
     * sharedPreference-配置数据(.xml文件）
     * cache-图片缓存文件
     */
    private String getCacheSize() {
        try {
            //cacheSize = DataCleanManager.getCacheSize(cacheDir);
            double mpCachesize = DataCleanManager.getFolderSize(MPFileUtility.getCacheRootDirectoryHandle(this));
            double othercachesize = DataCleanManager.getFolderSize(cacheDir);
            cacheSize = DataCleanManager.getFormatSize(mpCachesize + othercachesize);
            ImageLoader.getInstance().clearMemoryCache();
            ImageLoader.getInstance().clearDiscCache();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cacheSize;
    }


    private void clearUMengOauth() {
        UMShareAPI.get(this).deleteOauth(SetActivity.this, ClassConstant.UMengPlatform.platform_qq, umAuthListener);
        UMShareAPI.get(this).deleteOauth(SetActivity.this, ClassConstant.UMengPlatform.platform_weixin, umAuthListener);
        UMShareAPI.get(this).deleteOauth(SetActivity.this, ClassConstant.UMengPlatform.platform_sina, umAuthListener);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
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

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("系统设置页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("系统设置页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("系统设置页");
        MobclickAgent.onPause(this);
    }

    private void sharedItemOpen(SHARE_MEDIA share_media) {
        UMImage image = new UMImage(SetActivity.this, R.drawable.icon_app);
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        UMWeb web = new UMWeb("https://h5.idcool.com.cn/appdownload");
        web.setTitle("分析生活之美，分享生活之美，就在家图");//标题
        web.setThumb(image);  //缩略图
        String desi = "家图app丨帮你实现舒适生活";
        if (share_media == SHARE_MEDIA.SINA) {
            desi = "分析生活之美，分享生活之美，就在家图。" + "@家图互动";
        }
        web.setDescription(desi);//描述
        new ShareAction(SetActivity.this).
                setPlatform(share_media).
                withMedia(web).
                setCallback(umShareListener).share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform == SHARE_MEDIA.WEIXIN) {
                ToastUtils.showCenter(SetActivity.this, "微信好友分享成功啦");
            } else if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
                ToastUtils.showCenter(SetActivity.this, "微信朋友圈分享成功啦");
            } else if (platform == SHARE_MEDIA.SINA) {
                ToastUtils.showCenter(SetActivity.this, "新浪微博分享成功啦");
            }else if (platform == SHARE_MEDIA.QQ) {
                ToastUtils.showCenter(SetActivity.this, "QQ分享成功啦");
            }

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.showCenter(SetActivity.this, "分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showCenter(SetActivity.this, "分享取消了");
        }
    };

    @Override
    public void onClickWeiXin() {
        sharedItemOpen(SHARE_MEDIA.WEIXIN);
    }

    @Override
    public void onClickPYQ() {
        sharedItemOpen(SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    @Override
    public void onClickWeiBo() {

        sharedItemOpen(SHARE_MEDIA.SINA);
    }

    @Override
    public void onClickQQ() {

        sharedItemOpen(SHARE_MEDIA.QQ);
    }


}
