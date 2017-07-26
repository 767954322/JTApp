package com.homechart.app.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.articledetails.ArticleBean;
import com.homechart.app.home.bean.articledetails.ItemDetailsBean;
import com.homechart.app.home.bean.articleping.PingCommentInfoBean;
import com.homechart.app.home.bean.articleping.PingCommentListItemBean;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;

import java.util.List;

/**
 * Created by gumenghao on 17/7/26.
 */

public class MyArticlePingAdapter extends BaseAdapter {

    private Context context;
    private String user_id;//作者的user_id
    private List<PingCommentListItemBean> mListPing;

    public MyArticlePingAdapter(Context context, List<PingCommentListItemBean> mListPing, String user_id) {
        this.context = context;
        this.mListPing = mListPing;
        this.user_id = user_id;
    }

    @Override
    public int getCount() {
        return mListPing.size();
    }

    @Override
    public Object getItem(int position) {
        return mListPing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyHolder myHolder;
        if (convertView == null) {
            myHolder = new MyHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pingarticle_activity, null);
            myHolder.riv_one = (RoundImageView) convertView.findViewById(R.id.riv_one);
            myHolder.rl_name_one = (RelativeLayout) convertView.findViewById(R.id.rl_name_one);
            myHolder.rl_huifu_content = (RelativeLayout) convertView.findViewById(R.id.rl_huifu_content);
            myHolder.tv_name_one = (TextView) convertView.findViewById(R.id.tv_name_one);
            myHolder.tv_if_zuozhe = (TextView) convertView.findViewById(R.id.tv_if_zuozhe);
            myHolder.tv_content_one = (TextView) convertView.findViewById(R.id.tv_content_one);
            myHolder.ll_huifu_one = (LinearLayout) convertView.findViewById(R.id.ll_huifu_one);
            myHolder.tv_time_one = (TextView) convertView.findViewById(R.id.tv_time_one);
            myHolder.tv_huifu_content_two1 = (TextView) convertView.findViewById(R.id.tv_huifu_content_two1);
            myHolder.tv_huifu_content_four1 = (TextView) convertView.findViewById(R.id.tv_huifu_content_four1);
            myHolder.view_one = convertView.findViewById(R.id.view_one);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        PingCommentInfoBean pingCommentInfoBean = mListPing.get(position).getComment_info();

        myHolder.tv_name_one.setText(pingCommentInfoBean.getUser_info().getNickname());
        myHolder.tv_time_one.setText(pingCommentInfoBean.getAdd_time());
        myHolder.tv_content_one.setText(pingCommentInfoBean.getContent());
        ImageUtils.displayRoundImage(pingCommentInfoBean.getUser_info().getAvatar().getThumb(), myHolder.riv_one);

        if (pingCommentInfoBean.getReply_comment() == null) {
            myHolder.rl_huifu_content.setVisibility(View.GONE);
        } else {
            myHolder.rl_huifu_content.setVisibility(View.VISIBLE);
            myHolder.tv_huifu_content_two1.setText(pingCommentInfoBean.getReply_comment().getUser_info().getNickname());
            myHolder.tv_huifu_content_four1.setText(pingCommentInfoBean.getReply_comment().getContent());
        }
        if (user_id.equals(pingCommentInfoBean.getUser_info().getUser_id())) {
            myHolder.tv_if_zuozhe.setVisibility(View.VISIBLE);
        } else {
            myHolder.tv_if_zuozhe.setVisibility(View.GONE);
        }
        return convertView;
    }

    class MyHolder {
        private RoundImageView riv_one;
        private RelativeLayout rl_name_one;
        private RelativeLayout rl_huifu_content;
        private TextView tv_name_one;
        private TextView tv_if_zuozhe;
        private TextView tv_content_one;
        private LinearLayout ll_huifu_one;
        private TextView tv_time_one;
        private TextView tv_huifu_content_two1;
        private TextView tv_huifu_content_four1;
        private View view_one;
    }
}
