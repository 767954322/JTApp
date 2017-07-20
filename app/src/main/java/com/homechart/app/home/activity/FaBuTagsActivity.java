package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.pictag.TagItemDataChildBean;
import com.homechart.app.home.fragment.HomePicFragment;
import com.homechart.app.myview.FlowLayoutFaBuTags;
import com.homechart.app.myview.FlowLayoutFaBuTagsDing;
import com.homechart.app.myview.SerializableHashMap;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

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

public class FaBuTagsActivity
        extends BaseActivity
        implements View.OnClickListener,
        FlowLayoutFaBuTags.OnTagClickListener,
        FlowLayoutFaBuTagsDing.OnTagClickListener {

    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private TextView tv_content_right;
    private FlowLayoutFaBuTags fl_tags_kongjian;
    private FlowLayoutFaBuTags fl_tags_jubu;
    private FlowLayoutFaBuTags fl_tags_shouna;
    private FlowLayoutFaBuTags fl_tags_zhuangshi;
    public TagDataBean tagDataBean;
    private List<TagItemDataChildBean> listZiDing = new ArrayList<>();
    private List<TagItemDataChildBean> listZiDingSelect = new ArrayList<>();
    private Map<String, String> mSelectMap;
    private EditText et_tag_text;
    private FlowLayoutFaBuTagsDing fl_tags_zidingyi;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fabu_tags;
    }

    @Override
    protected void initView() {
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        fl_tags_kongjian = (FlowLayoutFaBuTags) findViewById(R.id.fl_tags_kongjian);
        fl_tags_jubu = (FlowLayoutFaBuTags) findViewById(R.id.fl_tags_jubu);
        fl_tags_shouna = (FlowLayoutFaBuTags) findViewById(R.id.fl_tags_shouna);
        fl_tags_zhuangshi = (FlowLayoutFaBuTags) findViewById(R.id.fl_tags_zhuangshi);
        et_tag_text = (EditText) findViewById(R.id.et_tag_text);
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        Bundle bundle = getIntent().getExtras();
        SerializableHashMap serializableHashMap = (SerializableHashMap) bundle.get("tags_select");
        mSelectMap = serializableHashMap.getMap();
        List<TagItemDataChildBean> list = (List<TagItemDataChildBean>) getIntent().getSerializableExtra("zidingyi");
        tagDataBean = (TagDataBean) getIntent().getSerializableExtra("tagdata");
        listZiDing.clear();
        listZiDingSelect.clear();
        fl_tags_zidingyi = (FlowLayoutFaBuTagsDing) findViewById(R.id.fl_tags_zidingyi);
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
                            .hideSoftInputFromWindow(FaBuTagsActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    String searchContext = et_tag_text.getText().toString().trim();
                    if (TextUtils.isEmpty(searchContext.trim())) {
                        ToastUtils.showCenter(FaBuTagsActivity.this, "请添加标签名称");
                    } else {
                        fl_tags_zidingyi.setVisibility(View.VISIBLE);
                        listZiDing.add(new TagItemDataChildBean("", et_tag_text.getText().toString()));
                        listZiDingSelect.add(new TagItemDataChildBean("", et_tag_text.getText().toString()));
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


        if (tagDataBean == null) {
            getTagData();
        } else {
            changeUI();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:

                FaBuTagsActivity.this.finish();

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
                FaBuTagsActivity.this.setResult(1, intent);
                FaBuTagsActivity.this.finish();

                break;
        }
    }

    //获取tag信息
    private void getTagData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(FaBuTagsActivity.this, getString(R.string.fabutags_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        data_msg = "{ \"tag_id\": " + data_msg + "}";
                        Message msg = new Message();
                        msg.obj = data_msg;
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(FaBuTagsActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(FaBuTagsActivity.this, getString(R.string.fabutags_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getPicTagData(callBack);
    }

    private void changeUI() {
        List<TagItemDataChildBean> listKongJian = tagDataBean.getTag_id().get(0).getChildren();
        List<TagItemDataChildBean> listJuBu = tagDataBean.getTag_id().get(1).getChildren();
        List<TagItemDataChildBean> listZhuangShi = tagDataBean.getTag_id().get(2).getChildren();
        List<TagItemDataChildBean> listShouNa = tagDataBean.getTag_id().get(3).getChildren();
        if (tagDataBean != null) {

            fl_tags_kongjian.setColorful(false);
            fl_tags_kongjian.setListData(listKongJian, mSelectMap, "空间");
            fl_tags_kongjian.setOnTagClickListener(this);

            fl_tags_jubu.setColorful(false);
            fl_tags_jubu.setListData(listJuBu, mSelectMap, "局部");
            fl_tags_jubu.setOnTagClickListener(this);

            fl_tags_shouna.setColorful(false);
            fl_tags_shouna.setListData(listShouNa, mSelectMap, "收纳");
            fl_tags_shouna.setOnTagClickListener(this);

            fl_tags_zhuangshi.setColorful(false);
            fl_tags_zhuangshi.setListData(listZhuangShi, mSelectMap, "装饰");
            fl_tags_zhuangshi.setOnTagClickListener(this);

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
            listZiDingSelect.add(new TagItemDataChildBean("", text));
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
            listZiDingSelect.add(new TagItemDataChildBean("", text));
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
                if (listZiDingSelect.get(i).getTag_name().equals(text)) {
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
                String info = (String) msg.obj;
                tagDataBean = GsonUtil.jsonToBean(info, TagDataBean.class);
                changeUI();
            }
        }
    };

}
