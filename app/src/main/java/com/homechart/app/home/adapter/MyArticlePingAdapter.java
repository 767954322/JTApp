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
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.bean.articleping.PingCommentInfoBean;
import com.homechart.app.home.bean.articleping.PingCommentListItemBean;
import com.homechart.app.myview.RoundImageView;
import com.homechart.app.utils.UIUtils;
import com.homechart.app.utils.imageloader.ImageUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by gumenghao on 17/7/26.
 */

public class MyArticlePingAdapter extends BaseAdapter {

    private Context context;
    private String user_id;//作者的user_id
    private HuiFu huiFu;
    private boolean ifMore;
    private List<PingCommentListItemBean> mListPing;

    public MyArticlePingAdapter(Context context, List<PingCommentListItemBean> mListPing, String user_id ,HuiFu huiFu,boolean ifMore) {
        this.context = context;
        this.mListPing = mListPing;
        this.user_id = user_id;
        this.huiFu = huiFu;
        this.ifMore = ifMore;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

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
            myHolder.ll_zan_add = (LinearLayout) convertView.findViewById(R.id.ll_zan_add);
            myHolder.tv_time_one = (TextView) convertView.findViewById(R.id.tv_time_one);
            myHolder.tv_zan_one = (TextView) convertView.findViewById(R.id.tv_zan_one);
            myHolder.tv_huifu_content_two1 = (TextView) convertView.findViewById(R.id.tv_huifu_content_two1);
            myHolder.tv_huifu_content_four1 = (TextView) convertView.findViewById(R.id.tv_huifu_content_four1);
            myHolder.view_one = convertView.findViewById(R.id.view_one);
            myHolder.iv_image_zan = (ImageView) convertView.findViewById(R.id.iv_image_zan);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        PingCommentInfoBean pingCommentInfoBean = mListPing.get(position).getComment_info();

        myHolder.tv_name_one.setText(pingCommentInfoBean.getUser_info().getNickname());



        String time1 = pingCommentInfoBean.getAdd_time();
        String shi1 = "";
        String yue1 = "";
        String nian1 = "";
        if (!TextUtils.isEmpty(time1)) {
            shi1 = time1.substring(time1.length() - 8, time1.length() - 3);
            yue1 = time1.substring(5, 7) + "月" + time1.substring(8, 10) + "日";
            nian1 = time1.substring(0, 4);
        }
        //计算时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String strCurrent = formatter.format(curDate);
        long data = PublicUtils.diffMathDay(time1, strCurrent, "yyyy-MM-dd HH:mm:ss");

        if (data <= 7) {
            myHolder.tv_time_one.setText(yue1 + "  " + shi1);
        } else if (data > 7 && data <= 30) {
            myHolder.tv_time_one.setText("1周以前");
        } else {
            myHolder.tv_time_one.setText("1月以前");
        }

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

        myHolder.ll_huifu_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huiFu.clickHuiFu(mListPing.get(position));
            }
        });

        if(mListPing.get(position).getComment_info().getIs_liked() == 1){//已经点赞了
            myHolder.iv_image_zan.setImageResource(R.drawable.zan);
            myHolder.tv_zan_one.setTextColor(UIUtils.getColor(R.color.bg_e79056));
        }else {//未赞
            myHolder.iv_image_zan.setImageResource(R.drawable.zan1);
            myHolder.tv_zan_one.setTextColor(UIUtils.getColor(R.color.bg_8f8f8f));
        }
        myHolder.tv_zan_one.setText(mListPing.get(position).getComment_info().getLike_num());
        myHolder.ll_zan_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huiFu.clickAddZan(mListPing.get(position),position);
            }
        });
        myHolder.riv_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huiFu.clickHeader(mListPing.get(position),position);
            }
        });

        if(!ifMore && position == mListPing.size()-1){
            myHolder.view_one.setVisibility(View.GONE);
        }else {
            myHolder.view_one.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public void changeLine(boolean ifMore) {
        this.ifMore = ifMore;
    }


    class MyHolder {
        private RoundImageView riv_one;
        private RelativeLayout rl_name_one;
        private RelativeLayout rl_huifu_content;
        private TextView tv_name_one;
        private TextView tv_if_zuozhe;
        private TextView tv_content_one;
        private LinearLayout ll_huifu_one;
        private LinearLayout ll_zan_add;
        private TextView tv_time_one;
        private TextView tv_huifu_content_two1;
        private TextView tv_huifu_content_four1;
        private TextView tv_zan_one;
        private View view_one;
        private ImageView iv_image_zan;
    }

   public interface HuiFu{
       void clickHuiFu(PingCommentListItemBean pingCommentListItemBean);
       void clickAddZan(PingCommentListItemBean pingCommentListItemBean,int position);
       void clickHeader(PingCommentListItemBean pingCommentListItemBean,int position);
    }
}
