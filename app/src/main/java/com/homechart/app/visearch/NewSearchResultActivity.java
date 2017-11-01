package com.homechart.app.visearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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
        extends BaseActivity implements View.OnClickListener {

    private ImageButton mBackButton;
    private String network;
    private TextView mTital;
    private String image_id;
    private String imagePath;
    private String searchstatus;
    private SearchSBean searchSBean;
    private NewEditPhotoViewMore mPhotoImage;
    private EditableImage editableImage;

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

        if(network.equals("true")){
            editableImage = new EditableImage(imagePath, true);
            mPhotoImage.initView(NewSearchResultActivity.this, editableImage, true);
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
