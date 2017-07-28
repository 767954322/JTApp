package com.homechart.app.imagedetail;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.utils.imageloader.ImageUtils;
import java.util.ArrayList;
import java.util.List;


public class ImageDetailsActivity extends BaseActivity implements View.OnClickListener {
    private HackyViewPager mViewPager;
    private List<String> imageLists;
    private int intExtra;
    private ArrayList<String> mImageUrl = new ArrayList<>();
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_case_detail_pic;
    }

    @Override
    protected void initView() {
        mViewPager = (HackyViewPager) findViewById(R.id.case_librafy_detail_activity_vp);
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                finish();
                break;
        }
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        imageLists = (List<String>) getIntent().getSerializableExtra("pic_url_list");
        intExtra = getIntent().getIntExtra("click_position", 0);//获得点击的位置
    }

    int load_position = 0;

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
        load_position = intExtra;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                intExtra = position;
                load_position = position;
                tv_tital_comment.setText((position + 1) + "/" + imageLists.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected void initData(Bundle savedInstanceState) {
        updateViewFromData();
        if (imageLists != null) {
            mViewPager.setAdapter(new SamplePagerAdapter(mImageUrl));
            mViewPager.setCurrentItem(intExtra);
            tv_tital_comment.setText((intExtra + 1) + "/" + imageLists.size());
        }
    }

    /**
     * 获取所有图片的url地址
     * <p>
     * TODO  BUG
     */

    private void updateViewFromData() {
        if (imageLists != null) {
            mImageUrl.addAll(imageLists);
        }
    }


    class SamplePagerAdapter extends PagerAdapter {

        private ArrayList<String> mImageUrl1;

        public SamplePagerAdapter(ArrayList<String> mImageUrl1) {
            this.mImageUrl1 = mImageUrl1;
        }

        @Override
        public int getCount() {
            return mImageUrl1.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            ImageUtils.disBlackImage(mImageUrl1.get(position), photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    ImageDetailsActivity.this.finish();
                }
            });
            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return false;
                }
            });
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
