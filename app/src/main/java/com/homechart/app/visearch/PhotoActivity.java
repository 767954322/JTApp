package com.homechart.app.visearch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseActivity;
import com.visenze.visearch.android.model.Image;

/**
 * Created by gumenghao on 17/9/11.
 */

public class PhotoActivity
        extends BaseActivity
        implements View.OnClickListener,
        CameraPreview.ImageCapturedCallback {

    private ImageView iv_camera_shutter_button;
    private CameraPreview camera_preview;
    private ImageView iv_back;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_photo_shibie;
    }

    @Override
    protected void initView() {
        iv_camera_shutter_button = (ImageView) findViewById(R.id.iv_camera_shutter_button);
        camera_preview = (CameraPreview) findViewById(R.id.camera_preview);
        iv_back = (ImageView) findViewById(R.id.iv_back);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_back.setOnClickListener(this);
        iv_camera_shutter_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                PhotoActivity.this.finish();
                break;
            case R.id.iv_camera_shutter_button:
                camera_preview.takePhoto(this);
                break;
        }
    }

    @Override
    public void OnImageCaptured(Image image, String imagePath) {
        Intent intent1 = new Intent(PhotoActivity.this, EditPhotoActivity.class);
        intent1.putExtra("image_url", imagePath);
        intent1.putExtra("type", "location");
        startActivity(intent1);
        PhotoActivity.this.finish();
    }
}
