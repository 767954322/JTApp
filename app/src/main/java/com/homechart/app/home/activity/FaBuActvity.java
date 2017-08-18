package com.homechart.app.home.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.UrlConstants;
import com.homechart.app.home.adapter.MyActivitysListAdapter;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.fabu.ActivityDataBean;
import com.homechart.app.home.bean.fabu.ActivityItemDataBean;
import com.homechart.app.home.bean.pictag.TagDataBean;
import com.homechart.app.home.bean.pictag.TagItemDataChildBean;
import com.homechart.app.myview.FlowLayoutFaBu;
import com.homechart.app.myview.HomeActivityPopWin;
import com.homechart.app.myview.MyListView;
import com.homechart.app.myview.SerializableHashMap;
import com.homechart.app.utils.BitmapUtil;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.Md5Util;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.alertview.AlertView;
import com.homechart.app.utils.alertview.OnItemClickListener;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.FileHttpManager;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.utils.volley.PutFileCallBack;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/21.
 */

public class FaBuActvity
        extends BaseActivity
        implements View.OnClickListener,
        FlowLayoutFaBu.OnTagClickListener,
        MyActivitysListAdapter.CheckStatus,
        PutFileCallBack,
        OnItemClickListener {
    private ImageView iv_image_fabu;
    private String urlImage;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private TextView tv_content_right;
    private FlowLayoutFaBu fl_tag_flowLayout;
    private List<String> listTag = new ArrayList<>();
    private MyListView lv_zhuti;
    public MyActivitysListAdapter adapter;
    public List<ActivityItemDataBean> activityList;
    private TextView tv_zhuti_tital;
    private View view_center;
    private Map<String, String> selectTags;
    private List<TagItemDataChildBean> listZiDingSelect;
    private Map<Integer, String> activityMap = new HashMap<>();
    private EditText et_fabu_miaosu;
    private HomeActivityPopWin homeActivityPopWin;
    private AlertView mAlertView;
    public TagDataBean tagDataBean;
    private String activity_id;
    private String type;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fabu;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        urlImage = getIntent().getStringExtra("image_path");
        activity_id = getIntent().getStringExtra("activity_id");
        type = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(activity_id)) {
            activityMap.put(-1, activity_id);
        }
    }

    @Override
    protected void initView() {

        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        et_fabu_miaosu = (EditText) findViewById(R.id.et_fabu_miaosu);
        tv_content_right = (TextView) findViewById(R.id.tv_content_right);
        tv_zhuti_tital = (TextView) findViewById(R.id.tv_zhuti_tital);
        view_center = findViewById(R.id.view_center);
        iv_image_fabu = (ImageView) findViewById(R.id.iv_image_fabu);
        fl_tag_flowLayout = (FlowLayoutFaBu) findViewById(R.id.fl_tag_flowLayout);
        lv_zhuti = (MyListView) findViewById(R.id.lv_zhuti);
    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        getActivityListData();
        tv_tital_comment.setText("发布图片");
        tv_content_right.setText("发布");
        ImageUtils.displayFilletImage("file://" + urlImage, iv_image_fabu);
        fl_tag_flowLayout.setColorful(false);
        fl_tag_flowLayout.setListData(listTag);
        fl_tag_flowLayout.setOnTagClickListener(this);
        homeActivityPopWin = new HomeActivityPopWin(FaBuActvity.this);
        showDialog();
        getTagData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                String miaosu1 = et_fabu_miaosu.getText().toString();

                if ((selectTags != null && selectTags.size() > 0) || !TextUtils.isEmpty(miaosu1)) {
                    mAlertView.show();
                } else {
                    FaBuActvity.this.finish();
                }
                break;
            case R.id.tv_content_right:
                String miaosu = et_fabu_miaosu.getText().toString();
                if (TextUtils.isEmpty(miaosu)) {
                    ToastUtils.showCenter(FaBuActvity.this, "请填写图片描述后发布哦");
                    break;
                }
                if (selectTags == null || selectTags.size() == 0) {
                    ToastUtils.showCenter(FaBuActvity.this, " 需要添加标签后才可发布哦");
                    break;
                }

                CustomProgress.show(FaBuActvity.this, "正在发布...", false, null);
                upLoaderHeader();
                break;
        }

    }

    private void getActivityListData() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(FaBuActvity.this, getString(R.string.activitylist_get_error));
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
                        msg.what = 0;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(FaBuActvity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(FaBuActvity.this, getString(R.string.color_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getDoingActivityData(callBack);

    }

    @Override
    public void TagClick(String text, int position) {

//        ToastUtils.showCenter(FaBuActvity.this, "点击" + position + text);
    }

    @Override
    public void DeleteTag(String text, int position) {
        fl_tag_flowLayout.cleanTag();
        listTag.remove(position);
        fl_tag_flowLayout.setListData(listTag);
        if (selectTags.containsKey(text)) {
            selectTags.remove(text);
        }
        for (int i = 0; i < listZiDingSelect.size(); i++) {
            if (text.equals(listZiDingSelect.get(i).getTag_name())) {
                listZiDingSelect.remove(i);
            }
        }
    }

    @Override
    public void AddTag(String text, int position) {
        Intent intent = new Intent(FaBuActvity.this, FaBuTagsActivity.class);
        SerializableHashMap myMap = new SerializableHashMap();
        myMap.setMap(selectTags);
        Bundle bundle = new Bundle();
        bundle.putSerializable("tags_select", myMap);
        intent.putExtras(bundle);
        intent.putExtra("zidingyi", (Serializable) listZiDingSelect);
        intent.putExtra("tagdata", tagDataBean);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 1) {
            Bundle bundle = data.getExtras();
            SerializableHashMap serializableHashMap = (SerializableHashMap) bundle.get("tags_select");
            listZiDingSelect = (List<TagItemDataChildBean>) data.getSerializableExtra("zidingyi");
            if (serializableHashMap != null && serializableHashMap.getMap() != null && serializableHashMap.getMap().size() > 0) {
                selectTags = serializableHashMap.getMap();
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            } else {
                selectTags.clear();
                Message message = new Message();
                message.what = 2;
                mHandler.sendMessage(message);
            }

        }

    }

    private void upLoaderHeader() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                SimpleDateFormat timesdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String fileName = timesdf.format(new Date()).toString();//获取系统时间
                //压缩图片
                Bitmap bitmap_before = BitmapUtil.getBitmap(urlImage);
//                Bitmap bitmap_compress = BitmapUtil.ratio(bitmap_before, 400, 800);
                Bitmap bitmap_compress_press = BitmapUtil.compressImage(bitmap_before);
                try {
                    boolean status = BitmapUtil.saveBitmap(bitmap_compress_press, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName + "/");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                FileHttpManager.getInstance().uploadFile(FaBuActvity.this, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName + "/"),
                        UrlConstants.PUT_IMAGE,
                        map,
                        PublicUtils.getPublicHeader(MyApplication.getInstance()));
            }
        }.start();

    }

    @Override
    public void checkChange(int position, boolean status, String activityId) {

        if (status) {
            activityMap.clear();
            activityMap.put(position, activityId);
        } else {
            activityMap.clear();
        }
        Log.d("test", activityMap.toString());
        adapter.notifyData(activityMap);
    }

    @Override
    public void activityDetail(int position, String activityId) {

        if (homeActivityPopWin.isShowing()) {
            homeActivityPopWin.dismiss();
        } else {

            homeActivityPopWin.setData(activityList.get(position));
            homeActivityPopWin.showAtLocation(FaBuActvity.this.findViewById(R.id.main),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                    0,
                    0);
        }

    }

    @Override
    public void onSucces(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            String image_id = jsonObject1.getString("immage_id");
            faBu(image_id);
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtils.showCenter(FaBuActvity.this, "发布失败");
        }

    }

    @Override
    public void onFails() {
        Message msg = new Message();
        msg.what = 4;
        mHandler.sendMessage(msg);

    }

    private void faBu(String image_id) {

        String activityStr = "";
        if (activityMap.size() > 0) {
            StringBuffer sbActivity = new StringBuffer();
            for (Integer key : activityMap.keySet()) {
                sbActivity.append(activityMap.get(key));
            }
            activityStr = sbActivity.toString();
        }

        StringBuffer sb = new StringBuffer();
        for (String key : selectTags.keySet()) {
            sb.append(key + " ");
        }
        String tagStr = sb.toString();
        String miaosu = et_fabu_miaosu.getText().toString();


        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(FaBuActvity.this, "发布失败");
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
                        msg.what = 3;
                        mHandler.sendMessage(msg);
                    } else {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(FaBuActvity.this, error_msg);
                    }
                } catch (JSONException e) {

                    CustomProgress.cancelDialog();
                    ToastUtils.showCenter(FaBuActvity.this, "发布失败");
                }
            }
        };
        MyHttpManager.getInstance().doFaBu(image_id, miaosu, tagStr, activityStr, callBack);

    }

    /***
     * 提示框
     */
    private void showDialog() {
        mAlertView = new AlertView
                ("", "是否取消本次发布", UIUtils.getString(R.string.fabu_back_cancle), new String[]{UIUtils.getString(R.string.fabu_back_sure)},
                        null, this, AlertView.Style.Alert, this);
    }

    @Override
    public void onItemClick(Object object, int position) {

        switch (position) {
            case 0:
                FaBuActvity.this.finish();
                break;
        }


    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            if (code == 0) {
                String info = (String) msg.obj;
                ActivityDataBean activityDataBean = GsonUtil.jsonToBean(info, ActivityDataBean.class);
                activityList = activityDataBean.getActivity_list();
                if (activityList != null && activityList.size() > 0) {
                    tv_zhuti_tital.setVisibility(View.VISIBLE);
                    view_center.setVisibility(View.VISIBLE);
                    adapter = new MyActivitysListAdapter(activityList, FaBuActvity.this, FaBuActvity.this, activityMap);
                    lv_zhuti.setAdapter(adapter);
                } else {
                    tv_zhuti_tital.setVisibility(View.GONE);
                    view_center.setVisibility(View.GONE);
                }
            } else if (code == 1) {
                listTag.clear();
                for (String key : selectTags.keySet()) {
                    listTag.add(key);
                }
                fl_tag_flowLayout.cleanTag();
                fl_tag_flowLayout.setListData(listTag);
            } else if (code == 2) {
                listTag.clear();
                fl_tag_flowLayout.cleanTag();
                fl_tag_flowLayout.setListData(listTag);
            } else if (code == 3) {

                String info = (String) msg.obj;
                try {
                    if(type.equals("home")){//首页
                        //友盟统计
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("evenname", "发布成功");
                        map.put("even", "首页");
                        MobclickAgent.onEvent(FaBuActvity.this, "jtaction2", map);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("首页")  //事件类别
                                .setAction("发布成功")      //事件操作
                                .build());
                    }else {//活动
                        //友盟统计
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("evenname", "发布成功");
                        map.put("even", "活动页");
                        MobclickAgent.onEvent(FaBuActvity.this, "jtaction2", map);
                        //ga统计
                        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                                .setCategory("活动页")  //事件类别
                                .setAction("发布成功")      //事件操作
                                .build());
                    }

                    JSONObject jsonObject = new JSONObject(info);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("item_info");
                    String item_id = jsonObject1.getString("item_id");
                    CustomProgress.cancelDialog();
                    Intent intent = new Intent(FaBuActvity.this, ImageDetailActivity.class);
                    intent.putExtra("item_id", item_id);
                    startActivity(intent);
                    FaBuActvity.this.finish();
                    //TODO(统计依据activity_id，为空是+号跳转的，不为空为活动跳进来了)

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (code == 4) {

                CustomProgress.cancelDialog();
                ToastUtils.showCenter(FaBuActvity.this, "发布失败");
            } else if (code == 5) {

                String info = (String) msg.obj;
                tagDataBean = GsonUtil.jsonToBean(info, TagDataBean.class);
            }

        }
    };


    //获取tag信息
    private void getTagData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(FaBuActvity.this, getString(R.string.fabutags_get_error));
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
                        msg.what = 5;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(FaBuActvity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(FaBuActvity.this, getString(R.string.fabutags_get_error));
                }
            }
        };
        MyHttpManager.getInstance().getPicTagData(callBack);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("发布图片页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("发布图片页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("发布图片页");
        MobclickAgent.onPause(this);
    }
}
