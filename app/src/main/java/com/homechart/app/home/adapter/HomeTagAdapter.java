package com.homechart.app.home.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.color.ColorBean;
import com.homechart.app.home.bean.color.ColorItemBean;
import com.homechart.app.home.bean.pictag.TagItemDataBean;
import com.homechart.app.home.bean.pictag.TagItemDataChildBean;
import com.homechart.app.myview.NoScrollGridView;
import com.homechart.app.utils.ToastUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by gumenghao on 17/6/15.
 */

public class HomeTagAdapter extends PagerAdapter {

    private View itemView;
    private Context mContext;
    private ColorBean mColorBean;
    private List<TagItemDataBean> mTagList;
    private PopupWindowCallBack mPopupWindowCallBack;
    private MyTagGridAdapter mAdapter;
    private Map<Integer, ColorItemBean> mSelectListData;

    public HomeTagAdapter(Context mContext,
                          List<TagItemDataBean> mTagList,
                          PopupWindowCallBack popupWindowCallBack,
                          ColorBean colorBean ,
                          Map<Integer, ColorItemBean> selectListData) {
        this.mContext = mContext;
        this.mSelectListData = selectListData;
        this.mTagList = mTagList;
        this.mColorBean = colorBean;
        this.mPopupWindowCallBack = popupWindowCallBack;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
    }

    @Override
    public int getCount() {
        return mTagList.size()+1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if(position != 4){
            itemView = View.inflate(mContext, R.layout.viewpager_tag_page, null);
            container.removeView(itemView);
            container.addView(itemView);
            GridView gv_pager_gridview = (GridView) itemView.findViewById(R.id.gv_pager_gridview);
            View view_tab_bottom = itemView.findViewById(R.id.view_tab_bottom);
            view_tab_bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindowCallBack.onDismiss();
                }
            });
            mAdapter = new MyTagGridAdapter(mTagList.get(position).getChildren(),position);
            gv_pager_gridview.setAdapter(mAdapter);
            return itemView;
        }else {
            itemView = View.inflate(mContext, R.layout.viewpager_tag_color_page, null);
            container.removeView(itemView);
            container.addView(itemView);

            GridView gv_color_gridview = (GridView) itemView.findViewById(R.id.gv_color_gridview);
            View view_tab_bottom = itemView.findViewById(R.id.view_tab_bottom);
            view_tab_bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindowCallBack.onDismiss();
                }
            });

            if (mColorBean != null) {
                final List<ColorItemBean> mListData = mColorBean.getColor_list();
                MyHomeColorAdapter colorAdapter = new MyHomeColorAdapter(mContext, mListData, mSelectListData);
                gv_color_gridview.setAdapter(colorAdapter);
                gv_color_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if(position == mListData.size()){
                            mPopupWindowCallBack.onClearColor();
                        }else {
                            mPopupWindowCallBack.onItemColorClick(mColorBean.getColor_list().get(position));
                        }
                   }
                });
            }
            return itemView;
        }

    }


   public void changeData(Map<Integer, ColorItemBean> selectListData){
        this.mSelectListData = selectListData;
       this.notifyDataSetChanged();
    }

    class MyTagGridAdapter extends BaseAdapter {

        private List<TagItemDataChildBean> mList_child;
        private int mPosition;

        public MyTagGridAdapter(List<TagItemDataChildBean> mList_child, int position) {
            this.mList_child = mList_child;
            this.mPosition = position;
        }

        @Override
        public int getCount() {
            return mList_child.size();
        }

        @Override
        public Object getItem(int position) {
            return mList_child.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            MyHolder myHolder;
            if (convertView == null) {
                myHolder = new MyHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.viewpager_tag_page_item, null);
                myHolder.tv_tag_page_item = (TextView) convertView.findViewById(R.id.tv_tag_page_item);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            myHolder.tv_tag_page_item.setText(mList_child.get(position).getTag_name());
            myHolder.tv_tag_page_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindowCallBack.onItemClick(mList_child.get(position).getTag_name(),mPosition);
                }
            });
            return convertView;
        }

        class MyHolder {

            private TextView tv_tag_page_item;

        }
    }

    public interface PopupWindowCallBack {
        void onDismiss();

        void onItemClick(String tagStr,int mPosition);

        void onItemColorClick(ColorItemBean colorItemBean);

        void onClearColor();
    }

}
