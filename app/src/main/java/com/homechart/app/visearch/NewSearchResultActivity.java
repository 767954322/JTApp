package com.homechart.app.visearch;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.croplayout.EditPhotoViewMore;
import com.homechart.app.croplayout.EditableImage;
import com.homechart.app.croplayout.NewEditPhotoViewMore;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.searchfservice.SearchSBean;
import com.homechart.app.imagedetail.PhotoView;
import com.homechart.app.imagedetail.PhotoViewAttacher;
import com.homechart.app.utils.imageloader.ImageUtils;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/11/1.
 */

public class NewSearchResultActivity
        extends BaseActivity implements View.OnClickListener {

    private ImageButton mBackButton;
    private String network;
    private TextView mTital;
    private String image_id;
    private String imagePath;
    private String searchstatus;
    private SearchSBean searchSBean;
    private int widerImage;
    private int heightImage;
    private PhotoView pv_Image;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_shibie_result;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();

        Intent intent = getIntent();
        image_id = intent.getStringExtra("image_id");
        imagePath = intent.getStringExtra("imagePath");
        searchstatus = intent.getStringExtra("searchstatus");
        network = intent.getStringExtra("network");
        searchSBean = (SearchSBean) intent.getSerializableExtra("searchSBean");
    }

    @Override
    protected void initView() {

        pv_Image = (PhotoView) findViewById(R.id.pv_Image);
        mTital = (TextView) findViewById(R.id.tv_tital_comment);
        mBackButton = (ImageButton) findViewById(R.id.nav_left_imageButton);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mBackButton.setOnClickListener(this);
        pv_Image.setOnScaleChangeListener(new PhotoViewAttacher.OnScaleChangeListener() {
            @Override
            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                Log.d("test", scaleFactor + "---" + focusX + "---" + focusY);
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTital.setText("自定义识别");

        if (network.equals("true")) {
            ImageUtils.disBlackImage(imagePath, pv_Image);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                NewSearchResultActivity.this.finish();
                break;
        }
    }
}
