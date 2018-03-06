package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.faxianpingdao.PingDaoItemBean;
import com.homechart.app.recyclerlibrary.adapter.MultiItemCommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.support.MultiItemTypeSupport;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.UIUtils;

import java.util.List;

/**
 * Created by gumenghao on 18/1/31.
 */

public class PingDaoListActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton mBack;
    private List<PingDaoItemBean> mListData;
    private RecyclerView mRecyclerView;
    private MultiItemCommonAdapter<PingDaoItemBean> mAdapter;
    private int selectPosition;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pingdao_list;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        mListData = (List<PingDaoItemBean>) getIntent().getSerializableExtra("list");
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

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                PingDaoListActivity.this.finish();
                break;
        }
    }


    private void buildHRecyclerView() {

        MultiItemTypeSupport<PingDaoItemBean> support = new MultiItemTypeSupport<PingDaoItemBean>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_pingdao_list;
            }

            @Override
            public int getItemViewType(int position, PingDaoItemBean s) {
                return 0;
            }
        };
        mAdapter = new MultiItemCommonAdapter<PingDaoItemBean>(PingDaoListActivity.this, mListData, support) {
            @Override
            public void convert(final BaseViewHolder holder, final int position) {

                ((TextView) holder.getView(R.id.tv_item_pingdao)).setText(mListData.get(position).getChannel_name());
                holder.getView(R.id.rl_pingdao_list).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = PingDaoListActivity.this.getIntent();
                        intent.putExtra("position", position);
                        PingDaoListActivity.this.setResult(11, intent);
                        PingDaoListActivity.this.finish();

                    }
                });
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PingDaoListActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

    }

}
