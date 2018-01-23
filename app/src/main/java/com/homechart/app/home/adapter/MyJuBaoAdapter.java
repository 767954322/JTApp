package com.homechart.app.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.contract.ItemClickJuBao;
import com.homechart.app.home.bean.jubaobean.JuBaoItemBean;

import java.util.List;

/**
 * Created by gumenghao on 18/1/23.
 */

public class MyJuBaoAdapter extends BaseAdapter {


    private int clickPosition = 0;
    private Context mContext;
    private List<JuBaoItemBean> mList;
    private ItemClickJuBao itemClickJuBao;

    public MyJuBaoAdapter(Context mContext, List<JuBaoItemBean> mList,ItemClickJuBao itemClickJuBao) {
        this.mContext = mContext;
        this.mList = mList;
        this.itemClickJuBao = itemClickJuBao;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyHolder myHolder = null;
        if (convertView == null) {
            myHolder = new MyHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_jubao, null);
            myHolder.rb_radio = (RadioButton) convertView.findViewById(R.id.rb_radio);
            myHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            myHolder.rl_item_all = (RelativeLayout) convertView.findViewById(R.id.rl_item_all);
            convertView.setTag(myHolder);

        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        myHolder.tv_name.setText(mList.get(position).getReport_value());

        if (clickPosition == position) {
            myHolder.rb_radio.setChecked(true);
        } else {
            myHolder.rb_radio.setChecked(false);
        }

        myHolder.rl_item_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPosition = position;
                itemClickJuBao.clickItem(position);
            }
        });
        return convertView;
    }

    class MyHolder {

        private RadioButton rb_radio;
        private TextView tv_name;
        private RelativeLayout rl_item_all;
    }

    public void setClickPosition(int clickPosition) {
        this.clickPosition = clickPosition;
    }
}
