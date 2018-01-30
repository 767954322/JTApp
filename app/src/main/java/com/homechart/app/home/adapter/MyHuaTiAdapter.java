package com.homechart.app.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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

    public MyHuaTiAdapter(List<RecommendItemDataBean> list, Context context) {
        this.list = list;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder myHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_huati, null);
            myHolder = new MyHolder();
            myHolder.iv_imageview_one = (ImageView) convertView.findViewById(R.id.iv_imageview_one);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) myHolder.iv_imageview_one.getLayoutParams();
        layoutParams.height = (int) ((PublicUtils.getScreenWidth(context) - UIUtils.getDimens(R.dimen.font_20)) / list.get(position).getRecommend_info().getRatio());
        ImageUtils.displayFilletImage(list.get(position).getRecommend_info().getImage_url(), myHolder.iv_imageview_one);

        return convertView;
    }

    class MyHolder {
        private ImageView iv_imageview_one;
    }

   public void dataChange(List<RecommendItemDataBean> list1){
        this.list = list1;
        notifyDataSetChanged();
    }

}
