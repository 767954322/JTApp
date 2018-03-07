package com.homechart.app.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.activity.NewTagsListActivity;
import com.homechart.app.home.bean.faxiantags.TagListItemBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gumenghao on 18/3/7.
 */

public class MyTagsAdapter extends BaseAdapter {

    private int mPosition;
    private List<TagListItemBean> mList;
    private Context context;
    private ClickItemIns clickItemIns;

    public MyTagsAdapter(List<TagListItemBean> mList, Context context, ClickItemIns clickItemIns, int position) {
        this.mList = mList;
        this.context = context;
        this.clickItemIns = clickItemIns;
        this.mPosition = position;
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

        MyHolder myHolder ;
        if(convertView == null){
            myHolder = new MyHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mytag,null);
            myHolder.tv_dingyue_tag = (TextView) convertView.findViewById(R.id.tv_dingyue_tag);
            convertView.setTag(myHolder);
        }else {
            myHolder = (MyHolder) convertView.getTag();
        }

        myHolder.tv_dingyue_tag.setText(mList.get(position).getTag_info().getTag_name());

        myHolder.tv_dingyue_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickItemIns.clickItemPosition(position,mPosition);
            }
        });
        return convertView;
    }

    class MyHolder {

        public TextView tv_dingyue_tag ;

    }
    public interface ClickItemIns {
        void clickItemPosition(int position, int numTag);
    }
}
