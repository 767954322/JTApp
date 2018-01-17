package com.homechart.app.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.huodongpeople.ZhongItemDataBean;
import com.homechart.app.home.bean.huodongpeople.ZhongPrizeInfoItemBean;
import com.homechart.app.myview.NoScrollGridView;

import java.util.List;

/**
 * Created by gumenghao on 18/1/17.
 */

public class MyZhongJiangAdapter extends BaseAdapter {


    private List<ZhongItemDataBean> mList;
    private Context mContext;

    public MyZhongJiangAdapter(List<ZhongItemDataBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder myHolder = null;
        if (convertView == null) {
            myHolder = new MyHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_zhongjiang_list, null);
            myHolder.sgv_gridview = (NoScrollGridView) convertView.findViewById(R.id.sgv_gridview);
            myHolder.tv_mingci = (TextView) convertView.findViewById(R.id.tv_mingci);
            myHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }

        myHolder.tv_mingci.setText(mList.get(position).getPrize_info().getGrade());
        myHolder.tv_name.setText(mList.get(position).getPrize_info().getName());
        List<ZhongPrizeInfoItemBean> listUser =  mList.get(position).getPrize_info().getUser_list();
        ZhongJiangPeopleAdapter adapter = new ZhongJiangPeopleAdapter(listUser,mContext);
        myHolder.sgv_gridview.setAdapter(adapter);

        return convertView;
    }

    class MyHolder {

        private TextView tv_mingci;
        private TextView tv_name;
        private NoScrollGridView sgv_gridview;

    }

}
