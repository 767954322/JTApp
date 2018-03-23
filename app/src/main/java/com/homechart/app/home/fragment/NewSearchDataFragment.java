package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
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
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.homechart.app.R;
import com.homechart.app.commont.KeybordS;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

@SuppressLint("ValidFragment")
public class NewSearchDataFragment
        extends BaseFragment
        implements View.OnClickListener {

    private NewSearchResultFragment mNewSearchResultFragment;
    private NewSearchAblumResultFragment mNewSearchAblumResultFragment;
    private MyPagerAdapter mViewPagerAdapter;
    private String type;

    public NewSearchDataFragment() {
    }

    public NewSearchDataFragment(FragmentManager fragmentManager, String search_tag, String search_info) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_search_data;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        Bundle bundle = getArguments();
        search_info = bundle.getString("search_info");
        search_tag = bundle.getString("search_tag");
        type = bundle.getString("type");
    }

    @Override
    protected void initView() {

        cet_clearedit = (ClearEditText) rootView.findViewById(R.id.cet_clearedit);
        tv_quxiao = (TextView) rootView.findViewById(R.id.tv_quxiao);
        mTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.tly_slidingtablayout);
        mViewPager = (CustomViewPagerTab) rootView.findViewById(R.id.vp_view);
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_quxiao.setOnClickListener(this);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                KeybordS.closeKeybord(cet_clearedit, activity);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
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
                        //刷新列表
                        mNewSearchResultFragment.upData(search_info);
                        mNewSearchAblumResultFragment.upData(search_info);
                    }

                    return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //设置下划线的高度
        mTabLayout.setIndicatorHeight(4f);
        mTabLayout.setIndicatorWidth(70f);
        //设置tab的字体大小
        mTabLayout.setTextsize(14f);
        mNewSearchResultFragment = new NewSearchResultFragment(fragmentManager, search_tag, search_info);
        mNewSearchAblumResultFragment = new NewSearchAblumResultFragment(fragmentManager, search_tag, search_info);
        mFragmentsList.add(mNewSearchResultFragment);
        mFragmentsList.add(mNewSearchAblumResultFragment);

        mViewPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setViewPager(mViewPager);
        //初始位置
        mViewPager.setCurrentItem(0);
        mViewPager.setScanScroll(true);


        if (!TextUtils.isEmpty(search_info)) {
            cet_clearedit.setText(search_info);
        } else {
            cet_clearedit.setText(search_tag);
        }
        cet_clearedit.setSelection(cet_clearedit.getText().length());

        if (type.equals("album")) {
            mViewPager.setCurrentItem(1);
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_quxiao:
                KeybordS.closeKeybord(cet_clearedit, activity);
                fragmentManager.popBackStack();
                break;
        }
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

    private ClearEditText cet_clearedit;
    private FragmentManager fragmentManager;
    private TextView tv_quxiao;
    private SlidingTabLayout mTabLayout;
    private CustomViewPagerTab mViewPager;
    private List<Fragment> mFragmentsList = new ArrayList<>();//页卡视图集合
    private final String[] mTitles = {"图片", "灵感辑"};
    private String search_info;
    private String search_tag;

}