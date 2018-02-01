package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.dingyueguanli.DYItemBean;
import com.homechart.app.home.bean.dingyueguanli.DYItemGroupItemBean;
import com.homechart.app.home.bean.dingyueguanli.DYlistBean;
import com.homechart.app.home.bean.faxianpingdao.PingDaoItemBean;
import com.homechart.app.myview.FlowLayoutDingYueTags;
import com.homechart.app.myview.FlowLayoutFaBuTags;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 18/2/1.
 */

public class DingYueGuanLiActivity extends BaseActivity implements View.OnClickListener, FlowLayoutDingYueTags.OnTagClickListener {


    private TextView mTital;
    private TextView mRight;
    private ImageButton mBack;
    private RecyclerView mRecyclerView;
    private MultiItemCommonAdapter<DYItemBean> mAdapter;
    private List<DYItemBean> mListData = new ArrayList<>();
    private Map<String, DYItemGroupItemBean> mMapSelect = new HashMap<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            switch (code) {
                case 1:
                    String json = (String) msg.obj;
                    DYlistBean dYlistBean = GsonUtil.jsonToBean(json, DYlistBean.class);
                    List<DYItemBean> group_list = dYlistBean.getGroup_list();
                    mListData.clear();
                    mListData.addAll(group_list);
                    for (int i = 0; i < mListData.size(); i++) {
                        for (int j = 0; j < mListData.get(i).getGroup_info().getTag_list().size(); j++) {
                            if (mListData.get(i).getGroup_info().getTag_list().get(j).getTag_info().getIs_subscribed().equals("1")) {
                                mMapSelect.put(mListData.get(i).getGroup_info().getTag_list().get(j).getTag_info().getTag_id(), mListData.get(i).getGroup_info().getTag_list().get(j));
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_guanli_dingyue;
    }

    @Override
    protected void initView() {

        mTital = (TextView) findViewById(R.id.tv_tital_comment);
        mRight = (TextView) findViewById(R.id.tv_content_right);
        mBack = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_list);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mBack.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        mTital.setText("订阅管理");
        mRight.setText("完成");
        buildHRecyclerView();
        getDingYueData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                DingYueGuanLiActivity.this.finish();
                break;
        }
    }

    private void buildHRecyclerView() {

        MultiItemTypeSupport<DYItemBean> support = new MultiItemTypeSupport<DYItemBean>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_dingyue_guanli;
            }

            @Override
            public int getItemViewType(int position, DYItemBean s) {
                return 0;
            }
        };
        mAdapter = new MultiItemCommonAdapter<DYItemBean>(DingYueGuanLiActivity.this, mListData, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {
                ((TextView) holder.getView(R.id.tv_dingyue_tab)).setText(mListData.get(position).getGroup_info().getGroup_name());

                ((FlowLayoutDingYueTags) holder.getView(R.id.fl_tags)).setColorful(true);
                ((FlowLayoutDingYueTags) holder.getView(R.id.fl_tags)).setListData(mListData.get(position).getGroup_info().getTag_list(), position);
                ((FlowLayoutDingYueTags) holder.getView(R.id.fl_tags)).setOnTagClickListener(DingYueGuanLiActivity.this);

            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DingYueGuanLiActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

    }

    private void getDingYueData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(DingYueGuanLiActivity.this, "订阅列表获取失败");
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
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    } else {
                        ToastUtils.showCenter(DingYueGuanLiActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getDingYueGuanLiList(callBack);


    }


    @Override
    public void tagClick(int position, int numTag) {


        String is_subscribed = mListData.get(numTag).getGroup_info().getTag_list().get(position).getTag_info().getIs_subscribed();
        if (is_subscribed.equals("0")) {//已经订阅
            mMapSelect.remove(mListData.get(numTag).getGroup_info().getTag_list().get(position).getTag_info().getTag_id());
        } else {
            mMapSelect.put(mListData.get(numTag).getGroup_info().getTag_list().get(position).getTag_info().getTag_id(), mListData.get(numTag).getGroup_info().getTag_list().get(position));
        }
    }
}
