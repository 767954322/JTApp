package com.homechart.app.imagedetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.UrlConstants;
import com.homechart.app.home.activity.CaiJiImgListActivity;
import com.homechart.app.home.activity.FaBuActvity;
import com.homechart.app.home.activity.FaBuImageActivity;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.utils.BitmapUtil;
import com.homechart.app.utils.CustomProgress;
import com.homechart.app.utils.Md5Util;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.imageloader.ImageUtils;
import com.homechart.app.utils.volley.FileHttpManager;
import com.homechart.app.utils.volley.MyHttpManager;
import com.homechart.app.utils.volley.OkStringRequest;
import com.homechart.app.utils.volley.PutFileCallBack;
import com.homechart.app.utils.widget.CustomProgressTouMing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class ImageBenDiActivity
        extends BaseActivity
        implements View.OnClickListener ,
        PutFileCallBack {
    private HackyViewPager mViewPager;
    private List<String> imageLists;
    private int intExtra;
    private ArrayList<String> mImageUrl = new ArrayList<>();
    private int ifhinttital;
    private boolean ifCancle =  false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_detail_pic_bendi;
    }

    @Override
    protected void initView() {
        mViewPager = (HackyViewPager) findViewById(R.id.case_librafy_detail_activity_vp);
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
        ifhinttital = getIntent().getIntExtra("ifhinttital", 0);//获得点击的位置
    }

    int load_position = 0;

    @Override
    protected void initListener() {
        super.initListener();
        load_position = intExtra;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                intExtra = position;
                load_position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected void initData(Bundle savedInstanceState) {
        CustomProgressTouMing.show(ImageBenDiActivity.this, "图片上传中...", false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ifCancle = true;
                ImageBenDiActivity.this.finish();
            }
        });

        updateViewFromData();
        upLoaderImage();
        if (imageLists != null) {
            mViewPager.setAdapter(new SamplePagerAdapter(mImageUrl));
            mViewPager.setCurrentItem(intExtra);
        }
    }

    private void upLoaderImage() {

        new Thread() {
            @Override
            public void run() {
                super.run();
                SimpleDateFormat timesdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String fileName = timesdf.format(new Date()).toString();//获取系统时间
                //压缩图片
                Bitmap bitmap_before = BitmapUtil.getBitmap(imageLists.get(0));
//                Bitmap bitmap_compress = BitmapUtil.ratio(bitmap_before, 400, 800);
//                Bitmap bitmap_compress_press = BitmapUtil.compressImage(bitmap_before);
                if(bitmap_before != null){
                    try {
                        boolean status = BitmapUtil.saveBitmap(bitmap_before, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName + "/");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Map<String, String> map = PublicUtils.getPublicMap(MyApplication.getInstance());
                    String signString = PublicUtils.getSinaString(map);
                    String tabMd5String = Md5Util.getMD5twoTimes(signString);
                    map.put(ClassConstant.PublicKey.SIGN, tabMd5String);
                    FileHttpManager.getInstance().uploadFile(ImageBenDiActivity.this, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName + "/"),
                            UrlConstants.PUT_IMAGE,
                            map,
                            PublicUtils.getPublicHeader(MyApplication.getInstance()));
                }else {
                    CustomProgressTouMing.cancelDialog();
                    ToastUtils.showCenter(ImageBenDiActivity.this,"上传图片失败，请重新上传图片");
                }
            }
        }.start();

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

    @Override
    public void onSucces(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            String image_id = jsonObject1.getString("immage_id");
            getDefaultTag(image_id, "");
        } catch (JSONException e) {
            e.printStackTrace();
            CustomProgressTouMing.cancelDialog();
            ToastUtils.showCenter(ImageBenDiActivity.this,"上传图片失败，请重新上传图片");
            ImageBenDiActivity.this.finish();
        }
    }

    @Override
    public void onFails() {
        CustomProgressTouMing.cancelDialog();
        ToastUtils.showCenter(ImageBenDiActivity.this,"上传图片失败，请重新上传图片");
        ImageBenDiActivity.this.finish();
    }
    private void getDefaultTag(final String image_id, final String url) {
        OkStringRequest.OKResponseCallback callBack = new OkStringRequest.OKResponseCallback() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgressTouMing.cancelDialog();
                ToastUtils.showCenter(ImageBenDiActivity.this, "默认标签获取失败");
                ImageBenDiActivity.this.finish();
            }

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error_code = jsonObject.getInt(ClassConstant.Parame.ERROR_CODE);
                    String error_msg = jsonObject.getString(ClassConstant.Parame.ERROR_MSG);
                    String data_msg = jsonObject.getString(ClassConstant.Parame.DATA);
                    if (error_code == 0) {
                        List<String> list = new ArrayList<>();
                        JSONObject jsonObject1 = new JSONObject(data_msg);
                        JSONArray jsonArray = jsonObject1.getJSONArray(image_id);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String tags = jsonArray.get(i).toString();
                                list.add(tags);
                            }
                        }
                        Intent intent = new Intent(ImageBenDiActivity.this, FaBuImageActivity.class);
                        intent.putExtra("image_id", image_id);
                        intent.putExtra("image_url", imageLists.get(0));
                        intent.putExtra("tags", (Serializable) list);
                        intent.putExtra("webUrl", "");
                        intent.putExtra("type", "location");
                        if(!ifCancle){
                            CustomProgressTouMing.cancelDialog();
                            startActivity(intent);
                        }
                        ImageBenDiActivity.this.finish();
                    } else {
                        CustomProgressTouMing.cancelDialog();
                        ToastUtils.showCenter(ImageBenDiActivity.this, error_msg);
                        ImageBenDiActivity.this.finish();
                    }
                } catch (JSONException e) {
                    CustomProgressTouMing.cancelDialog();
                    ToastUtils.showCenter(ImageBenDiActivity.this, "默认标签获取失败");
                    ImageBenDiActivity.this.finish();
                }
            }
        };
        MyHttpManager.getInstance().getImgDefaultTags(image_id, callBack);

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
            ImageUtils.disBlackImage("file://"  + mImageUrl1.get(position), photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    ImageBenDiActivity.this.finish();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ImageBenDiActivity.this.finish();
        return super.onKeyDown(keyCode, event);
    }
}
