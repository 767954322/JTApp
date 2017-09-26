package com.homechart.app.visearch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.activity.FaBuActvity;
import com.homechart.app.home.activity.HomeActivity;
import com.homechart.app.home.activity.ShiBieActivity;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.utils.ToastUtils;
import com.visenze.visearch.android.model.Image;

import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by gumenghao on 17/9/11.
 */

public class PhotoActivity
        extends BaseActivity
        implements View.OnClickListener,
        CameraPreview.ImageCapturedCallback {

    private TextView iv_camera_shutter_button;
    private CameraPreview camera_preview;
    private ImageView iv_back;
    private TextView camera_album_button;
    private TextView tv_shibiejilu;
    private ImageView camera_flash_button;
    private ImageView camera_switch_button;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_photo_shibie;
    }

    @Override
    protected void initView() {
        iv_camera_shutter_button = (TextView) findViewById(R.id.iv_camera_shutter_button);
        camera_preview = (CameraPreview) findViewById(R.id.camera_preview);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        camera_album_button = (TextView) findViewById(R.id.camera_album_button);
        tv_shibiejilu = (TextView) findViewById(R.id.tv_shibiejilu);
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
        tv_shibiejilu.setOnClickListener(this);
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
//                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(openAlbumIntent, RESULT_LOAD_IMAGE_FROM_GALLERY);
                GalleryFinal.openGallerySingle(0, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        if (resultList != null && resultList.size() > 0) {
                            Message message = new Message();
                            message.obj = resultList.get(0).getPhotoPath().toString();
                            handler.sendMessage(message);
                        } else {
                            ToastUtils.showCenter(PhotoActivity.this, "图片资源获取失败");
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {
                    }
                });
                break;
            case R.id.camera_flash_button:
                camera_flash_button.setSelected(camera_preview.turnOnTorch());
                break;
            case R.id.camera_switch_button:
                camera_flash_button.setSelected(false);
                camera_preview.switchCamera();
                break;
            case R.id.tv_shibiejilu:
                Intent intent = new Intent(PhotoActivity.this,ShiBieActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void OnImageCaptured(Image image, String imagePath) {
        Intent intent1 = new Intent(PhotoActivity.this, SearchLoadingActivity.class);
        intent1.putExtra("image_url", imagePath);
        intent1.putExtra("type", "location");
        intent1.putExtra("image_type", "location");
        startActivity(intent1);
        PhotoActivity.this.finish();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String url_Imag = (String) msg.obj;
            Intent intent1 = new Intent(PhotoActivity.this, SearchLoadingActivity.class);
            intent1.putExtra("image_url", url_Imag);
            intent1.putExtra("type", "location");
            intent1.putExtra("image_type", "location");
            startActivity(intent1);
            PhotoActivity.this.finish();
        }
    };

}
