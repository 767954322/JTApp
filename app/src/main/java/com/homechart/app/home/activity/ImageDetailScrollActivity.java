package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.imagedetail.ImageDetailBean;
import com.homechart.app.home.fragment.ImageDetailFragment;
import com.homechart.app.myview.HomeSharedPopWinPublic;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gumenghao on 17/6/26.
 */

public class ImageDetailScrollActivity
        extends BaseActivity
        implements View.OnClickListener,
        ImageDetailFragment.UserInfo ,
        HomeSharedPopWinPublic.ClickInter{
    private String item_id;
    private String mUserId;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private int mPosition;
    private List<String> mItemIdList;
    private ViewPager mViewPager;
    private MyImagePageAdater mAdapter;
    private TextView tv_content_right;
    private ImageButton nav_secondary_imageButton;
    private ImageDetailBean mImageDetailBean;
    private HomeSharedPopWinPublic homeSharedPopWinPublic;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_detail_scroll;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        item_id = getIntent().getStringExtra("item_id");
        mPosition = getIntent().getIntExtra("position", -1);
        mItemIdList = (List<String>) getIntent().getSerializableExtra("item_id_list");
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initView() {

        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        nav_secondary_imageButton = (ImageButton) findViewById(R.id.nav_secondary_imageButton);
        mViewPager = (ViewPager) findViewById(R.id.vp_viewpager);
        homeSharedPopWinPublic = new HomeSharedPopWinPublic(ImageDetailScrollActivity.this, ImageDetailScrollActivity.this);

    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        nav_secondary_imageButton.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        tv_tital_comment.setText("图片详情");
        mAdapter = new MyImagePageAdater(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                ImageDetailScrollActivity.this.finish();
                break;
            case R.id.tv_content_right:
                if (mImageDetailBean != null) {
                    Intent intent = new Intent(ImageDetailScrollActivity.this, ImageEditActvity.class);
                    intent.putExtra("image_value", mImageDetailBean);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.nav_secondary_imageButton:
                wei = 0;
                if (mImageDetailBean != null) {
                    homeSharedPopWinPublic.showAtLocation(ImageDetailScrollActivity.this.findViewById(R.id.menu_layout),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                            0,
                            0); //设置layout在PopupWindow中显示的位置
                }
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("图片详情页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("图片详情页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("图片详情页");
        MobclickAgent.onPause(this);
    }

    @Override
    public void getUserInfo(ImageDetailBean imageDetailBean) {

        this.mImageDetailBean = imageDetailBean;
        if (null != imageDetailBean) {
            if (!imageDetailBean.getUser_info().getUser_id().trim().equals(mUserId.trim())) {
                nav_secondary_imageButton.setVisibility(View.VISIBLE);
                tv_content_right.setVisibility(View.GONE);
                nav_secondary_imageButton.setImageResource(R.drawable.shared_icon);
            } else {
                tv_content_right.setVisibility(View.VISIBLE);
                nav_secondary_imageButton.setVisibility(View.GONE);
                tv_content_right.setText("编辑");
            }
        }

    }

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

    private void sharedItemOpen(SHARE_MEDIA share_media) {

        if (share_media == SHARE_MEDIA.WEIXIN) {
            //友盟统计
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("evenname", "分享图片");
            if (wei == 0) {
                map1.put("even", "图片详情+上+微信好友");
            } else {
                map1.put("even", "图片详情+下+微信好友");
            }
            MobclickAgent.onEvent(ImageDetailScrollActivity.this, "jtaction7", map1);
            //ga统计
            if (wei == 0) {
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("图片详情+上+微信好友")  //事件类别
                        .setAction("分享图片")      //事件操作
                        .build());
            } else {
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("图片详情+下+微信好友")  //事件类别
                        .setAction("分享图片")      //事件操作
                        .build());
            }


        } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
            //友盟统计
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("evenname", "分享图片");
            if (wei == 0) {
                map1.put("even", "图片详情+上+微信朋友圈");
            } else {
                map1.put("even", "图片详情+下+微信朋友圈");
            }
            MobclickAgent.onEvent(ImageDetailScrollActivity.this, "jtaction7", map1);
            //ga统计
            if (wei == 0) {
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("图片详情+上+微信朋友圈")  //事件类别
                        .setAction("分享图片")      //事件操作
                        .build());
            } else {
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("图片详情+下+微信朋友圈")  //事件类别
                        .setAction("分享图片")      //事件操作
                        .build());
            }
        } else if (share_media == SHARE_MEDIA.SINA) {
            //友盟统计
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("evenname", "分享图片");
            if (wei == 0) {
                map1.put("even", "图片详情+上+新浪微博");
            } else {
                map1.put("even", "图片详情+下+新浪微博");
            }
            MobclickAgent.onEvent(ImageDetailScrollActivity.this, "jtaction7", map1);
            //ga统计
            if (wei == 0) {
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("图片详情+上+新浪微博")  //事件类别
                        .setAction("分享图片")      //事件操作
                        .build());
            } else {
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("图片详情+下+新浪微博")  //事件类别
                        .setAction("分享图片")      //事件操作
                        .build());
            }
        }
        UMImage image = new UMImage(ImageDetailScrollActivity.this, mImageDetailBean.getItem_info().getImage().getImg0());
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        UMWeb web = new UMWeb("http://h5.idcool.com.cn/photo/" + mImageDetailBean.getItem_info().getItem_id());
        web.setTitle("「" + mImageDetailBean.getUser_info().getNickname() + "」在晒家｜家图APP");//标题
        web.setThumb(image);  //缩略图
        String desi = mImageDetailBean.getItem_info().getDescription() + mImageDetailBean.getItem_info().getTag();
        if (desi.length() > 160) {
            desi = desi.substring(0, 160) + "...";
        }
        web.setDescription(desi);//描述
        new ShareAction(ImageDetailScrollActivity.this).
                setPlatform(share_media).
                withMedia(web).
                setCallback(umShareListener).share();
    }

    public class MyImagePageAdater extends FragmentStatePagerAdapter {

        public MyImagePageAdater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ImageDetailFragment(mItemIdList.get(position), ImageDetailScrollActivity.this);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return mItemIdList.size();
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform == SHARE_MEDIA.WEIXIN) {
                ToastUtils.showCenter(ImageDetailScrollActivity.this, "微信好友分享成功啦");
            } else if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
                ToastUtils.showCenter(ImageDetailScrollActivity.this, "微信朋友圈分享成功啦");
            } else if (platform == SHARE_MEDIA.SINA) {
                ToastUtils.showCenter(ImageDetailScrollActivity.this, "新浪微博分享成功啦");
            }

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.showCenter(ImageDetailScrollActivity.this, "分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showCenter(ImageDetailScrollActivity.this, "分享取消了");
        }
    };
    private int wei = 0;
}
