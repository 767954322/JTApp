package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.activity.FenSiListActivity;
import com.homechart.app.home.activity.GuanZuListActivity;
import com.homechart.app.home.activity.HomeActivity;
import com.homechart.app.home.activity.LoginActivity;
import com.homechart.app.home.activity.MessagesListActivity;
import com.homechart.app.home.activity.MyInfoActivity;
import com.homechart.app.home.activity.SearchActivity;
import com.homechart.app.home.activity.SetActivity;
import com.homechart.app.home.activity.ShaiJiaListActivity;
import com.homechart.app.home.activity.ShouCangListActivity;
import com.homechart.app.home.activity.YuGouQingDanActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.lingganji.ui.activity.MyLingGanlistActivity;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ValidFragment")
public class HomeCenterFragment extends BaseFragment implements View.OnClickListener {

    private UserCenterInfoBean userCenterInfoBean;
    private FragmentManager fragmentManager;
    private RoundImageView iv_center_header;
    private RelativeLayout rl_unreader_msg_double;
    private RelativeLayout rl_unreader_msg_single;
    private RelativeLayout rl_fensi;
    private RelativeLayout rl_guanzu;
    private RelativeLayout rl_shoucang;
    private RelativeLayout rl_shaijia;
    private RelativeLayout rl_wodeanli;
    private RelativeLayout rl_yugouqingdan;
    private RelativeLayout rl_set;
    private TextView tv_center_name;
    private String mUserId;
    private TextView tv_unreader_mag_double;
    private TextView tv_unreader_mag_single;
    private TextView tv_fensi_num;
    private TextView tv_guanzhu_num;
    private TextView tv_shoucang_num;
    private TextView tv_shaijia_num;
    private ImageView iv_center_msgicon;
    private ImageView iv_zhuanye_icon;
    private Timer timer = new Timer(true);

    private int notice_num = 0;
    private int follow_notice = 0;
    private int collect_notice = 0;
    private int comment_notice = 0;
    private int system_notice = 0;
    private Boolean loginStatus;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String info = (String) msg.obj;
            int tag = msg.arg1;
            if (tag == 0) {
                userCenterInfoBean = GsonUtil.jsonToBean(info, UserCenterInfoBean.class);
                changeUI();
            } else if (tag == 1) {
                try {
                    JSONObject jsonObject = new JSONObject(info);
                    String num = jsonObject.getString("notice_num");
                    notice_num = jsonObject.getInt("notice_num");
                    follow_notice = jsonObject.getInt("follow_notice");
                    collect_notice = jsonObject.getInt("collect_notice");
                    comment_notice = jsonObject.getInt("comment_notice");
                    system_notice = jsonObject.getInt("system_notice");
                    changeUnReaderMsg(num);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    };


    public HomeCenterFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public HomeCenterFragment() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_center_pic;
    }

    @Override
    protected void initView() {
        iv_center_header = (RoundImageView) rootView.findViewById(R.id.iv_center_header);
        tv_center_name = (TextView) rootView.findViewById(R.id.tv_center_name);
        tv_fensi_num = (TextView) rootView.findViewById(R.id.tv_fensi_num);
        tv_guanzhu_num = (TextView) rootView.findViewById(R.id.tv_guanzhu_num);
        tv_shoucang_num = (TextView) rootView.findViewById(R.id.tv_shoucang_num);
        tv_shaijia_num = (TextView) rootView.findViewById(R.id.tv_shaijia_num);
        tv_unreader_mag_double = (TextView) rootView.findViewById(R.id.tv_unreader_mag_double);
        tv_unreader_mag_single = (TextView) rootView.findViewById(R.id.tv_unreader_mag_single);
        rl_fensi = (RelativeLayout) rootView.findViewById(R.id.rl_fensi);
        rl_guanzu = (RelativeLayout) rootView.findViewById(R.id.rl_guanzu);
        rl_shoucang = (RelativeLayout) rootView.findViewById(R.id.rl_shoucang);
        rl_shaijia = (RelativeLayout) rootView.findViewById(R.id.rl_shaijia);
        rl_wodeanli = (RelativeLayout) rootView.findViewById(R.id.rl_wodeanli);
        rl_yugouqingdan = (RelativeLayout) rootView.findViewById(R.id.rl_yugouqingdan);
        rl_set = (RelativeLayout) rootView.findViewById(R.id.rl_set);
        rl_unreader_msg_single = (RelativeLayout) rootView.findViewById(R.id.rl_unreader_msg_single);
        rl_unreader_msg_double = (RelativeLayout) rootView.findViewById(R.id.rl_unreader_msg_double);
        iv_center_msgicon = (ImageView) rootView.findViewById(R.id.iv_center_msgicon);
        iv_zhuanye_icon = (ImageView) rootView.findViewById(R.id.iv_zhuanye_icon);
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);

    }

    @Override
    protected void initListener() {
        super.initListener();

        rl_fensi.setOnClickListener(this);
        rl_guanzu.setOnClickListener(this);
        rl_shoucang.setOnClickListener(this);
        rl_shaijia.setOnClickListener(this);
        rl_wodeanli.setOnClickListener(this);
        rl_set.setOnClickListener(this);
        iv_center_header.setOnClickListener(this);
        iv_center_msgicon.setOnClickListener(this);
        rl_yugouqingdan.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        getUserInfo();
        getUnReaderMsg();
        try {
            timer.schedule(task, 1000, 600000);
        } catch (Exception e) {

        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_center_msgicon:
                Intent intent_messages = new Intent(activity, MessagesListActivity.class);
                intent_messages.putExtra("notice_num", notice_num);
                intent_messages.putExtra("follow_notice", follow_notice);
                intent_messages.putExtra("collect_notice", collect_notice);
                intent_messages.putExtra("comment_notice", comment_notice);
                intent_messages.putExtra("system_notice", system_notice);
                startActivityForResult(intent_messages, 2);

                break;
            case R.id.rl_fensi:
                Intent intent_fensi = new Intent(activity, FenSiListActivity.class);
                intent_fensi.putExtra(ClassConstant.LoginSucces.USER_ID, mUserId);
                startActivity(intent_fensi);

                break;
            case R.id.rl_guanzu:
                Intent intent_guanzu = new Intent(activity, GuanZuListActivity.class);
                intent_guanzu.putExtra(ClassConstant.LoginSucces.USER_ID, mUserId);
                startActivity(intent_guanzu);

                break;
            case R.id.rl_shoucang:
                Intent intent_shoucang = new Intent(activity, ShouCangListActivity.class);
                startActivity(intent_shoucang);
                break;
            case R.id.rl_shaijia:

                Intent intent_shaijia = new Intent(activity, MyLingGanlistActivity.class);
                intent_shaijia.putExtra(ClassConstant.LoginSucces.USER_ID, mUserId);
                startActivity(intent_shaijia);

//                Intent intent_shaijia = new Intent(activity, ShaiJiaListActivity.class);
//                intent_shaijia.putExtra(ClassConstant.LoginSucces.USER_ID, mUserId);
//                startActivity(intent_shaijia);

                break;
            case R.id.iv_center_header:
                Intent intent_wodeanli = new Intent(activity, MyInfoActivity.class);
                intent_wodeanli.putExtra("info", userCenterInfoBean);
                startActivityForResult(intent_wodeanli, 0);
                break;
            case R.id.rl_wodeanli:
                Intent intent_wodeanli1 = new Intent(activity, MyInfoActivity.class);
                intent_wodeanli1.putExtra("info", userCenterInfoBean);
                startActivityForResult(intent_wodeanli1, 0);

                break;
            case R.id.rl_set:

                Intent intent_set = new Intent(activity, SetActivity.class);
                startActivityForResult(intent_set, 3);

                break;
            case R.id.rl_yugouqingdan:

                Intent intent_yugou = new Intent(activity, YuGouQingDanActivity.class);
                startActivity(intent_yugou);
                break;
        }

    }

    //获取用户信息
    private void getUserInfo() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(activity, getString(R.string.userinfo_get_error));
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
                        msg.arg1 = 0;
                        handler.sendMessage(msg);


                    } else {
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getUserInfo(mUserId, callBack);


    }

    //获取未读消息数
    private void getUnReaderMsg() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(activity, getString(R.string.unreader_msg_get_error));
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
                        msg.arg1 = 1;
                        handler.sendMessage(msg);

                    } else {
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {

                }
            }
        };
        MyHttpManager.getInstance().getUnReadMsg(callBack);

    }

    private void changeUI() {

        if (null != userCenterInfoBean && null != userCenterInfoBean.getUser_info()) {
            tv_center_name.setText(userCenterInfoBean.getUser_info().getNickname());
            ImageUtils.displayRoundImage(userCenterInfoBean.getUser_info().getAvatar().getBig(), iv_center_header);

            if (!userCenterInfoBean.getUser_info().getProfession().trim().equals("0")) {//专业用户
                iv_zhuanye_icon.setVisibility(View.VISIBLE);
            } else {//普通用户
                iv_zhuanye_icon.setVisibility(View.GONE);
            }
        }
        if (null != userCenterInfoBean && null != userCenterInfoBean.getCounter()) {
            tv_fensi_num.setText(userCenterInfoBean.getCounter().getFans_num() + "");
            tv_guanzhu_num.setText(userCenterInfoBean.getCounter().getFollow_num() + "");
            tv_shoucang_num.setText(userCenterInfoBean.getCounter().getSubscribe_num() + "");
            tv_shaijia_num.setText(userCenterInfoBean.getCounter().getAlbum_num() + "");
        }

    }

    private void changeUnReaderMsg(String num) {

        int num_int = Integer.parseInt(num.trim());
        if (num_int == 0) {
            rl_unreader_msg_double.setVisibility(View.GONE);
            rl_unreader_msg_single.setVisibility(View.GONE);
        } else {
            if (num_int < 10) {
                rl_unreader_msg_double.setVisibility(View.GONE);
                rl_unreader_msg_single.setVisibility(View.VISIBLE);
                tv_unreader_mag_single.setText(num_int + "");
            } else if (10 <= num_int && num_int <= 99) {
                rl_unreader_msg_double.setVisibility(View.VISIBLE);
                rl_unreader_msg_single.setVisibility(View.GONE);
                tv_unreader_mag_double.setText(num_int + "");
            } else {
                rl_unreader_msg_double.setVisibility(View.VISIBLE);
                rl_unreader_msg_single.setVisibility(View.GONE);
                tv_unreader_mag_double.setText("99");
            }

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //修改后的返回
        if (requestCode == 0 && resultCode == 1) {
            getUserInfo();
        } else if (requestCode == 2) {
            getUnReaderMsg();
        } else if (requestCode == 3) {
            loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
            if (!loginStatus) {
                ((HomeActivity) activity).changeShowPic();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getUnReaderMsg();
        getUserInfo();
        MobclickAgent.onResume(activity);
//        if (null == userCenterInfoBean) {
//            getUserInfo();
//        }

    }

    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPause(activity);
    }

    //任务
    private TimerTask task = new TimerTask() {
        public void run() {
            getUnReaderMsg();
        }
    };
}
