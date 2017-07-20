package com.homechart.app.utils.alertview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homechart.app.R;

import java.util.List;

/**
 * @author he.liu .
 * @version v1.0 .
 * @date 2016-6-20 .
 * @file AlertViewAdapter.java .
 * @brief AlertView适配器 .
 */
public class AlertViewAdapter extends BaseAdapter {
    private List<String> mDatas;
    private List<String> mDestructive;

    public AlertViewAdapter(List<String> datas, List<String> destructive) {
        this.mDatas = datas;
        this.mDestructive = destructive;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String data = mDatas.get(position);
        Holder holder = null;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.item_alertbutton, null);
            holder = creatHolder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.UpdateUI(parent.getContext(), data, position);
        return view;
    }

    public Holder creatHolder(View view) {
        return new Holder(view);
    }

    class Holder {
        private TextView tvAlert;

        public Holder(View view) {
            tvAlert = (TextView) view.findViewById(R.id.tvAlert);
        }

        public void UpdateUI(Context context, String data, int position) {
            tvAlert.setText(data);
            if (mDestructive != null && mDestructive.contains(data)) {
                tvAlert.setTextColor(context.getResources().getColor(R.color.bg_ff3b30));
            } else {
                tvAlert.setTextColor(context.getResources().getColor(R.color.bg_007aff));
            }
        }
    }
}