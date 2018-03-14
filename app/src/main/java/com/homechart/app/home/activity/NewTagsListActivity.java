package com.homechart.app.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.home.adapter.MyTagsAdapter;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.faxiantags.FaXianTagBean;
import com.homechart.app.home.bean.faxiantags.ItemGroupBean;
import com.homechart.app.myview.FlowLayoutFaXianTags;
import com.homechart.app.myview.MyGridView;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.GsonUtil;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gumenghao on 18/1/31.
 */

public class NewTagsListActivity
        extends BaseActivity
        implements View.OnClickListener,
        MyTagsAdapter.ClickItemIns ,FlowLayoutFaXianTags.OnTagClickListener{

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
                NewTagsListActivity.this.finish();
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
        mAdapter = new MultiItemCommonAdapter<ItemGroupBean>(NewTagsListActivity.this, mListData, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {
                ((TextView) holder.getView(R.id.tv_dingyue_tab)).setText(mListData.get(position).getGroup_info().getGroup_name());
//                ((MyGridView) holder.getView(R.id.gv_content)).setSelector(new ColorDrawable(Color.TRANSPARENT));
//                ((MyGridView) holder.getView(R.id.gv_content)).setAdapter(new MyTagsAdapter(mListData.get(position).getGroup_info().getTag_list(), NewTagsListActivity.this, NewTagsListActivity.this, position));
                ((FlowLayoutFaXianTags) holder.getView(R.id.fl_tags)).setColorful(true);
                ((FlowLayoutFaXianTags) holder.getView(R.id.fl_tags)).setListData(mListData.get(position).getGroup_info().getTag_list(), position);
                ((FlowLayoutFaXianTags) holder.getView(R.id.fl_tags)).setOnTagClickListener(NewTagsListActivity.this);

            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NewTagsListActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

    }

    private void getDingYueData() {

        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showCenter(NewTagsListActivity.this, "标签导航列表获取失败");
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
                        ToastUtils.showCenter(NewTagsListActivity.this, error_msg);
                    }
                } catch (JSONException e) {
                }
            }
        };
        MyHttpManager.getInstance().getTagList(callBack);


    }

//    @Override
//    public void tagClick(int position, int numTag) {
//
//        Intent intent = NewTagsListActivity.this.getIntent();
//        intent.putExtra("position", position);
//        String tag_name = mListData.get(numTag).getGroup_info().getTag_list().get(position).getTag_info().getTag_name();
//        intent.putExtra("tag_name", tag_name);
//        NewTagsListActivity.this.setResult(11, intent);
//        NewTagsListActivity.this.finish();
//
//    }

    @Override
    public void clickItemPosition(int position, int numTag) {
        Intent intent = NewTagsListActivity.this.getIntent();
        intent.putExtra("position", position);
        String tag_name = mListData.get(numTag).getGroup_info().getTag_list().get(position).getTag_info().getTag_name();

        //友盟统计
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("evenname", "标签点击");
        map.put("even", tag_name + "标签导航点击标签的次数");
        MobclickAgent.onEvent(NewTagsListActivity.this, "shijian44", map);
        //ga统计
        MyApplication.getInstance().getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory(tag_name + "标签导航点击频道标签的次数")  //事件类别
                .setAction("标签点击")      //事件操作
                .build());

        intent.putExtra("tag_name", tag_name);
        NewTagsListActivity.this.setResult(11, intent);
        NewTagsListActivity.this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("标签导航页");
        Tracker t = MyApplication.getInstance().getDefaultTracker();
        // Set screen name.
        t.setScreenName("标签导航页");
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("标签导航页");
        MobclickAgent.onPause(this);

    }

    @Override
    public void tagClick(int position, int numTag) {
       clickItemPosition(position,numTag);
    }
}
