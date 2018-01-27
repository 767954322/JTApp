package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.flyco.tablayout.CustomViewPagerTab;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.home.activity.SearchActivity;
import com.homechart.app.home.activity.SearchResultActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

@SuppressLint("ValidFragment")
public class NewSearchResultFragment
        extends BaseFragment
        implements View.OnClickListener{


    public NewSearchResultFragment() {
    }

    public NewSearchResultFragment(FragmentManager fragmentManager,String search_tag,String search_info) {
        this.fragmentManager = fragmentManager;
        this.search_tag = search_tag;
        this.search_info = search_info;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
    }

    @Override
    protected void initView() {

        cet_clearedit = (ClearEditText) rootView.findViewById(R.id.cet_clearedit);
        tv_quxiao = (TextView) rootView.findViewById(R.id.tv_quxiao);
        mViewPager = (CustomViewPagerTab) rootView.findViewById(R.id.vp_view);
        mViewPager.setScanScroll(true);
        mTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.tly_slidingtablayout);
        //设置下划线的高度
        mTabLayout.setIndicatorHeight(4f);
        mTabLayout.setIndicatorWidth(70f);
        //设置tab的字体大小
        mTabLayout.setTextsize(14f);
        initFragment();
        mViewPagerAdapter = new MyPagerAdapter(fragmentManager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setViewPager(mViewPager);

        //初始位置
//        mViewPager.setCurrentItem(0);

    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_quxiao.setOnClickListener(this);
        cet_clearedit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(activity.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    String searchContext = cet_clearedit.getText().toString().trim();
                    if (TextUtils.isEmpty(searchContext.trim())) {
                        ToastUtils.showCenter(activity, "请输入搜索内容");
                    } else {
                        search_info = searchContext;
                        search_tag = "";
                        searchPicFragment.setSearchInfo(search_info);
//                        searchArticleFragment.setSearchInfo(search_info);
                    }

                    return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(search_info)) {
            cet_clearedit.setText(search_info);
        } else {
            cet_clearedit.setText(search_tag);
        }
        cet_clearedit.setSelection(cet_clearedit.getText().length());
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_quxiao:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }

    private void initFragment() {
        searchPicFragment = new SearchPicFragment(search_info, search_tag);
        mFragmentsList.add(searchPicFragment);
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(activity);
        MobclickAgent.onPageStart("搜索列表页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("搜索列表页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("搜索列表页");
        MobclickAgent.onPause(activity);
    }


    private FragmentManager fragmentManager;
    private Bundle mBundle;
    private String search_info;
    private String search_tag;
    private ClearEditText cet_clearedit;
    private TextView tv_quxiao;
    private final String[] mTitles = {"图片"};
    private CustomViewPagerTab mViewPager;
    private SlidingTabLayout mTabLayout;
    //页卡标题集合
    private List<Fragment> mFragmentsList = new ArrayList<>();//页卡视图集合

    private MyPagerAdapter mViewPagerAdapter;
    private SearchPicFragment searchPicFragment;
}