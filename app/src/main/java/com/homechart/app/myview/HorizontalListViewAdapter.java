package com.homechart.app.myview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.newhistory.HistoryDataBean;
import com.homechart.app.utils.imageloader.ImageUtils;

import java.util.List;

/**
 * Created by gumenghao on 17/11/6.
 */

public class HorizontalListViewAdapter extends BaseAdapter {
    List<HistoryDataBean> mData;
    Context mContext;
    public HorizontalListViewAdapter(Context mContext, List<HistoryDataBean> mData) {
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
        private ImageView iv_item_image;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_scroll_history, null);
            vh.iv_item_image = (ImageView) convertView.findViewById(R.id.iv_item_image);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        ImageUtils.disRectangleImage(mData.get(position).getImage_url(),vh.iv_item_image);
        return convertView;
    }
}
