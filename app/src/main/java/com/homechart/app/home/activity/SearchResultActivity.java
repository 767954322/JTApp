package com.homechart.app.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.flyco.tablayout.CustomViewPagerTab;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.search.SearchDataBean;
import com.homechart.app.home.bean.search.SearchDataColorBean;
import com.homechart.app.home.bean.search.SearchItemDataBean;
import com.homechart.app.home.bean.shouye.DataBean;
import com.homechart.app.home.bean.shouye.SYDataBean;
import com.homechart.app.home.fragment.SearchArticleFragment;
import com.homechart.app.home.fragment.SearchPicFragment;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gumenghao on 17/6/14.
 */

public class SearchResultActivity
        extends BaseActivity
        implements View.OnClickListener {

    private String search_info;
    private String search_tag;
    private ClearEditText cet_clearedit;
    private TextView tv_quxiao;
    private final String[] mTitles = {"图片", "文章"};
    private CustomViewPagerTab mViewPager;
    private SlidingTabLayout mTabLayout;
    //页卡标题集合
    private List<Fragment> mFragmentsList = new ArrayList<>();//页卡视图集合
    private MyPagerAdapter mViewPagerAdapter;
    private SearchPicFragment searchPicFragment;
    private SearchArticleFragment searchArticleFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        search_info = (String) getIntent().getSerializableExtra("search_info");
        search_tag = (String) getIntent().getSerializableExtra("search_tag");
    }

    @Override
    protected void initView() {
        cet_clearedit = (ClearEditText) findViewById(R.id.cet_clearedit);
        tv_quxiao = (TextView) findViewById(R.id.tv_quxiao);
        mViewPager = (CustomViewPagerTab) findViewById(R.id.vp_view);
        mViewPager.setScanScroll(true);
        mTabLayout = (SlidingTabLayout) findViewById(R.id.tly_slidingtablayout);
        //设置下划线的高度
        mTabLayout.setIndicatorHeight(4f);
        mTabLayout.setIndicatorWidth(70f);
        //设置tab的字体大小
        mTabLayout.setTextsize(14f);
        initFragment();
        mViewPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setViewPager(mViewPager);

        //初始位置
        mViewPager.setCurrentItem(0);
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
    protected void initListener() {
        super.initListener();
        tv_quxiao.setOnClickListener(this);
        cet_clearedit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchResultActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    String searchContext = cet_clearedit.getText().toString().trim();
                    if (TextUtils.isEmpty(searchContext.trim())) {
                        ToastUtils.showCenter(SearchResultActivity.this, "请输入搜索内容");
                    } else {
                        search_info = searchContext;
                        search_tag = "";
                        searchPicFragment.setSearchInfo(search_info);
                        searchArticleFragment.setSearchInfo(search_info);

                        //友盟统计
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("evenname", "搜索词");
                        map.put("even", search_info);
                        MobclickAgent.onEvent(SearchResultActivity.this, "jtaction22", map);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory(search_info)  //事件类别
                                .setAction("搜索词")      //事件操作
                                .build());
                    }

                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_quxiao:
                Intent intent = new Intent(SearchResultActivity.this, SearchActivity.class);
                setResult(1, intent);
                SearchResultActivity.this.finish();
                break;
        }
    }

    private void initFragment() {
        searchPicFragment = new SearchPicFragment(search_info, search_tag);
        searchArticleFragment = new SearchArticleFragment(search_info, search_tag);
        mFragmentsList.add(searchPicFragment);
        mFragmentsList.add(searchArticleFragment);

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
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("搜索列表页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("搜索列表页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("搜索列表页");
        MobclickAgent.onPause(this);
    }
}
