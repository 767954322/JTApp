package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flyco.tablayout.CustomViewPagerTab;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.activity.HomeActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.imagedetail.ImageBenDiActivity;
import com.homechart.app.myview.CaiJiPopWin;
import com.homechart.app.myview.CaiJiPopWin1;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

@SuppressLint("ValidFragment")
public class NewMyPicCenterFragment
        extends BaseFragment
        implements View.OnClickListener,
        OtherLingGuanLiFragment.ChangeUI ,
        CaiJiPopWin1.ClickInter {

    private FragmentManager fragmentManager;
    private ImageButton mIBBack;
    private TextView mTVTital;
    private final String[] mTitles = {"上传&采集", "收藏"};
    private CustomViewPagerTab mViewPager;
    //页卡标题集合
    private List<Fragment> mFragmentsList = new ArrayList<>();//页卡视图集合
    private TextView tv_content_right;
    private String user_id;
    private MyPagerAdapter mViewPagerAdapter;
    private boolean ifAllowScroll = true;
    private NewCaiJiCenterFragment newCaiJiCenterFragment;
    private NewPicCenterFragment newPicCenterFragment;
    private SlidingTabLayout mTabLayout;
    private Bundle mBundle;
    private ImageButton nav_secondary_imageButton;
    private CaiJiPopWin1 caijiPop;

    public NewMyPicCenterFragment() {
    }

    public NewMyPicCenterFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_pic_center;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
    }

    @Override
    protected void initView() {

        mIBBack = (ImageButton) rootView.findViewById(R.id.nav_left_imageButton);
        tv_content_right = (TextView) rootView.findViewById(R.id.tv_content_right);
        nav_secondary_imageButton = (ImageButton) rootView.findViewById(R.id.nav_secondary_imageButton);
        mTVTital = (TextView) rootView.findViewById(R.id.tv_tital_comment);

        mViewPager = (CustomViewPagerTab) rootView.findViewById(R.id.vp_view);
        mTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.tly_slidingtablayout);
    }

    @Override
    public void ifShowDelete(boolean bool) {
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mIBBack.setOnClickListener(this);
        nav_secondary_imageButton.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
//                    nav_secondary_imageButton.setVisibility(View.VISIBLE);
                } else if (position == 1) {
//                    nav_secondary_imageButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        caijiPop = new CaiJiPopWin1(activity, this);
        mTVTital.setText("图片");
        nav_secondary_imageButton.setImageResource(R.drawable.fabu1);
        user_id = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);

        //设置下划线的高度
        mTabLayout.setIndicatorHeight(4f);
        mTabLayout.setIndicatorWidth(70f);
        //设置tab的字体大小
        mTabLayout.setTextsize(14f);
        newCaiJiCenterFragment = new NewCaiJiCenterFragment(fragmentManager);
        newPicCenterFragment = new NewPicCenterFragment(fragmentManager);
        mFragmentsList.add(newCaiJiCenterFragment);
        mFragmentsList.add(newPicCenterFragment);

        mViewPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setViewPager(mViewPager);
        //初始位置
        mViewPager.setCurrentItem(0);
        mViewPager.setScanScroll(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                ((HomeActivity) activity).hideBottom(false);
                fragmentManager.beginTransaction().remove(this).commit();
                break;
            case R.id.nav_secondary_imageButton:
                if (null != caijiPop) {
                    caijiPop.showAtLocation(activity.findViewById(R.id.id_main),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                            0,
                            0); //设置layout在PopupWindow中显示的位置
                } else {

                    caijiPop = new CaiJiPopWin1(activity, this);
                    caijiPop.showAtLocation(activity.findViewById(R.id.id_main),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                            0,
                            0); //设置layout在PopupWindow中显示的位置

                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 5 && resultCode == 5){
            newCaiJiCenterFragment.onRefresh();
            newCaiJiCenterFragment.onScrollTop();

            String item_id = data.getStringExtra("item_id");
            List<String> item_id_list = new ArrayList<>();
            item_id_list.add(item_id);
            NewImageDetailsFragment newImageDetailsFragment = new NewImageDetailsFragment(getChildFragmentManager());
            Bundle bundle = new Bundle();
            bundle.putSerializable("item_id", item_id);
            bundle.putInt("position", 0);
            bundle.putString("type", "single");
            bundle.putSerializable("item_id_list", (Serializable) item_id_list);
            newImageDetailsFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.id_main, newImageDetailsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("图片个人中心");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("图片个人中心");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("图片个人中心");
    }

    @Override
    public void xiangceCaiJi() {
        GalleryFinal.openGallerySingle(33, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList != null && resultList.size() > 0) {
                    Message message = new Message();
                    message.arg1 = 0;
                    message.obj = resultList.get(0).getPhotoPath().toString();
                    handler.sendMessage(message);
                } else {
                    ToastUtils.showCenter(activity, "图片资源获取失败");
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
            }
        });
    }

    @Override
    public void paizhaoCaiJi() {
        GalleryFinal.openCamera(33, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList != null && resultList.size() > 0) {
                    Message message = new Message();
                    message.arg1 = 0;
                    message.obj = resultList.get(0).getPhotoPath().toString();
                    handler.sendMessage(message);
                } else {
                    ToastUtils.showCenter(activity, "拍照资源获取失败");
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String info = (String) msg.obj;
            int tag = msg.arg1;
            if (tag == 0) {
                String url_Imag = (String) msg.obj;
                List<String> listUrl = new ArrayList<>();
                listUrl.add(url_Imag);
                Intent intent = new Intent(activity, ImageBenDiActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("pic_url_list", (Serializable) listUrl);
                bundle.putInt("click_position", 0);
                intent.putExtras(bundle);
                startActivityForResult(intent,5);
            }
        }
    };


}