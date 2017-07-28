package com.homechart.app.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.articledetails.ArticleBean;
import com.homechart.app.home.bean.articledetails.ItemDetailsBean;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;

import java.util.List;

/**
 * Created by gumenghao on 17/7/26.
 */

public class MyArticlePicAdapter extends BaseAdapter {

    private ArticleBean mArticleBean;
    private Context mContext;
    private List<ItemDetailsBean> mList;
    private int wid_screen;
    private PicDetails picDetails;


    public MyArticlePicAdapter(Context context, ArticleBean articleBean, int wid_screen,PicDetails picDetails) {
        this.mArticleBean = articleBean;
        this.mContext = context;
        this.wid_screen = wid_screen;
        this.picDetails = picDetails;
        mList = mArticleBean.getItem_list();
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

        MyHolder myHolder;

        if (convertView == null) {
            myHolder = new MyHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_article_details_piccontent, null);
            myHolder.iv_item_article_pic = (ImageView) convertView.findViewById(R.id.iv_item_article_pic);
            myHolder.tv_item_article_content = (TextView) convertView.findViewById(R.id.tv_item_article_content);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }

        String pic_content = mList.get(position).getItem_info().getDescription();
        String pic_image = mList.get(position).getItem_info().getImage().getImg0();
        float ratio = mList.get(position).getItem_info().getImage().getRatio();
        if (TextUtils.isEmpty(pic_content)) {
            myHolder.tv_item_article_content.setVisibility(View.GONE);
        } else {
            myHolder.tv_item_article_content.setVisibility(View.VISIBLE);
            myHolder.tv_item_article_content.setText(pic_content);
        }

        if (TextUtils.isEmpty(pic_image)) {
            myHolder.iv_item_article_pic.setVisibility(View.GONE);
        } else {
            myHolder.iv_item_article_pic.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = myHolder.iv_item_article_pic.getLayoutParams();
            layoutParams.height = (int) ((wid_screen - UIUtils.getDimens(R.dimen.font_40)) / ratio);
            layoutParams.width = wid_screen - UIUtils.getDimens(R.dimen.font_40);
            myHolder.iv_item_article_pic.setLayoutParams(layoutParams);
            ImageUtils.disBlackImage(pic_image, myHolder.iv_item_article_pic);
        }

        myHolder.iv_item_article_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picDetails.clickPic(position);
            }
        });
        return convertView;
    }

    class MyHolder {

        private ImageView iv_item_article_pic;
        private TextView tv_item_article_content;

    }
    public interface PicDetails{
         void clickPic(int position);
    }

}
