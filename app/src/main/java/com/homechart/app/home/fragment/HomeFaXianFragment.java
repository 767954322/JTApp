package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.activity.JuBaoActivity;
import com.homechart.app.home.adapter.MyFaXianAdapter;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.home.bean.faxianpingdao.PingDaoBean;
import com.homechart.app.myview.HorizontalListView;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("ValidFragment")
public class HomeFaXianFragment
        extends BaseFragment {

    private FragmentManager fragmentManager;
    public PingDaoBean pingDaoBean;
    private HorizontalListView hlv_tab1;
    private HorizontalListView hlv_tab2;
    private MyFaXianAdapter myFaXianAdapter;


    public HomeFaXianFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public HomeFaXianFragment() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_faxian;
    }

    @Override
    protected void initView() {

        hlv_tab1 = (HorizontalListView) rootView.findViewById(R.id.hlv_tab1);
        hlv_tab2 = (HorizontalListView) rootView.findViewById(R.id.hlv_tab2);

    }

    @Override
    protected void initListener() {
        super.initListener();
        hlv_tab1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myFaXianAdapter.setSelectPosition(position);
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        getPingDaoTag();

    }

    private void getPingDaoTag() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(activity, "频道列表信息获取失败");
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        Message message = new Message();
                        message.obj = data_msg;
                        message.what = 1;
                        mHandler.sendMessage(message);
                        Log.d("test", data_msg);
                    } else {
                        ToastUtils.showCenter(activity, error_msg);
                    }
                } catch (JSONException e) {
                    ToastUtils.showCenter(activity, "频道列表信息获取失败");
                }
            }
        };
        MyHttpManager.getInstance().getPingDaoTags(callBack);

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String dataStr = (String) msg.obj;
                    pingDaoBean = GsonUtil.jsonToBean(dataStr, PingDaoBean.class);
                    pingDaoBean.getChannel_list();
                    myFaXianAdapter = new MyFaXianAdapter(pingDaoBean.getChannel_list(), activity);
                    hlv_tab1.setAdapter(myFaXianAdapter);
                    break;
            }
        }
    };

}
