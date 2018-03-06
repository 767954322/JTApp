package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.dingyueguanli.DYItemBean;
import com.homechart.app.home.bean.dingyueguanli.DYlistBean;
import com.homechart.app.home.bean.faxianpingdao.PingDaoItemBean;
import com.homechart.app.home.bean.faxiantags.FaXianTagBean;
import com.homechart.app.home.bean.faxiantags.ItemGroupBean;
import com.homechart.app.myview.FlowLayoutDingYueTags;
import com.homechart.app.myview.FlowLayoutFaXianTags;
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
import java.util.List;

/**
 * Created by gumenghao on 18/1/31.
 */

public class TagsListActivity extends BaseActivity implements View.OnClickListener, FlowLayoutFaXianTags.OnTagClickListener {
    private ImageButton mBack;
    private RecyclerView mRecyclerView;
    private MultiItemCommonAdapter<ItemGroupBean> mAdapter;
    private int selectPosition;

    private List<ItemGroupBean> mListData = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            switch (code) {
                case 1:
                    String json = "{\"data\": " + (String) msg.obj + "}";
                    FaXianTagBean faXianTagBean = GsonUtil.jsonToBean(json, FaXianTagBean.class);
                    List<ItemGroupBean> group_list = faXianTagBean.getData().getGroup_list();

//                    DYlistBean dYlistBean = GsonUtil.jsonToBean(json, DYlistBean.class);
//                    List<DYItemBean> group_list = dYlistBean.getGroup_list();
                    mListData.clear();
                    mListData.addAll(group_list);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pingdao_list;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
    }

    @Override
    protected void initView() {

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

        buildHRecyclerView();
        getDingYueData();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                TagsListActivity.this.finish();
                break;
        }
    }


    //getTagList

    private void buildHRecyclerView() {

        MultiItemTypeSupport<ItemGroupBean> support = new MultiItemTypeSupport<ItemGroupBean>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_faxian_tags;
            }

            @Override
            public int getItemViewType(int position, ItemGroupBean s) {
                return 0;
            }
        };
        mAdapter = new MultiItemCommonAdapter<ItemGroupBean>(TagsListActivity.this, mListData, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {
                ((TextView) holder.getView(R.id.tv_dingyue_tab)).setText(mListData.get(position).getGroup_info().getGroup_name());

                ((FlowLayoutFaXianTags) holder.getView(R.id.fl_tags)).setColorful(true);
                ((FlowLayoutFaXianTags) holder.getView(R.id.fl_tags)).setListData(mListData.get(position).getGroup_info().getTag_list(), position);
                ((FlowLayoutFaXianTags) holder.getView(R.id.fl_tags)).setOnTagClickListener(TagsListActivity.this);

            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TagsListActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

    }

    private void getDingYueData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(TagsListActivity.this, "标签导航列表获取失败");
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
                        ToastUtils.showCenter(TagsListActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getTagList(callBack);


    }

    @Override
    public void tagClick(int position, int numTag) {

        Intent intent = TagsListActivity.this.getIntent();
        intent.putExtra("position", position);
        String tag_name = mListData.get(numTag).getGroup_info().getTag_list().get(position).getTag_info().getTag_name();
        intent.putExtra("tag_name", tag_name);
        TagsListActivity.this.setResult(11, intent);
        TagsListActivity.this.finish();

    }

}
