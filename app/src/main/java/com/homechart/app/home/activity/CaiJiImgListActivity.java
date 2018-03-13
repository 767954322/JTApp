package com.homechart.app.home.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.homechart.app.R;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.fensi.UserListBean;
import com.homechart.app.home.recyclerholder.LoadMoreFooterView;
import com.homechart.app.myview.MyGridView;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.myview.SpaceItemDecoration;
import com.homechart.app.recyclerlibrary.adapter.CommonAdapter;
import com.homechart.app.recyclerlibrary.holder.BaseViewHolder;
import com.homechart.app.recyclerlibrary.recyclerview.HRecyclerView;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.glide.GlideImgManager;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.visenze.visearch.android.model.Image;

import java.util.List;

/**
 * Created by gumenghao on 18/3/13.
 */

public class CaiJiImgListActivity
        extends BaseActivity
        implements View.OnClickListener {
    private List<String> imageLists;
    private GridView gv_gridview;
    private ImageButton mIBBack;
    private CommonAdapter<String> mAdapter;
    private int widthScreen;
    private MyAdapter myAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_caiji_list;
    }


    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        imageLists = (List<String>) getIntent().getSerializableExtra("pic_url_list");

    }

    @Override
    protected void initView() {

        gv_gridview = (GridView) findViewById(R.id.gv_gridview);
        mIBBack = (ImageButton) findViewById(R.id.nav_left_imageButton);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mIBBack.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        widthScreen = (PublicUtils.getScreenWidth(this) - UIUtils.getDimens(R.dimen.font_30)) / 3;
//        widthScreen = PublicUtils.getScreenWidth(this) / 3;
        myAdapter = new MyAdapter(this, imageLists);
        gv_gridview.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                CaiJiImgListActivity.this.finish();
                break;
        }
    }

    class MyAdapter extends BaseAdapter {

        private Context context;
        private List<String> list;

        public MyAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MyHolder myHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_caiji_image, null);
                myHolder = new MyHolder();
                myHolder.iv_imageview = (ImageView) convertView.findViewById(R.id.iv_item_caiji);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) myHolder.iv_imageview.getLayoutParams();
            layoutParams.width = widthScreen;
            layoutParams.height = widthScreen;
            myHolder.iv_imageview.setLayoutParams(layoutParams);
            GlideImgManager.glideLoader(CaiJiImgListActivity.this, list.get(position), R.color.white, R.color.white, myHolder.iv_imageview, 1);
//            ImageUtils.displayFilletImage(list.get(position), myHolder.iv_imageview);
            return convertView;
        }

        class MyHolder {
            private ImageView iv_imageview;
        }
    }

}
