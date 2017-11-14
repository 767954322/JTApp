package com.homechart.app.visearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.croplayout.EditPhotoViewMore;
import com.homechart.app.croplayout.EditableImage;
import com.homechart.app.croplayout.NewEditPhotoViewMore;
import com.homechart.app.croplayout.handler.OnBoxChangedListener;
import com.homechart.app.croplayout.model.ScalableBox;
import com.homechart.app.home.activity.NewShopDetailsActivity;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.searchfservice.SearchSBean;
import com.homechart.app.home.bean.searchfservice.SearchSObjectBean;
import com.homechart.app.home.bean.searchfservice.SearchSObjectInfoBean;
import com.homechart.app.hotposition.NewImageLayout;
import com.homechart.app.hotposition.PointSimple;
import com.homechart.app.hotposition.PositionClickImp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<SearchSObjectBean> listSearch;
    private int mW;
    private int mH;
    private String clickposition;
    private int position;
    private String cropName;

    private boolean ifOpenSearch = false;
    private boolean ifMoveKuang = false;

    private String photoPath = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES).getPath() + File.separator + "JiaTuApp";

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
        position = intent.getIntExtra("position", -1);
        clickposition = intent.getStringExtra("clickposition");
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
        mPhotoImage.setOnBoxChangedListener(new OnBoxChangedListener() {
            @Override
            public void onChanged(int x1, int y1, int x2, int y2) {
                ifMoveKuang = true;
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        listSearch = searchSBean.getObject_list();
        if (network.equals("true")) {
            editableImage = new EditableImage(imagePath, true);
        } else {
            editableImage = new EditableImage(imagePath);
            mPhotoImage.setUrlImage("file://" + imagePath, true);
        }

        mPhotoImage.initView(NewSearchResultActivity.this, editableImage, true, this);
        if (mPhotoImage.getChildCount() == 2) {
            mPhotoImage.getChildAt(1).setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
                NewSearchResultActivity.this.finish();
                break;
            case R.id.tv_fuwei:
                tv_fuwei.setVisibility(View.GONE);
                if (listSearch != null && listSearch.size() > 0) {
                    ifOpenSearch = false;
                    if (mPhotoImage.getChildCount() == 2) {
                        mPhotoImage.getChildAt(1).setVisibility(View.GONE);
                    }
                    rly_point.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_sousuo:
                if (editableImage != null && editableImage.getBox() != null && ifOpenSearch) {
                    Bitmap bitmap = imageCrop(editableImage.getOriginalImage());
                    cropName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
                    File file = createFile(photoPath, cropName);
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String loc = editableImage.getBox().getX1() * 1.000000 / mW + "-" +
                            editableImage.getBox().getY1() * 1.000000 / heightImage + "-" +
                            (editableImage.getBox().getX2() - editableImage.getBox().getX1()) * 1.000000 / widerImage + "-" +
                            (editableImage.getBox().getY2() - editableImage.getBox().getY1()) * 1.000000 / heightImage;
                    Intent intent = new Intent(NewSearchResultActivity.this, NewShopDetailsActivity.class);
                    intent.putExtra("image_path", photoPath + "/" + cropName);
                    intent.putExtra("image_url", searchSBean.getImage_url());
                    intent.putExtra("loc", loc);
                    intent.putExtra("ifMoveKuang", ifMoveKuang);
                    if (listSearch != null && listSearch.size() > 0 && listSearch.size() > currentPosition) {
                        intent.putExtra("object_sign", listSearch.get(currentPosition).getObject_info().getObject_sign());
                        intent.putExtra("category_id", listSearch.get(currentPosition).getObject_info().getCategory_id());
                    }
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void setLayoutSize(int h, int w) {
        this.mH = h;
        this.mW = w;
        rly_point.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mW, mH);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                rly_point.setLayoutParams(layoutParams);
            }
        });

        if (null != searchSBean.getObject_list() && searchSBean.getObject_list().size() > 0) {

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
            if (!TextUtils.isEmpty(clickposition) && clickposition.equals("true")) {
                ifOpenSearch = true;
                rly_point.setVisibility(View.GONE);
            }
            il_points.setPoints(pointSimples);
            il_points.setImgBg(mW, mH, this, clickposition);
            if (!TextUtils.isEmpty(clickposition) && clickposition.equals("true")) {
                onClickPosition(position);
            }
        } else {
            ifOpenSearch = true;
            tv_fuwei.setVisibility(View.GONE);
            ScalableBox scalableBox = new ScalableBox(0, 0, 0, 0);
            editableImage.setBox(scalableBox);
            mPhotoImage.changeBox(editableImage, scalableBox);
            if (mPhotoImage.getChildCount() == 2) {
                mPhotoImage.getChildAt(1).setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onClickPosition(int pos) {
        tv_fuwei.setVisibility(View.VISIBLE);
        currentPosition = pos;
        ifMoveKuang = false;
        ifOpenSearch = true;
        rly_point.setVisibility(View.GONE);
        SearchSObjectInfoBean searchSObjectInfoBean = listSearch.get(pos).getObject_info();
        widerImage = mPhotoImage.getEditableImage().getOriginalImage().getWidth();
        heightImage = mPhotoImage.getEditableImage().getOriginalImage().getHeight();

        int x1 = (int) (searchSObjectInfoBean.getX() * widerImage);
        int y1 = (int) (searchSObjectInfoBean.getY() * heightImage);
        int x2 = (int) (searchSObjectInfoBean.getX() * widerImage) + (int) (searchSObjectInfoBean.getWidth() * widerImage);
        int y2 = (int) (searchSObjectInfoBean.getY() * heightImage) + (int) (searchSObjectInfoBean.getHeight() * heightImage);
        if (Math.abs(x1 - x2) < 40) {
            int xAdd = (40 - Math.abs(x1 - x2)) / 2;
            x1 = x1 - xAdd;
            x2 = x2 + xAdd;
        }
        if (Math.abs(y1 - y2) < 40) {
            int yAdd = (40 - Math.abs(y1 - y2)) / 2;
            y1 = y1 - yAdd;
            y2 = y2 + yAdd;
        }

        if (x1 < 3) {
            x1 = 3;
        }
        if (y1 < 3) {
            y1 = 3;
        }
        if (x2 > widerImage - 3) {
            x2 = widerImage - 3;
        }
        if (y2 > heightImage - 3) {
            y2 = heightImage - 3;
        }

        ScalableBox scalableBox = new ScalableBox(x1, y1, x2, y2);
        editableImage.setBox(scalableBox);
        mPhotoImage.changeBox(editableImage, scalableBox);
        if (mPhotoImage.getChildCount() == 2) {
            mPhotoImage.getChildAt(1).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 按正方形裁切图片
     */
    public Bitmap imageCrop(Bitmap bitmap) {
        int x1 = editableImage.getBox().getX1();
        int y1 = editableImage.getBox().getY1();
        int x2 = editableImage.getBox().getX2();
        int y2 = editableImage.getBox().getY2();

        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wCrop = x2 - x1;// 裁切后所取的正方形区域宽
        int hCrop = y2 - y1;// 裁切后所取的正方形区域高

        if (x1 == 0 && x2 == 0) {
            wCrop = w;
        }
        if (y1 == 0 && y2 == 0) {
            hCrop = h;
        }

        int retX = x1;//基于原图，取正方形左上角x坐标
        int retY = y1;
        //下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, wCrop, hCrop, null, false);
    }


    private File createFile(String path, String name) {
        File folder = new File(path);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                return null;
            }
        }
        return new File(folder, name);
    }


    private int currentPosition = -1;
}
