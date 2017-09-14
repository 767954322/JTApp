package com.homechart.app.visearch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
    private ImageView camera_album_button;
    private ImageView camera_flash_button;
    private ImageView camera_switch_button;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_photo_shibie;
    }

    @Override
    protected void initView() {
        iv_camera_shutter_button = (ImageView) findViewById(R.id.iv_camera_shutter_button);
        camera_preview = (CameraPreview) findViewById(R.id.camera_preview);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        camera_album_button = (ImageView) findViewById(R.id.camera_album_button);
        camera_flash_button = (ImageView) findViewById(R.id.camera_flash_button);
        camera_switch_button = (ImageView) findViewById(R.id.camera_switch_button);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_back.setOnClickListener(this);
        camera_album_button.setOnClickListener(this);
        iv_camera_shutter_button.setOnClickListener(this);
        camera_flash_button.setOnClickListener(this);
        camera_switch_button.setOnClickListener(this);
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
            case R.id.camera_album_button:
                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(openAlbumIntent, RESULT_LOAD_IMAGE_FROM_GALLERY);
                break;
            case R.id.camera_flash_button:
                camera_flash_button.setSelected(camera_preview.turnOnTorch());
                break;
            case R.id.camera_switch_button:
                camera_flash_button.setSelected(false);
                camera_preview.switchCamera();
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

    private static final int RESULT_LOAD_IMAGE_FROM_GALLERY = 0x00;
    /**
     * Image selection activity callback:
     *
     * get the image Uri from intent and pass to upload search params to start search
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
            Uri uri = data.getData();
            Intent intent1 = new Intent(PhotoActivity.this, EditPhotoActivity.class);
            intent1.putExtra("image_url", "uri");
            intent1.putExtra("type", "uri");
            intent1.setData(uri);
            startActivity(intent1);
            PhotoActivity.this.finish();
        }
    }

}
