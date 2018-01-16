package com.homechart.app.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.hddetails.ActivityPrizeItemBean;

import java.util.List;

/**
 * Created by gumenghao on 18/1/16.
 */

public class MyHuoDongJiangAdapter extends BaseAdapter {

    private List<ActivityPrizeItemBean> listPrize;
    private Context context;


    public MyHuoDongJiangAdapter(List<ActivityPrizeItemBean> listPrize, Context context) {
        this.listPrize = listPrize;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listPrize.size();
    }

    @Override
    public Object getItem(int position) {
        return listPrize.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_jiangpin, null);
            myHolder.tv_tital = (TextView) convertView.findViewById(R.id.tv_tital);
            myHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        myHolder.tv_tital.setText(listPrize.get(position).getGrade() + "：");
        myHolder.tv_content.setText(listPrize.get(position).getName());

        return convertView;
    }

    class MyHolder {

        private TextView tv_tital;
        private TextView tv_content;

    }
}
