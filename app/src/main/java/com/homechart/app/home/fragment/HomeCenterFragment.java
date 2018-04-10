package com.homechart.app.home.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.activity.ArticleDetailsActivity;
import com.homechart.app.home.activity.DingYueGuanLiActivity;
import com.homechart.app.home.activity.FaBuActvity;
import com.homechart.app.home.activity.FaBuImageActivity;
import com.homechart.app.home.activity.HomeActivity;
import com.homechart.app.home.activity.MyInfoActivity;
import com.homechart.app.home.activity.MyWebViewActivity;
import com.homechart.app.home.activity.SetActivity;
import com.homechart.app.home.activity.YuGouQingDanActivity;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.myview.CaiJiPopWin;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.visearch.PhotoActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

@SuppressLint("ValidFragment")
public class HomeCenterFragment
        extends BaseFragment
        implements View.OnClickListener,
        NewMessagesFragment.BackMessage,
        CaiJiPopWin.ClickInter {

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
    private RelativeLayout rl_dingyue_guanli;
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
    private ImageView iv_fabu;
    private Timer timer = new Timer(true);
    private CaiJiPopWin caijiPop;

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
                    changeUnReaderMsg(num);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (tag == 2) {

                String url_Imag = (String) msg.obj;

                Intent intent = new Intent(activity, FaBuImageActivity.class);
                intent.putExtra("image_url", url_Imag);
                intent.putExtra("webUrl", "");
                intent.putExtra("type", "location");
                startActivityForResult(intent, 5);

//                List<String> listUrl = new ArrayList<>();
//                listUrl.add(url_Imag);
//                Intent intent = new Intent(activity, ImageBenDiActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("pic_url_list", (Serializable) listUrl);
//                bundle.putInt("click_position", 0);
//                intent.putExtras(bundle);
//                startActivityForResult(intent,5);
//                Intent intent = new Intent(activity, FaBuActvity.class);
//                intent.putExtra("image_path", url_Imag);
//                startActivity(intent);
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
        rl_dingyue_guanli = (RelativeLayout) rootView.findViewById(R.id.rl_dingyue_guanli);
        rl_set = (RelativeLayout) rootView.findViewById(R.id.rl_set);
        rl_unreader_msg_single = (RelativeLayout) rootView.findViewById(R.id.rl_unreader_msg_single);
        rl_unreader_msg_double = (RelativeLayout) rootView.findViewById(R.id.rl_unreader_msg_double);
        iv_center_msgicon = (ImageView) rootView.findViewById(R.id.iv_center_msgicon);
        iv_zhuanye_icon = (ImageView) rootView.findViewById(R.id.iv_zhuanye_icon);
        iv_fabu = (ImageView) rootView.findViewById(R.id.iv_fabu);

    }

    @Override
    protected void initListener() {
        super.initListener();

        iv_fabu.setOnClickListener(this);
        rl_fensi.setOnClickListener(this);
        rl_guanzu.setOnClickListener(this);
        rl_shoucang.setOnClickListener(this);
        rl_shaijia.setOnClickListener(this);
        rl_wodeanli.setOnClickListener(this);
        rl_set.setOnClickListener(this);
        iv_center_header.setOnClickListener(this);
        iv_center_msgicon.setOnClickListener(this);
        rl_yugouqingdan.setOnClickListener(this);
        rl_dingyue_guanli.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);

        caijiPop = new CaiJiPopWin(activity, this);
        if (!TextUtils.isEmpty(mUserId)) {
            getUserInfo();
            getUnReaderMsg();
            try {
                timer.schedule(task, 1000, 60000);
            } catch (Exception e) {

            }
        }
    }

    public void flushData() {
        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
        if (!TextUtils.isEmpty(mUserId)) {
            getUserInfo();
            getUnReaderMsg();
            try {
                timer.schedule(task, 1000, 60000);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_center_msgicon:
                //友盟统计
                HashMap<String, String> map5 = new HashMap<String, String>();
                map5.put("evenname", "点击消息中心");
                map5.put("even", "点击消息中心查看消息的次数");
                MobclickAgent.onEvent(activity, "shijian29", map5);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("点击消息中心查看消息的次数")  //事件类别
                        .setAction("点击消息中心")      //事件操作
                        .build());
                NewMessagesFragment newMessagesFragment = new NewMessagesFragment(getChildFragmentManager(), this);
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null).replace(R.id.id_main, newMessagesFragment);
                fragmentTransaction.commit();
                break;
            case R.id.rl_fensi:

                NewFenSiListFragment newFenSiListFragment = new NewFenSiListFragment(getChildFragmentManager());
                Bundle bundle = new Bundle();
                bundle.putString(ClassConstant.LoginSucces.USER_ID, mUserId);
                newFenSiListFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction1 = getChildFragmentManager().beginTransaction();
                fragmentTransaction1.addToBackStack(null).replace(R.id.id_main, newFenSiListFragment);
                fragmentTransaction1.commit();

                break;
            case R.id.rl_guanzu:
                NewGuanZuListFragment newGuanZuListFragment = new NewGuanZuListFragment(getChildFragmentManager());
                Bundle bundle_guanzu = new Bundle();
                bundle_guanzu.putString(ClassConstant.LoginSucces.USER_ID, mUserId);
                newGuanZuListFragment.setArguments(bundle_guanzu);
                FragmentTransaction fragmentTransaction_guanzu = getChildFragmentManager().beginTransaction();
                fragmentTransaction_guanzu.addToBackStack(null).replace(R.id.id_main, newGuanZuListFragment);
                fragmentTransaction_guanzu.commit();

                break;
            case R.id.rl_shoucang:

                NewLingGanCenterFragment newLingGanCenterFragment = new NewLingGanCenterFragment(getChildFragmentManager());
                Bundle bundle_linggan = new Bundle();
                bundle_linggan.putString(ClassConstant.LoginSucces.USER_ID, mUserId);
                newLingGanCenterFragment.setArguments(bundle_linggan);
                FragmentTransaction fragmentTransaction_linggan = getChildFragmentManager().beginTransaction();
                fragmentTransaction_linggan.addToBackStack(null).replace(R.id.id_main, newLingGanCenterFragment);
                fragmentTransaction_linggan.commit();

                break;
            case R.id.rl_shaijia:
                //友盟统计
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("evenname", "图片查看");
                map.put("even", "我的");
                MobclickAgent.onEvent(activity, "shijian36", map);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("我的")  //事件类别
                        .setAction("图片查看")      //事件操作
                        .build());
                NewMyPicCenterFragment newMyPicCenterFragment = new NewMyPicCenterFragment(getChildFragmentManager());
                FragmentTransaction fragmentTransaction_pic = getChildFragmentManager().beginTransaction();
                fragmentTransaction_pic.addToBackStack(null).replace(R.id.id_main, newMyPicCenterFragment);
                fragmentTransaction_pic.commit();

//                NewPicCenterFragment newPicCenterFragment = new NewPicCenterFragment(getChildFragmentManager());
//                FragmentTransaction fragmentTransaction_piccenter = getChildFragmentManager().beginTransaction();
//                fragmentTransaction_piccenter.addToBackStack(null).replace(R.id.id_main, newPicCenterFragment);
//                fragmentTransaction_piccenter.commit();

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
                //友盟统计
                HashMap<String, String> map4 = new HashMap<String, String>();
                map4.put("evenname", "收藏商品查看");
                map4.put("even", "用户查看预购清单的次数");
                MobclickAgent.onEvent(activity, "shijian24", map4);
                //ga统计
                MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                        .setCategory("用户查看预购清单的次数")  //事件类别
                        .setAction("收藏商品查看")      //事件操作
                        .build());

                Intent intent_yugou = new Intent(activity, YuGouQingDanActivity.class);
                startActivity(intent_yugou);
                break;
            case R.id.rl_dingyue_guanli:
                Intent intent_dingyue = new Intent(activity, DingYueGuanLiActivity.class);
                startActivity(intent_dingyue);
                break;
            case R.id.iv_fabu:
                if (null != caijiPop) {
                    caijiPop.showAtLocation(activity.findViewById(R.id.id_main),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                            0,
                            0); //设置layout在PopupWindow中显示的位置
                } else {

                    caijiPop = new CaiJiPopWin(activity, this);
                    caijiPop.showAtLocation(activity.findViewById(R.id.id_main),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                            0,
                            0); //设置layout在PopupWindow中显示的位置

                }


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
            tv_shoucang_num.setText(userCenterInfoBean.getCounter().getAlbum_num() + userCenterInfoBean.getCounter().getCollect_album_num() + "");
            tv_shaijia_num.setText(userCenterInfoBean.getCounter().getItem_num() + "");
        }

    }

    private void changeUnReaderMsg(String num) {

        int num_int = Integer.parseInt(num.trim());
        if (num_int == 0) {

            ((HomeActivity)activity).visiableUnRedIcon(false);
            rl_unreader_msg_double.setVisibility(View.GONE);
            rl_unreader_msg_single.setVisibility(View.GONE);
        } else {

            ((HomeActivity)activity).visiableUnRedIcon(true);
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


        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
        //修改后的返回
        if (requestCode == 0 && resultCode == 1) {
            if (!TextUtils.isEmpty(mUserId)) {
                getUserInfo();
            }
        } else if (requestCode == 2) {
            if (!TextUtils.isEmpty(mUserId)) {
                getUnReaderMsg();
            }
        } else if (requestCode == 3) {
            loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);
            if (!loginStatus) {
                ((HomeActivity) activity).backPic();
                ((HomeActivity) activity).visiableUnRedIcon(false);
            }
        }else if (requestCode == 5 && resultCode == 5) {
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
            ClassConstant.HomeStatus.IMAGE_STATUS = 1;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mUserId = SharedPreferencesUtils.readString(ClassConstant.LoginSucces.USER_ID);
        if (!TextUtils.isEmpty(mUserId)) {
            getUnReaderMsg();
            getUserInfo();
        }
        MobclickAgent.onPageStart("我的");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("我的");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("我的");
    }

    //任务
    private TimerTask task = new TimerTask() {
        public void run() {
            getUnReaderMsg();
        }
    };

    @Override
    public void clickBackMessage() {
        getUnReaderMsg();
    }

    @Override
    public void xiangceCaiJi() {

        //友盟统计
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("evenname", "相册发布");
        map.put("even", "相册发布-我的");
        MobclickAgent.onEvent(activity, "shijian53", map);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("相册发布-我的")  //事件类别
                .setAction("相册发布")      //事件操作
                .build());

        GalleryFinal.openGallerySingle(33, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList != null && resultList.size() > 0) {
                    Message message = new Message();
                    message.arg1 = 2;
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


        //友盟统计
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("evenname", "拍照发布");
        map.put("even", "拍照发布-我的");
        MobclickAgent.onEvent(activity, "shijian55", map);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("拍照发布-我的")  //事件类别
                .setAction("拍照发布")      //事件操作
                .build());

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA}, 24);
        } else {
            GalleryFinal.openCamera(34, new GalleryFinal.OnHanlderResultCallback() {
                @Override
                public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                    if (resultList != null && resultList.size() > 0) {
                        Message message = new Message();
                        message.arg1 = 2;
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
    }

    @Override
    public void wangzhiCaiJi() {

        //友盟统计
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("evenname", "采集");
        map.put("even", "采集-我的");
        MobclickAgent.onEvent(activity, "shijian54", map);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("采集-我的")  //事件类别
                .setAction("采集")      //事件操作
                .build());
        Intent intent = new Intent(activity, MyWebViewActivity.class);
        intent.putExtra("weburl", "");
        startActivity(intent);

    }
}
