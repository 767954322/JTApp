package com.homechart.app.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.bean.search.RecommendItemDataBean;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;

import java.util.List;

/**
 * Created by gumenghao on 18/1/30.
 */

public class MyHuaTiAdapter extends BaseAdapter {

    private List<RecommendItemDataBean> list;
    private Context context;
    private ClickZhuTi mClickZhuTi;

    public MyHuaTiAdapter(List<RecommendItemDataBean> list, Context context, ClickZhuTi clickZhuTi) {
        this.list = list;
        this.context = context;
        this.mClickZhuTi = clickZhuTi;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyHolder myHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_huati, null);
            myHolder = new MyHolder();
            myHolder.iv_imageview_one = (ImageView) convertView.findViewById(R.id.iv_imageview_one);
            myHolder.tv_name_pic = (TextView) convertView.findViewById(R.id.tv_name_pic);
            myHolder.rl_waicheng = (RelativeLayout) convertView.findViewById(R.id.rl_waicheng);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) myHolder.iv_imageview_one.getLayoutParams();
        layoutParams.height = (int) ((PublicUtils.getScreenWidth(context) - UIUtils.getDimens(R.dimen.font_20)) / list.get(position).getRecommend_info().getRatio());
        ImageUtils.displayFilletImage(list.get(position).getRecommend_info().getImage_url(), myHolder.iv_imageview_one);

        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) myHolder.iv_imageview_one.getLayoutParams();
        layoutParams1.height = (int) ((PublicUtils.getScreenWidth(context) - UIUtils.getDimens(R.dimen.font_20)) / list.get(position).getRecommend_info().getRatio());
        myHolder.rl_waicheng.setLayoutParams(layoutParams1);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) myHolder.tv_name_pic.getLayoutParams();
        layoutParams2.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        myHolder.tv_name_pic.setLayoutParams(layoutParams2);
        myHolder.tv_name_pic.setText(list.get(position).getRecommend_info().getTitle());

        myHolder.iv_imageview_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickZhuTi.clickZhuTi(position);
            }
        });
        return convertView;
    }

    class MyHolder {
        private ImageView iv_imageview_one;
        private TextView tv_name_pic;
        private RelativeLayout rl_waicheng;
    }

    public void dataChange(List<RecommendItemDataBean> list1) {
        this.list = list1;
        notifyDataSetChanged();
    }

    public interface ClickZhuTi {
        void clickZhuTi(int position);
    }

}
