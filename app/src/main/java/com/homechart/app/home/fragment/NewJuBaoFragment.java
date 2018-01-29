package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.contract.ItemClickJuBao;
import com.homechart.app.home.activity.DesinerInfoHeaderActivity;
import com.homechart.app.home.activity.FenSiListActivity;
import com.homechart.app.home.activity.GuanZuListActivity;
import com.homechart.app.home.activity.ImageDetailScrollActivity;
import com.homechart.app.home.activity.JuBaoActivity;
import com.homechart.app.home.activity.LoginActivity;
import com.homechart.app.home.adapter.MyJuBaoAdapter;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.jubaobean.JuBaoBean;
import com.homechart.app.home.bean.jubaobean.JuBaoItemBean;
import com.homechart.app.home.bean.userimage.ImageDataBean;
import com.homechart.app.home.bean.userimage.UserImageBean;
import com.homechart.app.home.bean.userinfo.UserCenterInfoBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationBean;
import com.homechart.app.lingganji.common.entity.inspirationlist.InspirationListBean;
import com.homechart.app.lingganji.ui.activity.InspirationDetailActivity;
import com.homechart.app.myview.MyListView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.recyclerlibrary.recyclerview.OnLoadMoreListener;
import com.homechart.app.recyclerlibrary.recyclerview.OnRefreshListener;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("ValidFragment")
public class NewJuBaoFragment
        extends BaseFragment
        implements View.OnClickListener, ItemClickJuBao {

    private String item_id;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private TextView tv_content_right;
    private TextView et_liyou;
    private MyListView mlv_listview;
    private int clickPosition = -1;
    private List<JuBaoItemBean> mList = new ArrayList<>();
    private MyJuBaoAdapter myAdapter;
    private  FragmentManager fragmentManager;
    private Bundle mBundle;

    public NewJuBaoFragment() {
    }

    public NewJuBaoFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_jubao;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        mBundle = getArguments();
        item_id = (String) mBundle.getString("item_id");
    }

    @Override
    protected void initView() {
        nav_left_imageButton = (ImageButton) rootView.findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) rootView.findViewById(R.id.tv_tital_comment);
        tv_content_right = (TextView) rootView.findViewById(R.id.tv_content_right);
        mlv_listview = (MyListView) rootView.findViewById(R.id.mlv_listview);
        et_liyou = (TextView) rootView.findViewById(R.id.et_liyou);
    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        tv_tital_comment.setText("举报图片");
        tv_content_right.setText("提交");

        myAdapter = new MyJuBaoAdapter(activity, mList, this);
        mlv_listview.setAdapter(myAdapter);
        getJuBaoList();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                fragmentManager.popBackStack();
                break;
            case R.id.tv_content_right:
                if (-1 == clickPosition) {
                    ToastUtils.showCenter(activity, "请先选择一项再提交举报哦~");
                } else {
                    if (3 == clickPosition && mList.size() > 0 && mList.size() > clickPosition) {
                        if (et_liyou.getText().toString().trim().equals("")) {
                            ToastUtils.showCenter(activity, "需要输入内容才可以提交");
                        } else {
                            jubao();
                        }
                    } else {
                        if (-1 != clickPosition && mList.size() > 0 && mList.size() > clickPosition) {
                            jubao();
                        }
                    }
                }
                break;
        }
    }

    private void jubao() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(activity, "举报选项获取失败！");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        ToastUtils.showCenter(activity, "举报成功，我们会尽快核实！");
                        fragmentManager.popBackStack();
                    } else {
                        ToastUtils.showCenter(activity, "抱歉，您的举报没有发送成功，请稍后再试!");
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().juBaoImage(et_liyou.getText().toString().trim(), ClassConstant.JuBao.ITEM, item_id, mList.get(clickPosition).getReport_id() + "", callBack);
    }

    private void getJuBaoList() {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(activity, "举报选项获取失败！");
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
                        msg.obj = "{\"data\":" + data_msg + "}";
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(activity, "举报选项获取失败！");
                }
            }
        };
        MyHttpManager.getInstance().getJuBaoList("item", callBack);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    String str = (String) msg.obj;
                    JuBaoBean juBaoBean = GsonUtil.jsonToBean(str, JuBaoBean.class);
                    mList.addAll(juBaoBean.getData());
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void clickItem(int position) {
        this.clickPosition = position;
        myAdapter.notifyDataSetChanged();
    }
}