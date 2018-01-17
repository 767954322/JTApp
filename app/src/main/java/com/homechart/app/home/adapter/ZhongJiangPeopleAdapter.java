package com.homechart.app.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.huodongpeople.ZhongPrizeInfoItemBean;

import java.util.List;

/**
 * Created by gumenghao on 18/1/17.
 */

public class ZhongJiangPeopleAdapter extends BaseAdapter{

    private List<ZhongPrizeInfoItemBean> listUser ;
    private Context mContext ;


    public ZhongJiangPeopleAdapter(List<ZhongPrizeInfoItemBean> listUser, Context mContext) {
        this.listUser = listUser;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return listUser.size();
    }

    @Override
    public Object getItem(int position) {
        return listUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder myHolder = null;
        if(convertView == null){
            myHolder = new MyHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_zhongjiang_list_people,null);
            myHolder.tv_people_name = (TextView) convertView.findViewById(R.id.tv_people_name);
            convertView.setTag(myHolder);
        }else {
            myHolder = (MyHolder) convertView.getTag();
        }
        myHolder.tv_people_name.setText(listUser.get(position).getNickname());

        return convertView;
    }

    class MyHolder{
        private TextView tv_people_name;
    }

}
