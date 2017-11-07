package com.homechart.app.visearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.croplayout.EditPhotoViewMore;
import com.homechart.app.croplayout.EditableImage;
import com.homechart.app.croplayout.NewEditPhotoViewMore;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.searchfservice.SearchSBean;

import java.io.Serializable;

/**
 * Created by gumenghao on 17/11/1.
 */

public class NewSearchResultActivity
        extends BaseActivity implements View.OnClickListener, NewEditPhotoViewMore.LayoutSize {

    private ImageButton mBackButton;
    private String network;
    private TextView mTital;
    private String image_id;
    private String imagePath;
    private String searchstatus;
    private SearchSBean searchSBean;
    private NewEditPhotoViewMore mPhotoImage;
    private EditableImage editableImage;
    private RelativeLayout fl_image;
    private int widerImage;
    private int heightImage;
    private RelativeLayout rly_point;

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

        fl_image = (RelativeLayout) findViewById(R.id.fl_image);
        rly_point = (RelativeLayout) findViewById(R.id.rly_point);
        mTital = (TextView) findViewById(R.id.tv_tital_comment);
        mBackButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mPhotoImage = (NewEditPhotoViewMore) findViewById(R.id.photoedit_image_view);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mBackButton.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mTital.setText("自定义识别");

        if (network.equals("true")) {
            editableImage = new EditableImage(imagePath, true);
        } else {
            editableImage = new EditableImage(imagePath);
            mPhotoImage.setUrlImage("file://" + imagePath, true);
        }

        mPhotoImage.initView(NewSearchResultActivity.this, editableImage, false, this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                NewSearchResultActivity.this.finish();
                break;
        }
    }

    @Override
    public void setLayoutSize(int mH, int mW) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mW, mH);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        rly_point.setLayoutParams(layoutParams);
    }
}
