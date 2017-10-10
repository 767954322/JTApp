package com.homechart.app.hotposition;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.homechart.app.R;

import java.util.ArrayList;

public class ImageLayout extends FrameLayout implements View.OnClickListener {

    ArrayList<PointSimple> points;

    FrameLayout layouPoints;

    ImageView imgBg;
    int mWidth;
    int mHeight;

    Context mContext;
    PositionClickImp mPositionClickImp;

    public ImageLayout(Context context) {
        this(context, null);
    }

    public ImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {

        mContext = context;

        View imgPointLayout = inflate(context, R.layout.layout_imgview_point, this);

        imgBg = (ImageView) imgPointLayout.findViewById(R.id.imgBg);
        layouPoints = (FrameLayout) imgPointLayout.findViewById(R.id.layouPoints);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setImgBg(int width, int height, String imgUrl, PositionClickImp positionClickImp) {

        this.mPositionClickImp = positionClickImp;
        ViewGroup.LayoutParams lp = imgBg.getLayoutParams();
        lp.width = width;
        lp.height = height;

        imgBg.setLayoutParams(lp);

        ViewGroup.LayoutParams lp1 = layouPoints.getLayoutParams();
        lp1.width = width;
        lp1.height = height;
        this.mWidth = width;
        this.mHeight = height;

        layouPoints.setLayoutParams(lp1);

        Glide.with(mContext).load(imgUrl).asBitmap().into(imgBg);

        addPoints(width, height);

    }

    public void setPoints(ArrayList<PointSimple> points) {

        this.points = points;
    }

    private void addPoints(int width, int height) {

        layouPoints.removeAllViews();

        if (points != null && points.size() > 0) {
            for (int i = 0; i < points.size(); i++) {

                double width_scale = points.get(i).width_scale;
                double height_scale = points.get(i).height_scale;


                LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_img_point, this, false);
                ImageView imageView = (ImageView) view.findViewById(R.id.imgPoint);
                imageView.setTag(i);

                AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                animationDrawable.start();

                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();

                layoutParams.leftMargin = (int) (width * width_scale);
                layoutParams.topMargin = (int) (height * height_scale);

                imageView.setOnClickListener(this);

                layouPoints.addView(view, layoutParams);
            }
        }
    }

    View view1;

    @Override
    public void onClick(View view) {
//      if(view1 != null){
//          layouPoints.addView(view1);
//      }
        int pos = (int) view.getTag();
//      view1 = layouPoints.getChildAt(pos);
//      layouPoints.removeView(view1);
//      layouPoints.invalidate();
        mPositionClickImp.onClickPosition(pos);
    }
}