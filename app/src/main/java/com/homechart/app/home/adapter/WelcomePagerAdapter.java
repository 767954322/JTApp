package com.homechart.app.home.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.TextViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.activity.HomeActivity;
import com.homechart.app.home.activity.LoginActivity;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;

/**
 * @author allen .
 * @version v1.0 .
 * @date 2017-2-24.
 * @file WelcomePagerAdapter.java .
 * @brief 首次进入滑动页Adapter .
 */
public class WelcomePagerAdapter extends PagerAdapter {
    public List<Integer> comm_data_ls;
    private final Activity context;
    private View itemView;
    public OnClickJump onClickJump;

    public WelcomePagerAdapter(Activity context, List<Integer> comm_data_ls, OnClickJump onClickJump) {
        this.context = context;
        this.comm_data_ls = comm_data_ls;
        this.onClickJump = onClickJump;
    }

    @Override
    public int getCount() {
        return comm_data_ls.size();
    }

    public Object instantiateItem(ViewGroup container, final int position) {
        itemView = View.inflate(context, R.layout.item_viewpage_content, null);
        ImageView imageView = ((ImageView) itemView.findViewById(R.id.image));
        TextView tv_tiaoguo = ((TextView) itemView.findViewById(R.id.tv_tiaoguo));

        if (position == 0) {
            tv_tiaoguo.setVisibility(View.VISIBLE);
            tv_tiaoguo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickJump.onClickJump();
                }
            });
        } else {
            tv_tiaoguo.setVisibility(View.GONE);
        }

        imageView.setBackgroundResource(comm_data_ls.get(position));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 2) {
                    onClickJump.onClickJump();
                }
            }
        });
        container.removeView(itemView);
        container.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
//        container.removeView(itemView);
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == (arg1);
    }

    public interface OnClickJump {
        public void onClickJump();
    }
}
