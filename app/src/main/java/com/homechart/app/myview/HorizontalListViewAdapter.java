package com.homechart.app.myview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homechart.app.R;

import java.util.List;

/**
 * Created by gumenghao on 17/11/6.
 */

public class HorizontalListViewAdapter extends BaseAdapter {
    List<String> mData;
    Context mContext;
    public HorizontalListViewAdapter(Context mContext, List<String> mData) {
        this.mData = mData;
        this.mContext=mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    private ViewHolder vh = new ViewHolder();

    private static class ViewHolder {
        private TextView file;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_login, null);
//            vh.file = (TextView) convertView.findViewById(R.id.file);
//            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
}
