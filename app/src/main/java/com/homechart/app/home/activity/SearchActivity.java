package com.homechart.app.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.KeybordS;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.hotwords.HotWordsBean;
import com.homechart.app.home.bean.searchremind.RemindBean;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.myview.FlowLayoutSearch;
import com.homechart.app.myview.MyListView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.myview.RoundJiaoImageView;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.glide.GlideImgManager;
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
 * Created by gumenghao on 17/6/13.
 */

public class SearchActivity
        extends BaseActivity
        implements View.OnClickListener {


    private List<String> mListRemindTags = new ArrayList<>();
    private RelativeLayout rl_pic_all;
    private RoundJiaoImageView iv_pic_one;
    private RoundJiaoImageView iv_pic_two;
    private RoundJiaoImageView iv_pic_three;
    private View view_below_pics;
    private RelativeLayout rl_more_pic;
    private RelativeLayout rl_more_ling;
    private TextView tv_result_pic_tital;
    private View view_result_list;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {

        tv_quxiao = (TextView) findViewById(R.id.tv_quxiao);
        his_flowLayout = (FlowLayoutSearch) findViewById(R.id.his_flowLayout);
        cet_clearedit = (ClearEditText) findViewById(R.id.cet_clearedit);
        lv_hostory_list = (MyListView) findViewById(R.id.lv_hostory_list);
        rl_search_hostory = (RelativeLayout) findViewById(R.id.rl_search_hostory);
        sv_hostory = (ScrollView) findViewById(R.id.sv_hostory);
        sv_result = (ScrollView) findViewById(R.id.sv_result);
        iv_delete_hostory = (ImageView) findViewById(R.id.iv_delete_hostory);


        rl_more_pic = (RelativeLayout) findViewById(R.id.rl_more_pic);
        rl_album_all = (RelativeLayout) findViewById(R.id.rl_album_all);
        rl_images_one = (RelativeLayout) findViewById(R.id.rl_images_one);
        rl_images_two = (RelativeLayout) findViewById(R.id.rl_images_two);
        rl_images_three = (RelativeLayout) findViewById(R.id.rl_images_three);
        rl_header1 = (RelativeLayout) findViewById(R.id.rl_header1);
        rl_header2 = (RelativeLayout) findViewById(R.id.rl_header2);
        rl_header3 = (RelativeLayout) findViewById(R.id.rl_header3);
        riv_header_one = (RoundImageView) findViewById(R.id.riv_header_one);
        riv_header_two = (RoundImageView) findViewById(R.id.riv_header_two);
        riv_header_three = (RoundImageView) findViewById(R.id.riv_header_three);
        iv_img1 = (ImageView) findViewById(R.id.iv_img1);
        iv_img2 = (ImageView) findViewById(R.id.iv_img2);
        iv_img3 = (ImageView) findViewById(R.id.iv_img3);
        iv_img4 = (ImageView) findViewById(R.id.iv_img4);
        iv_img5 = (ImageView) findViewById(R.id.iv_img5);
        iv_img6 = (ImageView) findViewById(R.id.iv_img6);
        iv_img7 = (ImageView) findViewById(R.id.iv_img7);
        iv_img8 = (ImageView) findViewById(R.id.iv_img8);
        iv_img9 = (ImageView) findViewById(R.id.iv_img9);
        iv_img10 = (ImageView) findViewById(R.id.iv_img10);
        iv_img11 = (ImageView) findViewById(R.id.iv_img11);
        iv_img12 = (ImageView) findViewById(R.id.iv_img12);

        rl_result_image = (RelativeLayout) findViewById(R.id.rl_result_image);
        rl_result_ling = (RelativeLayout) findViewById(R.id.rl_result_ling);
        rl_pic_all = (RelativeLayout) findViewById(R.id.rl_pic_all);
        rl_more_ling = (RelativeLayout) findViewById(R.id.rl_more_ling);
        lv_result_list = (MyListView) findViewById(R.id.lv_result_list);

        iv_pic_one = (RoundJiaoImageView) findViewById(R.id.iv_pic_one);
        iv_pic_two = (RoundJiaoImageView) findViewById(R.id.iv_pic_two);
        iv_pic_three = (RoundJiaoImageView) findViewById(R.id.iv_pic_three);
        view_below_pics = findViewById(R.id.view_below_pics);
        tv_result_pic_tital = (TextView)findViewById(R.id.tv_result_pic_tital);
        view_result_list = findViewById(R.id.view_result_list);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        width_Imgs = (PublicUtils.getScreenWidth(SearchActivity.this) - UIUtils.getDimens(R.dimen.font_50)) / 3;
        getTagData();
        mListHostory = PublicUtils.getSearchHostory();
        if (mListHostory.size() > 0) {
            rl_search_hostory.setVisibility(View.VISIBLE);
            lv_hostory_list.setVisibility(View.VISIBLE);
            hostoryAdapter = new HostoryAdapter(SearchActivity.this, mListHostory);
            lv_hostory_list.setAdapter(hostoryAdapter);
        } else {
            rl_search_hostory.setVisibility(View.GONE);
            lv_hostory_list.setVisibility(View.GONE);
        }
        initLing();
        initPic();
        myPicTagsAdapter = new MyPicTagsAdapter(SearchActivity.this, mListRemindTags);
        lv_result_list.setAdapter(myPicTagsAdapter);

    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_quxiao.setOnClickListener(this);
        iv_pic_one.setOnClickListener(this);
        iv_pic_two.setOnClickListener(this);
        iv_pic_three.setOnClickListener(this);
        rl_images_one.setOnClickListener(this);
        rl_images_two.setOnClickListener(this);
        rl_images_three.setOnClickListener(this);
        iv_delete_hostory.setOnClickListener(this);
        rl_more_pic.setOnClickListener(this);
        rl_more_ling.setOnClickListener(this);
        cet_clearedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str_Search = s.toString();
                mSearchStr = str_Search.trim();
                if (str_Search.trim().equals("")) {
                    mRemindBean = null;
                    sv_hostory.setVisibility(View.VISIBLE);
                    sv_result.setVisibility(View.GONE);
                } else {
                    //TODO 去获取相关字段
                    getSearchResult(mSearchStr);
                }

            }
        });
        cet_clearedit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    search();
                    return true;
                }

                return false;
            }
        });

    }


    private void initPic() {

        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) iv_pic_one.getLayoutParams();
        layoutParams1.width = width_Imgs;
        layoutParams1.height = width_Imgs;
        layoutParams1.alignWithParent = true;
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) iv_pic_two.getLayoutParams();
        layoutParams2.width = width_Imgs;
        layoutParams2.height = width_Imgs;
        layoutParams2.alignWithParent = true;
        layoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) iv_pic_three.getLayoutParams();
        layoutParams3.width = width_Imgs;
        layoutParams3.height = width_Imgs;
        layoutParams3.alignWithParent = true;
        layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        iv_pic_one.setLayoutParams(layoutParams1);
        iv_pic_two.setLayoutParams(layoutParams2);
        iv_pic_three.setLayoutParams(layoutParams3);
    }

    private void initLing() {
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) rl_images_one.getLayoutParams();
        layoutParams1.width = width_Imgs;
        layoutParams1.height = width_Imgs;
        layoutParams1.alignWithParent = true;
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) rl_images_two.getLayoutParams();
        layoutParams2.width = width_Imgs;
        layoutParams2.height = width_Imgs;
        layoutParams2.alignWithParent = true;
        layoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) rl_images_three.getLayoutParams();
        layoutParams3.width = width_Imgs;
        layoutParams3.height = width_Imgs;
        layoutParams3.alignWithParent = true;
        layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        rl_images_one.setLayoutParams(layoutParams1);
        rl_images_two.setLayoutParams(layoutParams2);
        rl_images_three.setLayoutParams(layoutParams3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_quxiao:
                KeybordS.closeKeybord(cet_clearedit,SearchActivity.this);
                SearchActivity.this.finish();
                break;
            case R.id.iv_delete_hostory:
                //友盟统计
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("evenname", "搜索历史清空");
                map.put("even", "搜索历史清空");
                MobclickAgent.onEvent(SearchActivity.this, "shijian57", map);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("搜索历史清空")  //事件类别
                        .setAction("搜索历史清空")      //事件操作
                        .build());
                SharedPreferencesUtils.writeString(ClassConstant.SearchHestory.HESTORY_SEARCH, "");
                rl_search_hostory.setVisibility(View.GONE);
                lv_hostory_list.setVisibility(View.GONE);
                ToastUtils.showCenter(SearchActivity.this, "已清空");
                break;
            case R.id.iv_pic_one:

                //友盟统计
                HashMap<String, String> map4 = new HashMap<String, String>();
                map4.put("evenname", "搜索前三图点击");
                map4.put("even", "搜索前三图点击");
                MobclickAgent.onEvent(SearchActivity.this, "shijian61", map4);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("搜索前三图点击")  //事件类别
                        .setAction("搜索前三图点击")      //事件操作
                        .build());

                if (null != mRemindBean && null != mRemindBean.getItem_list() && mRemindBean.getItem_list().size() > 0) {
                    if(!TextUtils.isEmpty(cet_clearedit.getText().toString().trim())){
                        PublicUtils.replaceSearchHostory(cet_clearedit.getText().toString());
                    }
                    Intent intent = new Intent();
                    intent.putExtra("item_id", mRemindBean.getItem_list().get(0).getItem_info().getItem_id());
                    setResult(17, intent);
                    SearchActivity.this.finish();
                }
                break;
            case R.id.iv_pic_two:

                //友盟统计
                HashMap<String, String> map5 = new HashMap<String, String>();
                map5.put("evenname", "搜索前三图点击");
                map5.put("even", "搜索前三图点击");
                MobclickAgent.onEvent(SearchActivity.this, "shijian61", map5);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("搜索前三图点击")  //事件类别
                        .setAction("搜索前三图点击")      //事件操作
                        .build());
                if (null != mRemindBean && null != mRemindBean.getItem_list() && mRemindBean.getItem_list().size() > 1) {
                    if(!TextUtils.isEmpty(cet_clearedit.getText().toString().trim())){
                        PublicUtils.replaceSearchHostory(cet_clearedit.getText().toString());
                    }
                    Intent intent = new Intent();
                    intent.putExtra("item_id", mRemindBean.getItem_list().get(1).getItem_info().getItem_id());
                    setResult(17, intent);
                    SearchActivity.this.finish();
                }
                break;
            case R.id.iv_pic_three:

                //友盟统计
                HashMap<String, String> map6 = new HashMap<String, String>();
                map6.put("evenname", "搜索前三图点击");
                map6.put("even", "搜索前三图点击");
                MobclickAgent.onEvent(SearchActivity.this, "shijian61", map6);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("搜索前三图点击")  //事件类别
                        .setAction("搜索前三图点击")      //事件操作
                        .build());
                if (null != mRemindBean && null != mRemindBean.getItem_list() && mRemindBean.getItem_list().size() > 2) {
                    if(!TextUtils.isEmpty(cet_clearedit.getText().toString().trim())){
                        PublicUtils.replaceSearchHostory(cet_clearedit.getText().toString());
                    }
                    Intent intent = new Intent();
                    intent.putExtra("item_id", mRemindBean.getItem_list().get(2).getItem_info().getItem_id());
                    setResult(17, intent);
                    SearchActivity.this.finish();
                }
                break;
            case R.id.rl_more_pic:

                //友盟统计
                HashMap<String, String> map1 = new HashMap<String, String>();
                map1.put("evenname", "更多图片搜索");
                map1.put("even", "更多图片搜索");
                MobclickAgent.onEvent(SearchActivity.this, "shijian58", map1);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("更多图片搜索")  //事件类别
                        .setAction("更多图片搜索")      //事件操作
                        .build());

                if(!TextUtils.isEmpty(cet_clearedit.getText().toString().trim())){
                    PublicUtils.replaceSearchHostory(cet_clearedit.getText().toString().trim());
                    Intent intent = new Intent();
                    intent.putExtra("search_info", cet_clearedit.getText().toString().trim());
                    intent.putExtra("search_tag", "");
                    setResult(10, intent);
                    SearchActivity.this.finish();
                }
                break;
            case R.id.rl_images_one:

                //友盟统计
                HashMap<String, String> map2 = new HashMap<String, String>();
                map2.put("evenname", "搜索前三灵感辑点击");
                map2.put("even", "搜索前三灵感辑点击");
                MobclickAgent.onEvent(SearchActivity.this, "shijian60", map2);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("搜索前三灵感辑点击")  //事件类别
                        .setAction("搜索前三灵感辑点击")      //事件操作
                        .build());

                if (null != mRemindBean && null != mRemindBean.getAlbum_list() && mRemindBean.getAlbum_list().size() > 0) {
                    if(!TextUtils.isEmpty(cet_clearedit.getText().toString().trim())){
                        PublicUtils.replaceSearchHostory(cet_clearedit.getText().toString());
                    }
                    Intent intent = new Intent();
                    intent.putExtra("album_id", mRemindBean.getAlbum_list().get(0).getAlbum_info().getAlbum_id());
                    setResult(18, intent);
                    SearchActivity.this.finish();
                }
                break;
            case R.id.rl_images_two:

                //友盟统计
                HashMap<String, String> map3 = new HashMap<String, String>();
                map3.put("evenname", "搜索前三灵感辑点击");
                map3.put("even", "搜索前三灵感辑点击");
                MobclickAgent.onEvent(SearchActivity.this, "shijian60", map3);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("搜索前三灵感辑点击")  //事件类别
                        .setAction("搜索前三灵感辑点击")      //事件操作
                        .build());

                if (null != mRemindBean && null != mRemindBean.getAlbum_list() && mRemindBean.getAlbum_list().size() > 1) {
                    if(!TextUtils.isEmpty(cet_clearedit.getText().toString().trim())){
                        PublicUtils.replaceSearchHostory(cet_clearedit.getText().toString());
                    }
                    Intent intent = new Intent();
                    intent.putExtra("album_id", mRemindBean.getAlbum_list().get(1).getAlbum_info().getAlbum_id());
                    setResult(18, intent);
                    SearchActivity.this.finish();
                }
                break;
            case R.id.rl_images_three:

                //友盟统计
                HashMap<String, String> map8 = new HashMap<String, String>();
                map8.put("evenname", "搜索前三灵感辑点击");
                map8.put("even", "搜索前三灵感辑点击");
                MobclickAgent.onEvent(SearchActivity.this, "shijian60", map8);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("搜索前三灵感辑点击")  //事件类别
                        .setAction("搜索前三灵感辑点击")      //事件操作
                        .build());
                if (null != mRemindBean && null != mRemindBean.getAlbum_list() && mRemindBean.getAlbum_list().size() > 2) {
                    if(!TextUtils.isEmpty(cet_clearedit.getText().toString().trim())){
                        PublicUtils.replaceSearchHostory(cet_clearedit.getText().toString());
                    }
                    Intent intent = new Intent();
                    intent.putExtra("album_id", mRemindBean.getAlbum_list().get(2).getAlbum_info().getAlbum_id());
                    setResult(18, intent);
                    SearchActivity.this.finish();
                }
                break;
            case R.id.rl_more_ling:

                //友盟统计
                HashMap<String, String> map7 = new HashMap<String, String>();
                map7.put("evenname", "更多灵感辑搜索");
                map7.put("even", "更多灵感辑搜索");
                MobclickAgent.onEvent(SearchActivity.this, "shijian59", map7);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("更多灵感辑搜索")  //事件类别
                        .setAction("更多灵感辑搜索")      //事件操作
                        .build());
                if(!TextUtils.isEmpty(cet_clearedit.getText().toString().trim())){
                    PublicUtils.replaceSearchHostory(cet_clearedit.getText().toString().trim());
                    Intent intent = new Intent();
                    intent.putExtra("search_info", cet_clearedit.getText().toString().trim());
                    intent.putExtra("search_tag", "");
                    setResult(19, intent);
                    SearchActivity.this.finish();
                }

                break;
        }
    }

    private void getTagData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(SearchActivity.this, getString(R.string.searchtag_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        Message msg = new Message();
                        msg.obj = data_msg;
                        handler.sendMessage(msg);

                    } else {
                        ToastUtils.showCenter(SearchActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getSearchHotWords(callBack);

    }

    public void getSearchResult(String resultStr) {


        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(SearchActivity.this, getString(R.string.searchtag_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        Message msg = new Message();
                        msg.obj = data_msg;
                        mHandler.sendMessage(msg);

                    } else {
                        ToastUtils.showCenter(SearchActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getSearchWorldData(resultStr, callBack);

    }

    // 搜索功能
    private void search() {

        String searchContext = cet_clearedit.getText().toString().trim();
        if (TextUtils.isEmpty(searchContext.trim())) {
            ToastUtils.showCenter(this, "请输入搜索内容");
        } else {
            PublicUtils.replaceSearchHostory(searchContext.trim());
            Intent intent = new Intent();
            intent.putExtra("search_info", searchContext);
            intent.putExtra("search_tag", "");
            setResult(10, intent);
            SearchActivity.this.finish();
        }

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String dataStr = (String) msg.obj;
            HotWordsBean hotWordsBean = GsonUtil.jsonToBean(dataStr, HotWordsBean.class);
            List<String> listHot = hotWordsBean.getHot_words();
            myData = new String[listHot.size()];
            for (int i = 0; i < listHot.size(); i++) {
                myData[i] = listHot.get(i);
            }
            his_flowLayout.setColorful(false);
            his_flowLayout.setData(myData);
            his_flowLayout.setOnTagClickListener(new FlowLayoutSearch.OnTagClickListener() {
                @Override
                public void TagClick(String text) {
                    PublicUtils.replaceSearchHostory(text.trim());
                    Intent intent = new Intent();
                    intent.putExtra("search_tag", text);
                    intent.putExtra("search_info", "");
                    setResult(10, intent);
                    SearchActivity.this.finish();
                }
            });
        }
    };


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String dataStr = (String) msg.obj;
            RemindBean remindBean = GsonUtil.jsonToBean(dataStr, RemindBean.class);
            if (null != remindBean && remindBean.getKw().trim().equals(mSearchStr)) {
                mRemindBean = remindBean;
                changeSearchUI(remindBean);
            }
        }
    };

    private void changeSearchUI(RemindBean remindBean) {

        sv_hostory.setVisibility(View.GONE);
        sv_result.setVisibility(View.VISIBLE);
        if ((null != remindBean.getWord_list() && remindBean.getWord_list().size() > 0) ||
                (null != remindBean.getItem_list() && remindBean.getItem_list().size() > 0)) {
            rl_result_image.setVisibility(View.VISIBLE);
        } else {
            rl_result_image.setVisibility(View.GONE);
        }
        if (null != remindBean.getAlbum_list() && remindBean.getAlbum_list().size() > 0) {
            rl_result_ling.setVisibility(View.VISIBLE);
        } else {
            rl_result_ling.setVisibility(View.GONE);
        }

        //文字提示
        if (null != remindBean.getWord_list() && remindBean.getWord_list().size() > 0) {
            mListRemindTags.clear();
            mListRemindTags.addAll(remindBean.getWord_list());
            myPicTagsAdapter.notifyDataSetChanged();
            lv_result_list.setVisibility(View.VISIBLE);
        } else {
            lv_result_list.setVisibility(View.GONE);
        }
//        String urlTest = "https://t12.baidu.com/it/u=1107254696,794684901&fm=173&app=25&f=JPEG?w=500&h=333&s=6F00498B417183D45659AC170300E0C3";
        //图片提示
        if (null != remindBean.getItem_list() && remindBean.getItem_list().size() > 0) {
            tv_result_pic_tital.setVisibility(View.VISIBLE);
            view_result_list.setVisibility(View.VISIBLE);
            if (remindBean.getItem_list().size() == 1) {
//                GlideImgManager.glideLoader(SearchActivity.this, urlTest, R.color.white, R.color.white, iv_pic_one);
                GlideImgManager.glideLoader(SearchActivity.this, remindBean.getItem_list().get(0).getItem_info().getImage_url(), R.color.white, R.color.white, iv_pic_one);
                iv_pic_two.setVisibility(View.GONE);
                iv_pic_three.setVisibility(View.GONE);
                iv_pic_one.setVisibility(View.VISIBLE);
            } else if (remindBean.getItem_list().size() == 2) {
//                GlideImgManager.glideLoader(SearchActivity.this, urlTest, R.color.white, R.color.white, iv_pic_one);
//                GlideImgManager.glideLoader(SearchActivity.this, urlTest, R.color.white, R.color.white, iv_pic_two);
                GlideImgManager.glideLoader(SearchActivity.this, remindBean.getItem_list().get(0).getItem_info().getImage_url(), R.color.white, R.color.white, iv_pic_one);
                GlideImgManager.glideLoader(SearchActivity.this, remindBean.getItem_list().get(1).getItem_info().getImage_url(), R.color.white, R.color.white, iv_pic_two);
                iv_pic_three.setVisibility(View.GONE);
                iv_pic_two.setVisibility(View.VISIBLE);
                iv_pic_one.setVisibility(View.VISIBLE);
            } else if (remindBean.getItem_list().size() >= 3) {
//                GlideImgManager.glideLoader(SearchActivity.this, urlTest, R.color.white, R.color.white, iv_pic_one);
//                GlideImgManager.glideLoader(SearchActivity.this, urlTest, R.color.white, R.color.white, iv_pic_two);
//                GlideImgManager.glideLoader(SearchActivity.this, urlTest, R.color.white, R.color.white, iv_pic_three);
                GlideImgManager.glideLoader(SearchActivity.this, remindBean.getItem_list().get(0).getItem_info().getImage_url(), R.color.white, R.color.white, iv_pic_one);
                GlideImgManager.glideLoader(SearchActivity.this, remindBean.getItem_list().get(1).getItem_info().getImage_url(), R.color.white, R.color.white, iv_pic_two);
                GlideImgManager.glideLoader(SearchActivity.this, remindBean.getItem_list().get(2).getItem_info().getImage_url(), R.color.white, R.color.white, iv_pic_three);
                iv_pic_three.setVisibility(View.VISIBLE);
                iv_pic_two.setVisibility(View.VISIBLE);
                iv_pic_one.setVisibility(View.VISIBLE);
            }
            rl_pic_all.setVisibility(View.VISIBLE);
            view_below_pics.setVisibility(View.VISIBLE);
        } else {
            tv_result_pic_tital.setVisibility(View.GONE);
            view_result_list.setVisibility(View.GONE);
            rl_pic_all.setVisibility(View.GONE);
            view_below_pics.setVisibility(View.GONE);
        }

        if (null != remindBean.getAlbum_list() && remindBean.getAlbum_list().size() > 0) {
            //设置灵感辑图片
            RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) iv_img1.getLayoutParams();
            layoutParams1.width = width_Imgs / 2 - UIUtils.getDimens(R.dimen.font_2);
            layoutParams1.height = width_Imgs / 2 - UIUtils.getDimens(R.dimen.font_2);
            layoutParams1.alignWithParent = true;

            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) iv_img2.getLayoutParams();
            layoutParams2.width = width_Imgs / 2 - UIUtils.getDimens(R.dimen.font_2);
            layoutParams2.height = width_Imgs / 2 - UIUtils.getDimens(R.dimen.font_2);
            layoutParams2.alignWithParent = true;
            layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

            RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) iv_img3.getLayoutParams();
            layoutParams3.width = width_Imgs / 2 - UIUtils.getDimens(R.dimen.font_2);
            layoutParams3.height = width_Imgs / 2 - UIUtils.getDimens(R.dimen.font_2);
            layoutParams3.alignWithParent = true;
            layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

            RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) iv_img4.getLayoutParams();
            layoutParams4.width = width_Imgs / 2 - UIUtils.getDimens(R.dimen.font_2);
            layoutParams4.height = width_Imgs / 2 - UIUtils.getDimens(R.dimen.font_2);
            layoutParams4.alignWithParent = true;
            layoutParams4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM | RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

            iv_img1.setLayoutParams(layoutParams1);
            iv_img2.setLayoutParams(layoutParams2);
            iv_img3.setLayoutParams(layoutParams3);
            iv_img4.setLayoutParams(layoutParams4);
            iv_img5.setLayoutParams(layoutParams1);
            iv_img6.setLayoutParams(layoutParams2);
            iv_img7.setLayoutParams(layoutParams3);
            iv_img8.setLayoutParams(layoutParams4);
            iv_img9.setLayoutParams(layoutParams1);
            iv_img10.setLayoutParams(layoutParams2);
            iv_img11.setLayoutParams(layoutParams3);
            iv_img12.setLayoutParams(layoutParams4);

            if (remindBean.getAlbum_list().size() == 1) {
                rl_images_one.setVisibility(View.VISIBLE);
                rl_images_two.setVisibility(View.GONE);
                rl_images_three.setVisibility(View.GONE);
                ImageUtils.displayRoundImage(remindBean.getAlbum_list().get(0).getAlbum_info().getAvatar(), riv_header_one);
            } else if (remindBean.getAlbum_list().size() == 2) {
                rl_images_one.setVisibility(View.VISIBLE);
                rl_images_two.setVisibility(View.VISIBLE);
                rl_images_three.setVisibility(View.GONE);
                rl_header1.setVisibility(View.VISIBLE);
                rl_header2.setVisibility(View.VISIBLE);
                rl_header3.setVisibility(View.GONE);
                ImageUtils.displayRoundImage(remindBean.getAlbum_list().get(0).getAlbum_info().getAvatar(), riv_header_one);
                ImageUtils.displayRoundImage(remindBean.getAlbum_list().get(1).getAlbum_info().getAvatar(), riv_header_two);
            } else if (remindBean.getAlbum_list().size() >= 3) {
                rl_images_one.setVisibility(View.VISIBLE);
                rl_images_two.setVisibility(View.VISIBLE);
                rl_images_three.setVisibility(View.VISIBLE);
                rl_header1.setVisibility(View.VISIBLE);
                rl_header2.setVisibility(View.VISIBLE);
                rl_header3.setVisibility(View.VISIBLE);
                ImageUtils.displayRoundImage(remindBean.getAlbum_list().get(0).getAlbum_info().getAvatar(), riv_header_one);
                ImageUtils.displayRoundImage(remindBean.getAlbum_list().get(1).getAlbum_info().getAvatar(), riv_header_two);
                ImageUtils.displayRoundImage(remindBean.getAlbum_list().get(2).getAlbum_info().getAvatar(), riv_header_three);
            }
            if (remindBean.getAlbum_list().size() > 0 &&
                    null != remindBean.getAlbum_list().get(0).getAlbum_info() &&
                    null != remindBean.getAlbum_list().get(0).getAlbum_info().getImages() &&
                    remindBean.getAlbum_list().get(0).getAlbum_info().getImages().size() > 0) {
                ImageUtils.displayFilletLeftTopImage(remindBean.getAlbum_list().get(0).getAlbum_info().getImages().get(0), iv_img1);
            } else {
                ImageUtils.displayFilletLeftTopImage("drawable://" + R.drawable.test_color, iv_img1);
            }
            if (remindBean.getAlbum_list().size() > 0 &&
                    null != remindBean.getAlbum_list().get(0).getAlbum_info() &&
                    null != remindBean.getAlbum_list().get(0).getAlbum_info().getImages() &&
                    remindBean.getAlbum_list().get(0).getAlbum_info().getImages().size() > 1) {
                ImageUtils.displayFilletRightTopImage(remindBean.getAlbum_list().get(0).getAlbum_info().getImages().get(1), iv_img2);
            } else {
                ImageUtils.displayFilletRightTopImage("drawable://" + R.drawable.test_color, iv_img2);
            }
            if (remindBean.getAlbum_list().size() > 0 &&
                    null != remindBean.getAlbum_list().get(0).getAlbum_info() &&
                    null != remindBean.getAlbum_list().get(0).getAlbum_info().getImages() &&
                    remindBean.getAlbum_list().get(0).getAlbum_info().getImages().size() > 2) {
                ImageUtils.displayFilletLeftBottomImage(remindBean.getAlbum_list().get(0).getAlbum_info().getImages().get(2), iv_img3);
            } else {
                ImageUtils.displayFilletLeftBottomImage("drawable://" + R.drawable.test_color, iv_img3);
            }
            if (remindBean.getAlbum_list().size() > 0 &&
                    null != remindBean.getAlbum_list().get(0).getAlbum_info() &&
                    null != remindBean.getAlbum_list().get(0).getAlbum_info().getImages() &&
                    remindBean.getAlbum_list().get(0).getAlbum_info().getImages().size() > 3) {
                ImageUtils.displayFilletRightBottomImage(remindBean.getAlbum_list().get(0).getAlbum_info().getImages().get(3), iv_img4);
            } else {
                ImageUtils.displayFilletRightBottomImage("drawable://" + R.drawable.test_color, iv_img4);
            }
            if (remindBean.getAlbum_list().size() > 1 &&
                    null != remindBean.getAlbum_list().get(1).getAlbum_info() &&
                    null != remindBean.getAlbum_list().get(1).getAlbum_info().getImages() &&
                    remindBean.getAlbum_list().get(1).getAlbum_info().getImages().size() > 0) {
                ImageUtils.displayFilletLeftTopImage(remindBean.getAlbum_list().get(1).getAlbum_info().getImages().get(0), iv_img5);
            } else {
                ImageUtils.displayFilletLeftTopImage("drawable://" + R.drawable.test_color, iv_img5);
            }
            if (remindBean.getAlbum_list().size() > 1 &&
                    null != remindBean.getAlbum_list().get(1).getAlbum_info() &&
                    null != remindBean.getAlbum_list().get(1).getAlbum_info().getImages() &&
                    remindBean.getAlbum_list().get(1).getAlbum_info().getImages().size() > 1) {
                ImageUtils.displayFilletRightTopImage(remindBean.getAlbum_list().get(1).getAlbum_info().getImages().get(1), iv_img6);
            } else {
                ImageUtils.displayFilletRightTopImage("drawable://" + R.drawable.test_color, iv_img6);
            }
            if (remindBean.getAlbum_list().size() > 1 &&
                    null != remindBean.getAlbum_list().get(1).getAlbum_info() &&
                    null != remindBean.getAlbum_list().get(1).getAlbum_info().getImages() &&
                    remindBean.getAlbum_list().get(1).getAlbum_info().getImages().size() > 2) {
                ImageUtils.displayFilletLeftBottomImage(remindBean.getAlbum_list().get(1).getAlbum_info().getImages().get(2), iv_img7);
            } else {
                ImageUtils.displayFilletLeftBottomImage("drawable://" + R.drawable.test_color, iv_img7);
            }
            if (remindBean.getAlbum_list().size() > 1 &&
                    null != remindBean.getAlbum_list().get(1).getAlbum_info() &&
                    null != remindBean.getAlbum_list().get(1).getAlbum_info().getImages() &&
                    remindBean.getAlbum_list().get(1).getAlbum_info().getImages().size() > 3) {
                ImageUtils.displayFilletRightBottomImage(remindBean.getAlbum_list().get(1).getAlbum_info().getImages().get(3), iv_img8);
            } else {
                ImageUtils.displayFilletRightBottomImage("drawable://" + R.drawable.test_color, iv_img8);
            }
            if (remindBean.getAlbum_list().size() > 2 &&
                    null != remindBean.getAlbum_list().get(2).getAlbum_info() &&
                    null != remindBean.getAlbum_list().get(2).getAlbum_info().getImages() &&
                    remindBean.getAlbum_list().get(2).getAlbum_info().getImages().size() > 0) {
                ImageUtils.displayFilletLeftTopImage(remindBean.getAlbum_list().get(2).getAlbum_info().getImages().get(0), iv_img9);
            } else {
                ImageUtils.displayFilletLeftTopImage("drawable://" + R.drawable.test_color, iv_img9);
            }
            if (remindBean.getAlbum_list().size() > 2 &&
                    null != remindBean.getAlbum_list().get(2).getAlbum_info() &&
                    null != remindBean.getAlbum_list().get(2).getAlbum_info().getImages() &&
                    remindBean.getAlbum_list().get(2).getAlbum_info().getImages().size() > 1) {
                ImageUtils.displayFilletRightTopImage(remindBean.getAlbum_list().get(2).getAlbum_info().getImages().get(1), iv_img10);
            } else {
                ImageUtils.displayFilletRightTopImage("drawable://" + R.drawable.test_color, iv_img10);
            }
            if (remindBean.getAlbum_list().size() > 2 &&
                    null != remindBean.getAlbum_list().get(2).getAlbum_info() &&
                    null != remindBean.getAlbum_list().get(2).getAlbum_info().getImages() &&
                    remindBean.getAlbum_list().get(2).getAlbum_info().getImages().size() > 2) {
                ImageUtils.displayFilletLeftBottomImage(remindBean.getAlbum_list().get(2).getAlbum_info().getImages().get(2), iv_img11);
            } else {
                ImageUtils.displayFilletLeftBottomImage("drawable://" + R.drawable.test_color, iv_img11);
            }
            if (remindBean.getAlbum_list().size() > 2 &&
                    null != remindBean.getAlbum_list().get(2).getAlbum_info() &&
                    null != remindBean.getAlbum_list().get(2).getAlbum_info().getImages() &&
                    remindBean.getAlbum_list().get(2).getAlbum_info().getImages().size() > 3) {
                ImageUtils.displayFilletRightBottomImage(remindBean.getAlbum_list().get(2).getAlbum_info().getImages().get(3), iv_img12);
            } else {
                ImageUtils.displayFilletRightBottomImage("drawable://" + R.drawable.test_color, iv_img12);
            }
            rl_album_all.setVisibility(View.VISIBLE);
        } else {
            rl_album_all.setVisibility(View.GONE);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && requestCode == 1) {

            SearchActivity.this.finish();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
        MobclickAgent.onPageEnd("搜索页");
    }


    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);
        MobclickAgent.onPageStart("搜索页");
    }


    class HostoryAdapter extends BaseAdapter {

        private Context context;
        private List<String> list;

        public HostoryAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            MyHolder myHolder = null;
            if (null == convertView) {
                myHolder = new MyHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_search_hostory, null);
                myHolder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
                myHolder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            myHolder.tv_item_name.setText(list.get(position));
            myHolder.rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //友盟统计
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("evenname", "搜索历史点击");
                    map.put("even", "搜索历史点击");
                    MobclickAgent.onEvent(SearchActivity.this, "shijian56", map);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("搜索历史点击")  //事件类别
                            .setAction("搜索历史点击")      //事件操作
                            .build());

                    PublicUtils.replaceSearchHostory(list.get(position));
                    Intent intent = new Intent();
                    intent.putExtra("search_info", list.get(position));
                    intent.putExtra("search_tag", "");
                    setResult(10, intent);
                    SearchActivity.this.finish();
                }
            });
            return convertView;
        }

        class MyHolder {
            private RelativeLayout rl_item;
            private TextView tv_item_name;
        }
    }

    private TextView tv_quxiao;
    private FlowLayoutSearch his_flowLayout;
    private String[] myData;
    private ClearEditText cet_clearedit;
    private MyListView lv_hostory_list;
    private List<String> mListHostory;
    private HostoryAdapter hostoryAdapter;
    private RelativeLayout rl_search_hostory;
    private ScrollView sv_hostory;
    private ScrollView sv_result;
    private ImageView iv_delete_hostory;

    private RelativeLayout rl_album_all;
    private RelativeLayout rl_images_one;
    private RelativeLayout rl_images_two;
    private RelativeLayout rl_images_three;
    private RelativeLayout rl_header1;
    private RelativeLayout rl_header2;
    private RelativeLayout rl_header3;
    private RoundImageView riv_header_one;
    private RoundImageView riv_header_two;
    private RoundImageView riv_header_three;
    private ImageView iv_img1;
    private ImageView iv_img2;
    private ImageView iv_img3;
    private ImageView iv_img4;
    private ImageView iv_img5;
    private ImageView iv_img6;
    private ImageView iv_img7;
    private ImageView iv_img8;
    private ImageView iv_img9;
    private ImageView iv_img10;
    private ImageView iv_img11;
    private ImageView iv_img12;
    private int width_Imgs;
    private String mSearchStr = "";
    private RemindBean mRemindBean = null;
    private RelativeLayout rl_result_ling;
    private RelativeLayout rl_result_image;
    private MyListView lv_result_list;
    private MyPicTagsAdapter myPicTagsAdapter;


    class MyPicTagsAdapter extends BaseAdapter {
        private Context context;
        private List<String> list;

        public MyPicTagsAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            MyPicTagsHolder myPicTagsHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_search_remind, null);
                myPicTagsHolder = new MyPicTagsHolder();
                myPicTagsHolder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
                myPicTagsHolder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
                convertView.setTag(myPicTagsHolder);
            } else {
                myPicTagsHolder = (MyPicTagsHolder) convertView.getTag();
            }

            myPicTagsHolder.tv_item_name.setText(list.get(position));
            myPicTagsHolder.rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //友盟统计
                    HashMap<String, String> map7 = new HashMap<String, String>();
                    map7.put("evenname", "联想词点击");
                    map7.put("even", "联想词点击");
                    MobclickAgent.onEvent(SearchActivity.this, "shijian62", map7);
                    //ga统计
                    MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("联想词点击")  //事件类别
                            .setAction("联想词点击")      //事件操作
                            .build());
                    PublicUtils.replaceSearchHostory(list.get(position));
                    Intent intent = new Intent();
                    intent.putExtra("search_info", list.get(position));
                    intent.putExtra("search_tag", "");
                    setResult(10, intent);
                    SearchActivity.this.finish();
                }
            });

            return convertView;
        }

        class MyPicTagsHolder {
            private TextView tv_item_name;
            private RelativeLayout rl_item;
        }
    }

}