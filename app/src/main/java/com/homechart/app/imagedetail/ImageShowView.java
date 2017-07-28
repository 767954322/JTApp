package com.homechart.app.imagedetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.homechart.app.R;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;

/**
 * @author Malidong .
 * @version v1.0 .
 * @date 2016-6-13 .
 * @file ImageShowView.java .
 * @brief .
 */
public class ImageShowView extends LinearLayout {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 图片轮播视图
     */
    private ViewPager mAdvPager = null;
    /**
     * 滚动图片视图适配
     */
    private ImageCycleAdapter mAdvAdapter;
    /**
     * 图片轮播指示个图
     */
    private ImageView mImageView = null;

    /**
     * 滚动图片指示视图列表
     */
    private ImageView[] mImageViews = null;

    /**
     * @param context
     */
    public ImageShowView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    @SuppressLint("Recycle")
    public ImageShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_show_imageview, this);
        mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
    }

    /**
     * 装填图片数据
     *
     * @param imageUrlList
     * @param imageShowViewListener
     */
    public void setImageResources(ArrayList<String> imageUrlList,
                                  ImageShowViewListener imageShowViewListener, int position) {
        // 图片广告数量
        int imageCount = imageUrlList.size();
        imageCount = (imageCount > 0) ? imageCount : 1;
        mImageViews = new ImageView[imageCount];
        for (int i = 0; i < imageCount; i++) {
            mImageView = new ImageView(mContext);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            params.leftMargin = 30;
            mImageView.setScaleType(ScaleType.CENTER_CROP);
            mImageView.setLayoutParams(params);
            mImageViews[i] = mImageView;
        }

        mAdvAdapter = new ImageCycleAdapter(mContext, imageUrlList,
                imageShowViewListener);
        mAdvPager.setAdapter(mAdvAdapter);
        mAdvPager.setCurrentItem(position);
    }

    private class ImageCycleAdapter extends PagerAdapter {

        /**
         * 图片视图缓存列表
         */
        private ArrayList<SmartImageView> mImageViewCacheList;

        /**
         * 图片资源列表
         */
        private ArrayList<String> mAdList = new ArrayList<String>();

        /**
         * 广告图片点击监听
         */
        private ImageShowViewListener mImageShowViewListener;

        private Context mContext;

        public ImageCycleAdapter(Context context, ArrayList<String> adList,
                                 ImageShowViewListener imageShowViewListener) {
            this.mContext = context;
            this.mAdList = adList;
            mImageShowViewListener = imageShowViewListener;
            mImageViewCacheList = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return mImageViews.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            SmartImageView imageView = null;
            if (mImageViewCacheList.isEmpty()) {
                imageView = new SmartImageView(mContext);
                imageView.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                // test
                imageView.setScaleType(ScaleType.CENTER_CROP);
                // 设置图片点击监听
//                imageView.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
                imageView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mImageShowViewListener.onImageClick(position, v);
                        return false;
                    }
                });
            } else {
                imageView = mImageViewCacheList.remove(0);
            }
            String imageUrl = (mAdList != null && mAdList.size() > 0) ? mAdList.get(position) : "";
            imageView.setTag(imageUrl);
            container.addView(imageView);
            ImageUtils.disRectangleImage(imageUrl, imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            SmartImageView view = (SmartImageView) object;
            mAdvPager.removeView(view);
            mImageViewCacheList.add(view);
        }
    }

    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    public interface ImageShowViewListener {

        /**
         * 单击图片事件
         *
         * @param position
         * @param imageView
         */
        void onImageClick(int position, View imageView);
    }
}
