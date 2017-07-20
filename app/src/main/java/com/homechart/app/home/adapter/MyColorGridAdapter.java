package com.homechart.app.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.imagedetail.ColorInfoBean;
import com.homechart.app.utils.UIUtils;

import java.util.List;

/**
 * Created by gumenghao on 17/7/4.
 */

public class MyColorGridAdapter extends BaseAdapter {

    private List<ColorInfoBean> listColor;
    private Context context;

    public MyColorGridAdapter(List<ColorInfoBean> listColor, Context context) {
        this.listColor = listColor;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listColor.size();
    }

    @Override
    public Object getItem(int position) {
        return listColor.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyColorHolder myColorHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_color_per, null);
            myColorHolder = new MyColorHolder();
            myColorHolder.view_color = convertView.findViewById(R.id.view_color);
            myColorHolder.value_color = (TextView) convertView.findViewById(R.id.tv_color_item_value);
            myColorHolder.name_color = (TextView) convertView.findViewById(R.id.tv_color_name);
            myColorHolder.per_color = (TextView) convertView.findViewById(R.id.tv_color_per);

            convertView.setTag(myColorHolder);
        } else {
            myColorHolder = (MyColorHolder) convertView.getTag();
        }

        if (listColor.get(position).getColor_value().trim().equalsIgnoreCase("ffffff")) {
            myColorHolder.view_color.setBackgroundResource(R.drawable.view_imagedetails_color);
        } else {
            myColorHolder.view_color.setBackgroundColor(Color.parseColor("#" + listColor.get(position).getColor_value()));
        }
        myColorHolder.value_color.setText("#" + listColor.get(position).getColor_value());
        myColorHolder.name_color.setText(listColor.get(position).getColor_name());
        String per = listColor.get(position).getColor_percent().trim();
        float temp = Float.parseFloat(per) * 100;
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String text_per = df.format(temp);
        if (text_per.substring(0, 1).equals(".")) {
            text_per = "0" + text_per;
        }
        myColorHolder.per_color.setText(text_per + "%");

        return convertView;
    }

    class MyColorHolder {

        private View view_color;
        private TextView value_color;
        private TextView name_color;
        private TextView per_color;

    }

}
