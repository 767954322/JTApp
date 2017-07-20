package com.homechart.app.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.fabu.ActivityItemDataBean;

import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/21.
 */

public class MyActivitysListAdapter extends BaseAdapter {

    private List<ActivityItemDataBean> activityList;
    private Context context;
    private CheckStatus mCheckStatus;
    private Map<Integer, String> mMap;


    public void notifyData(Map<Integer, String> map) {
        this.mMap = map;
        notifyDataSetChanged();
    }

    public MyActivitysListAdapter(List<ActivityItemDataBean> activityList, Context context, CheckStatus checkStatus, Map<Integer, String> map) {
        this.activityList = activityList;
        this.context = context;
        this.mCheckStatus = checkStatus;
        this.mMap = map;
    }

    @Override
    public int getCount() {
        return activityList.size();
    }

    @Override
    public Object getItem(int position) {
        return activityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final MyHolder myHolder;
        if (convertView == null) {
            myHolder = new MyHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_activitys_fabu, null);
            myHolder.cb_check_add = (CheckBox) convertView.findViewById(R.id.cb_check_add);
            myHolder.tv_tital = (TextView) convertView.findViewById(R.id.tv_activity_tital);
            myHolder.tv_activity_add = (TextView) convertView.findViewById(R.id.tv_activity_add);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }

        if (mMap.containsKey(position) || mMap.containsValue(activityList.get(position).getActivity_info().getActivity_id())) {
            myHolder.cb_check_add.setChecked(true);
        } else {
            myHolder.cb_check_add.setChecked(false);
        }
        String str_Tital = activityList.get(position).getActivity_info().getTitle();
        if (str_Tital.length() > 8) {
            str_Tital = str_Tital.substring(0, 8) + "...";
        }
        myHolder.tv_tital.setText(str_Tital);
        myHolder.tv_tital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myHolder.cb_check_add.isChecked()) {
                    mCheckStatus.checkChange(position, false, activityList.get(position).getActivity_info().getActivity_id());
                    myHolder.cb_check_add.setChecked(false);
                } else {
                    mCheckStatus.checkChange(position, true, activityList.get(position).getActivity_info().getActivity_id());
                    myHolder.cb_check_add.setChecked(true);
                }
            }
        });

        myHolder.cb_check_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myHolder.cb_check_add.isChecked()) {

                    mCheckStatus.checkChange(position, true, activityList.get(position).getActivity_info().getActivity_id());
                    myHolder.cb_check_add.setChecked(true);
                } else {

                    mCheckStatus.checkChange(position, false, activityList.get(position).getActivity_info().getActivity_id());
                    myHolder.cb_check_add.setChecked(false);
                }
            }
        });
        myHolder.tv_activity_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCheckStatus.activityDetail(position, activityList.get(position).getActivity_info().getActivity_id());
            }
        });
        return convertView;
    }

    class MyHolder {

        private CheckBox cb_check_add;
        private TextView tv_tital;
        private TextView tv_activity_add;

    }

    public interface CheckStatus {

        void checkChange(int position, boolean status, String activityId);

        void activityDetail(int position, String activityId);

    }

}
