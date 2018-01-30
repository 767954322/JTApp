package com.homechart.app.home.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.faxianpingdao.PingDaoItemBean;

import java.util.List;

/**
 * Created by gumenghao on 18/1/30.
 */

public class MyFaXianAdapter1 extends BaseAdapter {

    private List<String> mList;
    private Context mContext;
    private int selectPosition = 0;

    public MyFaXianAdapter1(List<String> mList, Context mContext) {
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

        MyHolder myHolder;
        if (convertView == null) {
            myHolder = new MyHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pingdao1, null);
            myHolder.tv_item_pingdao = (TextView) convertView.findViewById(R.id.tv_item_pingdao);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }

        myHolder.tv_item_pingdao.setText(mList.get(position));

        return convertView;
    }


    class MyHolder {
        private TextView tv_item_pingdao;
    }

}
