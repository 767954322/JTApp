package com.homechart.app.home.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.UrlConstants;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.city.ProvinceBean;
import com.homechart.app.home.bean.fensi.FenSiBean;
import com.homechart.app.home.bean.putheader.HeaderBean;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.timepiker.citypickerview.widget.AgePiker;
import com.homechart.app.timepiker.citypickerview.widget.CityPicker;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.Md5Util;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.StringUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by gumenghao on 17/6/7.
 */

public class MyInfoActivity
        extends BaseActivity
        implements View.OnClickListener,
        PutFileCallBack,
        RadioGroup.OnCheckedChangeListener,
        TextWatcher, OnItemClickListener {

    private ProvinceBean provinceBean;
    private ImageButton mIBBack;
    private TextView mTVTital;
    private TextView mTVBaoCun;
    private int mTag = 0;
    private RelativeLayout rl_myinfo_location;
    private RelativeLayout rl_myinfo_header;
    private RelativeLayout rl_myinfo_age;
    private RoundImageView iv_myinfo_header;
    private UserCenterInfoBean userCenterInfoBean;
    private EditText et_myinfo_nikename;
    private EditText et_myinfo_jianjie;
    private RadioGroup rg_sex;
    private RadioButton rb_nan;
    private RadioButton rb_nv;
    private TextView tv_myinfo_location;
    private CityPicker cityPicker;
    private String mUserId;
    private AgePiker agerPicker;
    private TextView tv_myinfo_age;
    private TextView tv_myinfo_mobile_num;
    private ImageView iv_myinfo_mobile_icon;
    private TextView tv_myinfo_mobile;
    private RelativeLayout rl_myinfo_mobile;
    private RelativeLayout rl_myinfo_shiming;
    private View view_below_mobile;

    private String province_id = "";
    private String city_id = "";
    private String age_select = "";

    private String path = "";//获取的头像路径
    private String avatar_id = "";//通过获取用户信息或上传头像接口获得
    private int sex = 0;//1:男  2:女
    private String header_id = "";//上传头像的id
    private String[] str;
    private View view_nikename;
    private View view_jianjie;

    private boolean imageChange = false;
    private String nikeChange = "";
    private String sexChange = "";
    private String locationChange = "";
    private String ageChange = "";
    private String jianjieChange = "";
    private AlertView mAlertView;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_myinfo;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        userCenterInfoBean = (UserCenterInfoBean) getIntent().getSerializableExtra("info");
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
    }

    @Override
    protected void initView() {
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mTVTital = (TextView) findViewById(R.id.tv_tital_comment);
        mTVBaoCun = (TextView) findViewById(R.id.tv_content_right);
        tv_myinfo_age = (TextView) findViewById(R.id.tv_myinfo_age);
        tv_myinfo_mobile = (TextView) findViewById(R.id.tv_myinfo_mobile);
        tv_myinfo_mobile_num = (TextView) findViewById(R.id.tv_myinfo_mobile_num);
        tv_myinfo_location = (TextView) findViewById(R.id.tv_myinfo_location);
        iv_myinfo_mobile_icon = (ImageView) findViewById(R.id.iv_myinfo_mobile_icon);
        rl_myinfo_header = (RelativeLayout) findViewById(R.id.rl_myinfo_header);
        rl_myinfo_location = (RelativeLayout) findViewById(R.id.rl_myinfo_location);
        rl_myinfo_age = (RelativeLayout) findViewById(R.id.rl_myinfo_age);
        rl_myinfo_mobile = (RelativeLayout) findViewById(R.id.rl_myinfo_mobile);
        rl_myinfo_shiming = (RelativeLayout) findViewById(R.id.rl_myinfo_shiming);
        iv_myinfo_header = (RoundImageView) findViewById(R.id.iv_myinfo_header);
        et_myinfo_nikename = (EditText) findViewById(R.id.et_myinfo_nikename);
        et_myinfo_jianjie = (EditText) findViewById(R.id.et_myinfo_jianjie);
        view_below_mobile = (View) findViewById(R.id.view_below_mobile);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        rb_nan = (RadioButton) findViewById(R.id.rb_nan);
        rb_nv = (RadioButton) findViewById(R.id.rb_nv);
        view_nikename = findViewById(R.id.view_nikename);
        view_jianjie = findViewById(R.id.view_jianjie);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mIBBack.setOnClickListener(this);
        mTVBaoCun.setOnClickListener(this);
        rl_myinfo_location.setOnClickListener(this);
        rl_myinfo_header.setOnClickListener(this);
        rl_myinfo_age.setOnClickListener(this);
        rl_myinfo_mobile.setOnClickListener(this);
        rl_myinfo_shiming.setOnClickListener(this);
        view_jianjie.setOnClickListener(this);
        view_nikename.setOnClickListener(this);
        rg_sex.setOnCheckedChangeListener(this);
        et_myinfo_nikename.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() > 15) {
                    ToastUtils.showCenter(MyInfoActivity.this, "请输入长度2-15个字的昵称");
                    et_myinfo_nikename.setText(s.toString().substring(0, 15));
                    et_myinfo_nikename.setSelection(et_myinfo_nikename.getText().length());
                }

            }
        });
        et_myinfo_jianjie.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() > 15) {
                    ToastUtils.showCenter(MyInfoActivity.this, "请输入长度15个字以内的介绍");
                    et_myinfo_jianjie.setText(s.toString().substring(0, 15));
                    et_myinfo_jianjie.setSelection(et_myinfo_jianjie.getText().length());
                }

            }
        });

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTVTital.setText(R.string.myinfoactivity_tital);
        mTVBaoCun.setText(R.string.setactivity_baocun);
        showDialog();
        getCitydata(mTag);
        if (userCenterInfoBean != null) {
            changeUI();
        } else {
            getUserInfo();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                String nikename = et_myinfo_nikename.getText().toString().trim();
                boolean nikeB = nikename.equals(nikeChange);
                boolean sexB = true;
                if (sex == 0) {
                    sexB = true;
                } else {
                    sexB = sexChange.equals(sex + "");
                }
                boolean locationB = true;
                if (TextUtils.isEmpty(province_id) && TextUtils.isEmpty(city_id)) {
                    locationB = true;
                } else {
                    locationB = locationChange.equals((province_id + city_id).trim());
                }
                boolean ageB = ageChange.equals(age_select);
                String jianjie = et_myinfo_jianjie.getText().toString().trim();
                boolean jianjieB = jianjieChange.trim().equals(jianjie);
                if (!imageChange &&
                        nikeB &&
                        sexB &&
                        locationB &&
                        ageB &&
                        jianjieB
                        ) {//没更改
                    MyInfoActivity.this.finish();
                } else {
                    mAlertView.show();
                }


                break;
            case R.id.tv_content_right:
                String nikename1 = et_myinfo_nikename.getText().toString();
                if (nikename1.length() < 2) {
                    ToastUtils.showCenter(MyInfoActivity.this, "请输入长度2-15个字的昵称");
                    return;
                }

                CustomProgress.show(MyInfoActivity.this, "正在保存...", false, null);
                if (!path.equals("")) {
                    upLoaderHeader();
                } else {
                    saveUserInfo();
                }

                break;
            case R.id.rl_myinfo_header:

                GalleryFinal.openGallerySingle(0, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (resultList != null && resultList.size() > 0) {
                            if (reqeustCode == 0) {
                                String picPath = resultList.get(0).getPhotoPath();
                                Message message = new Message();
                                message.arg1 = 2;
                                message.obj = picPath;
                                handler.sendMessage(message);
                            }
                        } else {
                            ToastUtils.showCenter(MyInfoActivity.this, "图片资源获取失败");
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {
                    }
                });

                break;
            case R.id.rl_myinfo_location:

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(rl_myinfo_header.getWindowToken(), 0);
                }


                if (provinceBean == null) {
                    getCitydata(mTag);
                } else {
                    openCity();
                }

                break;

            case R.id.rl_myinfo_age:
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm1 != null) {
                    imm1.hideSoftInputFromWindow(rl_myinfo_header.getWindowToken(), 0);
                }
                agerPicker = new AgePiker.Builder(MyInfoActivity.this, provinceBean).textSize(20)
                        .itemPadding(10)
                        .build();
                if (userCenterInfoBean != null) {
                    String beforeAge = userCenterInfoBean.getUser_info().getAge_group();
                    if (beforeAge != null) {
                        agerPicker.setAgar(beforeAge);
                    }
                }
                agerPicker.show();
                agerPicker.setOnCityItemClickListener(new AgePiker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                        age_select = citySelected[0];
                        tv_myinfo_age.setTextColor(UIUtils.getColor(R.color.bg_262626));
                        tv_myinfo_age.setText(age_select);

                    }

                    @Override
                    public void onCancel() {
                    }
                });


                break;

            case R.id.rl_myinfo_mobile:

                if (userCenterInfoBean != null && TextUtils.isEmpty(userCenterInfoBean.getUser_info().getMobile())) {

                    Intent intent = new Intent(MyInfoActivity.this, BundleMobileActivity.class);
                    startActivityForResult(intent, 0);

                }

                break;
            case R.id.rl_myinfo_shiming:

                if (userCenterInfoBean != null && !userCenterInfoBean.getUser_info().getProfession().equals("0")) {

                    //TODO 跳转专业用户资料页
                    Intent intent = new Intent(MyInfoActivity.this, DesinerInfoActivity.class);
                    intent.putExtra("info", userCenterInfoBean);
                    startActivity(intent);

                }

                break;

            case R.id.view_nikename:

                et_myinfo_nikename.requestFocus();
                InputMethodManager imm2 = (InputMethodManager) et_myinfo_nikename.getContext().getSystemService(MyInfoActivity.this.INPUT_METHOD_SERVICE);
                imm2.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                et_myinfo_nikename.setSelection(et_myinfo_nikename.getText().length());
                break;
            case R.id.view_jianjie:

                et_myinfo_jianjie.requestFocus();
                InputMethodManager imm3 = (InputMethodManager) et_myinfo_jianjie.getContext().getSystemService(MyInfoActivity.this.INPUT_METHOD_SERVICE);
                imm3.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                et_myinfo_jianjie.setSelection(et_myinfo_jianjie.getText().length());
                break;
        }
    }

    private void changeUI() {

        if (null != userCenterInfoBean && null != userCenterInfoBean.getUser_info()) {
            ImageUtils.displayRoundImage(userCenterInfoBean.getUser_info().getAvatar().getBig(), iv_myinfo_header);
            nikeChange = userCenterInfoBean.getUser_info().getNickname();
            et_myinfo_nikename.setText(userCenterInfoBean.getUser_info().getNickname());

            if (TextUtils.isEmpty(userCenterInfoBean.getUser_info().getLocation())) {
                locationChange = "";
                tv_myinfo_location.setText("未设置");
                tv_myinfo_location.setTextColor(UIUtils.getColor(R.color.bg_b2b2b2));
            } else {
                String str_location = userCenterInfoBean.getUser_info().getLocation();
                locationChange = (userCenterInfoBean.getUser_info().getProvince() + userCenterInfoBean.getUser_info().getCity()).trim();
                str = str_location.split(" ");
                tv_myinfo_location.setText(userCenterInfoBean.getUser_info().getLocation() + "区");
                tv_myinfo_location.setTextColor(UIUtils.getColor(R.color.bg_262626));
            }


            if (TextUtils.isEmpty(userCenterInfoBean.getUser_info().getMobile())) {

                tv_myinfo_mobile_num.setVisibility(View.GONE);
                iv_myinfo_mobile_icon.setVisibility(View.VISIBLE);
                tv_myinfo_mobile.setVisibility(View.VISIBLE);

            } else {

                tv_myinfo_mobile_num.setVisibility(View.VISIBLE);
                iv_myinfo_mobile_icon.setVisibility(View.GONE);
                tv_myinfo_mobile.setVisibility(View.GONE);
                tv_myinfo_mobile_num.setText(userCenterInfoBean.getUser_info().getMobile());

            }

            if (!userCenterInfoBean.getUser_info().getProfession().equals("0")) {

                rl_myinfo_shiming.setVisibility(View.VISIBLE);
                view_below_mobile.setVisibility(View.VISIBLE);


            } else {

                rl_myinfo_shiming.setVisibility(View.GONE);
                view_below_mobile.setVisibility(View.GONE);

            }

            String gender = userCenterInfoBean.getUser_info().getGender();
            if (!gender.trim().equals("")) {

                if (gender.trim().equals("m")) {
                    sexChange = "1";
                    rb_nan.setChecked(true);
                } else if (gender.trim().equals("f")) {
                    sexChange = "2";
                    rb_nv.setChecked(true);
                }

            } else {
                sexChange = "";
            }

            if (!TextUtils.isEmpty(userCenterInfoBean.getUser_info().getSlogan())) {
                et_myinfo_jianjie.setText(userCenterInfoBean.getUser_info().getSlogan());
                jianjieChange = userCenterInfoBean.getUser_info().getSlogan();
            }

            if (!TextUtils.isEmpty(userCenterInfoBean.getUser_info().getAge_group())) {
                ageChange = userCenterInfoBean.getUser_info().getAge_group();
                age_select = userCenterInfoBean.getUser_info().getAge_group();
                tv_myinfo_age.setTextColor(UIUtils.getColor(R.color.bg_262626));
                tv_myinfo_age.setText(userCenterInfoBean.getUser_info().getAge_group());
            } else {
                ageChange = "";
                tv_myinfo_age.setTextColor(UIUtils.getColor(R.color.bg_b2b2b2));
                tv_myinfo_age.setText("未设置");
            }

        }
    }

    //获取用户信息
    private void getUserInfo() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(MyInfoActivity.this, getString(R.string.userinfo_get_error));
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
                        msg.arg1 = 3;
                        msg.obj = data_msg;
                        handler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(MyInfoActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getUserInfo(mUserId, callBack);


    }
    //保存用户信息

    private void saveUserInfo() {

        String nikename = et_myinfo_nikename.getText().toString();

        Map<String, String> map = new HashMap<>();
        map.put("nickname", nikename);
        if (!TextUtils.isEmpty(header_id)) {
            map.put("avatar_id", header_id);
        }

        if (sex != 0) {
            if (sex == 1) {
                map.put("gender", "m");
            } else {
                map.put("gender", "f");
            }
        }
        if (!TextUtils.isEmpty(province_id) && !TextUtils.isEmpty(city_id)) {
            map.put("province", province_id);
            map.put("city", city_id);
        }
        if (!TextUtils.isEmpty(age_select)) {
            map.put("age_group", age_select);
        }
        map.put("slogan", et_myinfo_jianjie.getText().toString());

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                CustomProgress.cancelDialog();
                ToastUtils.showCenter(MyInfoActivity.this, getString(R.string.userinfo_get_error));
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        getUserInfo();
                        imageChange = false;
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(MyInfoActivity.this, "保存成功");
                        getUserInfo();
                        Intent intent_result = getIntent();
                        setResult(1, intent_result);
                    } else {
                        CustomProgress.cancelDialog();
                        ToastUtils.showCenter(MyInfoActivity.this, error_msg);

                    }
                } catch (JSONException e) {
                    CustomProgress.cancelDialog();
                }
            }
        };
        MyHttpManager.getInstance().saveUserInfo(map, callBack);


    }


    private void upLoaderHeader() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                String signString = PublicUtils.getSinaString(map);
                String tabMd5String = Md5Util.getMD5twoTimes(signString);
                map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                FileHttpManager.getInstance().uploadFile(MyInfoActivity.this, new File(path),
                        UrlConstants.PUT_FILE,
                        map,
                        PublicUtils.getPublicHeader(MyApplication.getInstance()));
            }
        }.start();

    }

    private void openCity() {

        if (provinceBean != null) {

            cityPicker = new CityPicker.Builder(MyInfoActivity.this, provinceBean).textSize(20)
                    .titleTextColor("#000000")
                    .backgroundPop(R.color.white)
                    .textColor(Color.parseColor("#000000"))
                    .provinceCyclic(true)
                    .cityCyclic(false)
                    .districtCyclic(false)
                    .visibleItemsCount(7)
                    .itemPadding(10)
                    .onlyShowProvinceAndCity(true)
                    .build();
            if (str != null && str.length == 2) {
                cityPicker.defaultProvinceName = str[0];
                cityPicker.defaultCityName = str[1];
            }
            cityPicker.show();
            cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                @Override
                public void onSelected(String... citySelected) {

                    province_id = citySelected[2];
                    city_id = citySelected[3];
//                    Toast.makeText(MyInfoActivity.this,
//                            "选择结果：\n省：" + citySelected[0] +
//                                    "\n市：" + citySelected[1] +
//                                    "\n省份编号：" + citySelected[2] +
//                                    "\n城市编号：" + citySelected[3],
//                            Toast.LENGTH_LONG).show();

                    tv_myinfo_location.setTextColor(UIUtils.getColor(R.color.bg_262626));
                    tv_myinfo_location.setText(citySelected[0] + "  " + citySelected[1]);

                }

                @Override
                public void onCancel() {
                }
            });
        } else {
            getCitydata(mTag);
        }

    }

    //获取省市信息
    private void getCitydata(final int tag) {

        OkStringRequest.OKResponseCallback callback = new OkStringRequest.OKResponseCallback() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showCenter(MyInfoActivity.this, UIUtils.getString(R.string.citydata_error));
            }

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        String newJson = "{\"data\":" + data_msg + "}";
                        provinceBean = GsonUtil.jsonToBean(newJson, ProvinceBean.class);
                        Message message = new Message();
                        message.arg1 = tag;
                        handler.sendMessage(message);
                    } else {
                        ToastUtils.showCenter(MyInfoActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(MyInfoActivity.this, UIUtils.getString(R.string.citydata_error));
                }
            }
        };
        MyHttpManager.getInstance().getCityList(callback);


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int tag = msg.arg1;
            if (tag == 1 && null != provinceBean) {//传1：为未获取城市数据，重新获取的，传0：为刚进来获取数据，不需要操作
                //TODO 打开省市区
                openCity();
            } else if (tag == 2) {//拍照回调
                imageChange = true;
                path = (String) msg.obj;
                ImageUtils.displayRoundImage("file://" + path, iv_myinfo_header);
            } else if (tag == 3) { //获取个人资料的返回
                String info = (String) msg.obj;
                userCenterInfoBean = GsonUtil.jsonToBean(info, UserCenterInfoBean.class);
                changeUI();
            }
        }
    };


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == R.id.rb_nan) {
            sex = 1;
            rb_nan.setTextColor(UIUtils.getColor(R.color.bg_262626));
            rb_nv.setTextColor(UIUtils.getColor(R.color.bg_b2b2b2));
        } else if (checkedId == R.id.rb_nv) {
            sex = 2;
            rb_nv.setTextColor(UIUtils.getColor(R.color.bg_262626));
            rb_nan.setTextColor(UIUtils.getColor(R.color.bg_b2b2b2));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onSucces(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
            String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
            String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
            if (error_code == 0) {
                HeaderBean headerBean = GsonUtil.jsonToBean(data_msg, HeaderBean.class);
                header_id = headerBean.getAvatar_id();
                saveUserInfo();
            } else {
                header_id = "";
                CustomProgress.cancelDialog();
                ToastUtils.showCenter(MyInfoActivity.this, error_msg);
            }
        } catch (JSONException e) {
            header_id = "";
            CustomProgress.cancelDialog();
            ToastUtils.showCenter(MyInfoActivity.this, UIUtils.getString(R.string.putheader_error));
        }

    }

    @Override
    public void onFails() {
        header_id = "";
        CustomProgress.cancelDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == 1) {//绑定手机号嘛回调

            String mobile = (String) data.getStringExtra("mobile");
            tv_myinfo_mobile_num.setVisibility(View.VISIBLE);
            iv_myinfo_mobile_icon.setVisibility(View.GONE);
            tv_myinfo_mobile.setVisibility(View.GONE);
            String a = mobile.substring(0, 5);
            String b = mobile.substring(9, 11);
            mobile = a + "*****" + b;
            tv_myinfo_mobile_num.setText(mobile);
            Intent intent_result = getIntent();
            setResult(1, intent_result);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("修改个人信息页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("修改个人信息页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        if (null == userCenterInfoBean) {
            getUserInfo();
        }

    }

    /***
     * 提示框
     */
    private void showDialog() {
        mAlertView = new AlertView
                ("", "是否取消本次修改", UIUtils.getString(R.string.fabu_back_cancle), new String[]{UIUtils.getString(R.string.fabu_back_sure)},
                        null, this, AlertView.Style.Alert,"", this);
    }

    @Override
    public void onItemClick(Object object, int position,String tag) {

        switch (position) {
            case 0:
                MyInfoActivity.this.finish();
                break;
        }


    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("修改个人信息页");
        MobclickAgent.onPause(this);
    }

}
