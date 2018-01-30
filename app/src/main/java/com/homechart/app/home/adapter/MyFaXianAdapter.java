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

public class MyFaXianAdapter extends BaseAdapter {

    private List<PingDaoItemBean> mList;
    private Context mContext;
    private int selectPosition = 0;

    public MyFaXianAdapter(List<PingDaoItemBean> mList, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pingdao, null);
            myHolder.view_one_show = convertView.findViewById(R.id.view_one_show);
            myHolder.view_bottom = convertView.findViewById(R.id.view_bottom);
            myHolder.rl_item_pingdao = (RelativeLayout) convertView.findViewById(R.id.rl_item_pingdao);
            myHolder.tv_item_pingdao = (TextView) convertView.findViewById(R.id.tv_item_pingdao);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }

        if (position == 0) {
            myHolder.view_one_show.setVisibility(View.VISIBLE);
        } else {
            myHolder.view_one_show.setVisibility(View.GONE);
        }

        myHolder.tv_item_pingdao.setText(mList.get(position).getTag_name());
        TextPaint tp = myHolder.tv_item_pingdao.getPaint();
        if (selectPosition == position) {
            tp.setFakeBoldText(true);
            myHolder.view_bottom.setVisibility(View.VISIBLE);
        } else {
            tp.setFakeBoldText(false);
            myHolder.view_bottom.setVisibility(View.GONE);
        }


        return convertView;
    }


    class MyHolder {
        private View view_one_show;
        private View view_bottom;
        private RelativeLayout rl_item_pingdao;
        private TextView tv_item_pingdao;
    }


    public void setSelectPosition(int position) {
        this.selectPosition = position;
        notifyDataSetChanged();
    }

}
