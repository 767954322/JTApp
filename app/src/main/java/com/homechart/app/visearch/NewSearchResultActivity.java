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
import com.homechart.app.hotposition.NewImageLayout;
import com.homechart.app.hotposition.PointSimple;
import com.homechart.app.hotposition.PositionClickImp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gumenghao on 17/11/1.
 */

public class NewSearchResultActivity
        extends BaseActivity
        implements View.OnClickListener,
        NewEditPhotoViewMore.LayoutSize,
        PositionClickImp {

    private ImageButton mBackButton;
    private String network;
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
    private NewImageLayout il_points;
    private TextView tv_fuwei;
    private TextView tv_sousuo;

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
        mBackButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        mPhotoImage = (NewEditPhotoViewMore) findViewById(R.id.photoedit_image_view);
        il_points = (NewImageLayout) findViewById(R.id.il_points);
        tv_fuwei = (TextView) findViewById(R.id.tv_fuwei);
        tv_sousuo = (TextView) findViewById(R.id.tv_sousuo);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mBackButton.setOnClickListener(this);
        tv_fuwei.setOnClickListener(this);
        tv_sousuo.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

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
            case R.id.tv_fuwei:
                rly_point.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void setLayoutSize(int mH, int mW) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mW, mH);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        rly_point.setLayoutParams(layoutParams);

        ArrayList<PointSimple> pointSimples = new ArrayList<>();
        for (int i = 0; i < searchSBean.getObject_list().size(); i++) {
            PointSimple pointSimple = new PointSimple();
            float width = searchSBean.getObject_list().get(i).getObject_info().getX();
            float height = searchSBean.getObject_list().get(i).getObject_info().getY();
            pointSimple.width_scale = width;
            pointSimple.height_scale = height;
            pointSimple.width_object = searchSBean.getObject_list().get(i).getObject_info().getWidth();
            pointSimple.height_object = searchSBean.getObject_list().get(i).getObject_info().getHeight();
            pointSimples.add(pointSimple);
        }
        il_points.setPoints(pointSimples);
        il_points.setImgBg(mW, mH, this);

    }

    @Override
    public void onClickPosition(int pos) {
        rly_point.setVisibility(View.GONE);


    }
}
