package com.homechart.app.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.color.ColorBean;
import com.homechart.app.home.bean.color.ColorItemBean;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.utils.UIUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/19.
 */

public class MyColorAdapter extends BaseAdapter {

    private Context mContext;
    private List<ColorItemBean> mColorlist;
    private Map<Integer, ColorItemBean> mSelectColorMap;

    public MyColorAdapter(Context mContext, List<ColorItemBean> color_list, Map<Integer, ColorItemBean> selectColorMap) {
        this.mContext = mContext;
        this.mColorlist = color_list;
        this.mSelectColorMap = selectColorMap;
    }

    @Override
    public int getCount() {
        if (null != mSelectColorMap && mSelectColorMap.size() > 0) {

            return mColorlist.size() + 1;
        } else {
            return mColorlist.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (null != mSelectColorMap && mSelectColorMap.size() > 0) {
            if (position == mColorlist.size()) {
                return mColorlist.get(mColorlist.size() - 1);
            } else {
                return mColorlist.get(position);
            }
        } else {
            return mColorlist.get(position);
        }
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pop_color, null);
            myHolder.imageView = (RoundImageView) convertView.findViewById(R.id.iv_color_pic);
            myHolder.tv_name = (TextView) convertView.findViewById(R.id.iv_color_name);
            myHolder.iv_color_select = (ImageView) convertView.findViewById(R.id.iv_color_select);
            myHolder.iv_color_pic_line = (RoundImageView) convertView.findViewById(R.id.iv_color_pic_line);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }

        if (position == mColorlist.size()) {
            myHolder.imageView.setBackgroundResource(R.drawable.color_line_white);
            myHolder.iv_color_select.setVisibility(View.VISIBLE);
            myHolder.iv_color_select.setImageResource(R.drawable.qingkong);
            myHolder.tv_name.setText("清空");
        } else {
            if (mSelectColorMap.containsKey(mColorlist.get(position).getColor_id())) {

                if (mColorlist.get(position).getColor_name().trim().equals("白色")) {
                    myHolder.iv_color_select.setImageResource(R.drawable.xuanbaise);
                }
                myHolder.iv_color_select.setVisibility(View.VISIBLE);
                myHolder.iv_color_pic_line.setVisibility(View.VISIBLE);

            } else {
                myHolder.iv_color_select.setVisibility(View.INVISIBLE);
                myHolder.iv_color_pic_line.setVisibility(View.INVISIBLE);
            }

            if (mColorlist.get(position).getColor_name().trim().equals("白色")) {
                myHolder.imageView.setBackgroundResource(R.drawable.color_line_white);
            } else {
                myHolder.imageView.setBackgroundColor(Color.parseColor("#" + mColorlist.get(position).getColor_value()));
            }
            myHolder.tv_name.setText(mColorlist.get(position).getColor_name());
        }
        return convertView;
    }

    class MyHolder {
        private RoundImageView imageView;
        private TextView tv_name;
        private ImageView iv_color_select;
        private RoundImageView iv_color_pic_line;

    }


    public void changeData(List<ColorItemBean> color_list, Map<Integer, ColorItemBean> selectColorMap) {
        this.mColorlist = color_list;
        this.mSelectColorMap = selectColorMap;
        this.notifyDataSetChanged();
    }
}
