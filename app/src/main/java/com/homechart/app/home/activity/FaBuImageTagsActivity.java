package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.faxiantags.FaXianTagBean;
import com.homechart.app.home.bean.faxiantags.TagInfoBean;
import com.homechart.app.home.bean.faxiantags.TagListItemBean;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.pictag.TagItemDataChildBean;
import com.homechart.app.myview.FlowLayoutCaiJiTags;
import com.homechart.app.myview.FlowLayoutFaBuTags;
import com.homechart.app.myview.FlowLayoutFaBuTagsDing;
import com.homechart.app.myview.SerializableHashMap;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/22.
 */

public class FaBuImageTagsActivity
        extends BaseActivity
        implements View.OnClickListener,
        FlowLayoutCaiJiTags.OnTagClickListener,
        FlowLayoutFaBuTagsDing.OnTagClickListener {

    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private TextView tv_content_right;
    private FlowLayoutCaiJiTags fl_tags_kongjian;
    private FlowLayoutCaiJiTags fl_tags_jubu;
    private FlowLayoutCaiJiTags fl_tags_shouna;
    private FlowLayoutCaiJiTags fl_tags_zhuangshi;
    public FaXianTagBean faXianTagBean;
    private List<TagListItemBean> listZiDing = new ArrayList<>();
    private List<TagListItemBean> listZiDingSelect = new ArrayList<>();
    private Map<String, String> mSelectMap;
    private EditText et_tag_text;
    private FlowLayoutCaiJiTags fl_tags_zidingyi;
    private FlowLayoutCaiJiTags fl_tags_gongneng;
    private FlowLayoutCaiJiTags fl_tags_shenghuo;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fabu_image_tags;
    }

    @Override
    protected void initView() {
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        fl_tags_kongjian = (FlowLayoutCaiJiTags) findViewById(R.id.fl_tags_kongjian);
        fl_tags_jubu = (FlowLayoutCaiJiTags) findViewById(R.id.fl_tags_jubu);
        fl_tags_shouna = (FlowLayoutCaiJiTags) findViewById(R.id.fl_tags_shouna);
        fl_tags_zhuangshi = (FlowLayoutCaiJiTags) findViewById(R.id.fl_tags_zhuangshi);
        fl_tags_gongneng = (FlowLayoutCaiJiTags) findViewById(R.id.fl_tags_gongneng);
        fl_tags_shenghuo = (FlowLayoutCaiJiTags) findViewById(R.id.fl_tags_shenghuo);
        et_tag_text = (EditText) findViewById(R.id.et_tag_text);
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        Bundle bundle = getIntent().getExtras();
        SerializableHashMap serializableHashMap = (SerializableHashMap) bundle.get("tags_select");
        mSelectMap = serializableHashMap.getMap();
        List<TagListItemBean> list = (List<TagListItemBean>) getIntent().getSerializableExtra("zidingyi");
        faXianTagBean = (FaXianTagBean) getIntent().getSerializableExtra("tagdata");
        listZiDing.clear();
        listZiDingSelect.clear();
        fl_tags_zidingyi = (FlowLayoutCaiJiTags) findViewById(R.id.fl_tags_zidingyi);
        if (list != null && list.size() > 0) {
            listZiDing.addAll(list);
            listZiDingSelect.addAll(list);
            fl_tags_zidingyi.setVisibility(View.VISIBLE);
        } else {

            fl_tags_zidingyi.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();

        nav_left_imageButton.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);
        et_tag_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(FaBuImageTagsActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    String searchContext = et_tag_text.getText().toString().trim();
                    if (TextUtils.isEmpty(searchContext.trim())) {
                        ToastUtils.showCenter(FaBuImageTagsActivity.this, "请添加标签名称");
                    } else {
                        fl_tags_zidingyi.setVisibility(View.VISIBLE);
                        listZiDing.add(new TagListItemBean( new TagInfoBean (et_tag_text.getText().toString(), "")));
                        listZiDingSelect.add(new TagListItemBean( new TagInfoBean (et_tag_text.getText().toString(), "")));
                        if (mSelectMap == null) {
                            mSelectMap = new HashMap<String, String>();
                        }
                        mSelectMap.put(et_tag_text.getText().toString(), et_tag_text.getText().toString());
                        fl_tags_zidingyi.cleanTag();
                        fl_tags_zidingyi.setListData(listZiDing, mSelectMap, "自定义");
                        et_tag_text.setText("");

                        Log.d("test", mSelectMap.toString());
                    }

                    return true;
                }

                return false;
            }
        });

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        tv_tital_comment.setText("添加标签");
        tv_content_right.setText("完成");


        if (faXianTagBean == null) {
            getDingYueData();
        } else {
            changeUI();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:

                FaBuImageTagsActivity.this.finish();

                break;
            case R.id.tv_content_right:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                if (mSelectMap != null) {
                    SerializableHashMap myMap = new SerializableHashMap();
                    myMap.setMap(mSelectMap);
                    bundle.putSerializable("tags_select", myMap);
                } else {
                    bundle.putSerializable("tags_select", null);
                }

                intent.putExtras(bundle);
                intent.putExtra("zidingyi", (Serializable) listZiDingSelect);
                FaBuImageTagsActivity.this.setResult(1, intent);
                FaBuImageTagsActivity.this.finish();

                break;
        }
    }

    private void getDingYueData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(FaBuImageTagsActivity.this, "标签导航列表获取失败");
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
                        msg.what = 7;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(FaBuImageTagsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getTagList(callBack);
    }

    private void changeUI() {
        if (faXianTagBean != null && faXianTagBean.getData().getGroup_list().size() > 5) {
            List<TagListItemBean> listKongJian = faXianTagBean.getData().getGroup_list().get(0).getGroup_info().getTag_list();
            List<TagListItemBean> listJuBu = faXianTagBean.getData().getGroup_list().get(1).getGroup_info().getTag_list();
            List<TagListItemBean> listZhuangShi = faXianTagBean.getData().getGroup_list().get(2).getGroup_info().getTag_list();
            List<TagListItemBean> listShouNa = faXianTagBean.getData().getGroup_list().get(3).getGroup_info().getTag_list();
            List<TagListItemBean> listGongNeng = faXianTagBean.getData().getGroup_list().get(4).getGroup_info().getTag_list();
            List<TagListItemBean> listShengHuo = faXianTagBean.getData().getGroup_list().get(5).getGroup_info().getTag_list();

            fl_tags_kongjian.setColorful(false);
            fl_tags_kongjian.setListData(listKongJian, mSelectMap, faXianTagBean.getData().getGroup_list().get(0).getGroup_info().getGroup_name());
            fl_tags_kongjian.setOnTagClickListener(this);

            fl_tags_jubu.setColorful(false);
            fl_tags_jubu.setListData(listJuBu, mSelectMap, faXianTagBean.getData().getGroup_list().get(1).getGroup_info().getGroup_name());
            fl_tags_jubu.setOnTagClickListener(this);

            fl_tags_zhuangshi.setColorful(false);
            fl_tags_zhuangshi.setListData(listShouNa, mSelectMap, faXianTagBean.getData().getGroup_list().get(2).getGroup_info().getGroup_name());
            fl_tags_zhuangshi.setOnTagClickListener(this);

            fl_tags_shouna.setColorful(false);
            fl_tags_shouna.setListData(listZhuangShi, mSelectMap, faXianTagBean.getData().getGroup_list().get(3).getGroup_info().getGroup_name());
            fl_tags_shouna.setOnTagClickListener(this);

            fl_tags_gongneng.setColorful(false);
            fl_tags_gongneng.setListData(listGongNeng, mSelectMap, faXianTagBean.getData().getGroup_list().get(4).getGroup_info().getGroup_name());
            fl_tags_gongneng.setOnTagClickListener(this);

            fl_tags_shenghuo.setColorful(false);
            fl_tags_shenghuo.setListData(listShengHuo, mSelectMap, faXianTagBean.getData().getGroup_list().get(5).getGroup_info().getGroup_name());
            fl_tags_shenghuo.setOnTagClickListener(this);

            fl_tags_zidingyi.setColorful(false);
            fl_tags_zidingyi.setListData(listZiDing, mSelectMap, "自定义");
            fl_tags_zidingyi.setOnTagClickListener(this);

        }
    }

    @Override
    public void tagClick(String text, int position, Map<String, String> selectMap, String type) {

        if (mSelectMap == null) {
            mSelectMap = new HashMap<>();
        }
        mSelectMap.putAll(selectMap);
        if (type.equals("自定义") && mSelectMap.containsKey(text)) {
            listZiDingSelect.add(new TagListItemBean( new TagInfoBean (text, "")));
            mSelectMap.put(text, text);
        }

    }

    @Override
    public void removeTagClick(String text, int position, Map<String, String> selectMap, String type) {

        if (mSelectMap != null) {
            mSelectMap.remove(text);
        }
    }

    @Override
    public void tagClickDing(String text, int position, Map<String, String> selectMap, String type) {
        if (mSelectMap == null) {
            mSelectMap = new HashMap<>();
        }
        mSelectMap.putAll(selectMap);
        if (type.equals("自定义") && mSelectMap.containsKey(text)) {
            listZiDingSelect.add(new TagListItemBean( new TagInfoBean (text, "")));
            mSelectMap.put(text, text);
        }
    }

    @Override
    public void removeTagClickDing(String text, int position, Map<String, String> selectMap, String type) {
        if (mSelectMap != null) {
            mSelectMap.remove(text);
        }
        if (type.equals("自定义")) {
            for (int i = 0; i < listZiDingSelect.size(); i++) {
                if (listZiDingSelect.get(i).getTag_info().getTag_name().equals(text)) {
                    listZiDingSelect.remove(i);
                }
            }
        }
        boolean isNoEmpty = listZiDing != null && listZiDing.size() > 0;
        if (isNoEmpty) {
            fl_tags_zidingyi.setVisibility(View.VISIBLE);
        } else {

            fl_tags_zidingyi.setVisibility(View.GONE);
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
//                String info = (String) msg.obj;
//                tagDataBean = GsonUtil.jsonToBean(info, TagDataBean.class);
//                changeUI();
            }else if (msg.what == 7) {
                String json = "{\"data\": " + (String) msg.obj + "}";
                faXianTagBean = GsonUtil.jsonToBean(json, FaXianTagBean.class);
                changeUI();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("添加图片标签页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("添加图片标签页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("添加图片标签页");
        MobclickAgent.onPause(this);
    }
}
