package com.homechart.app.hotposition;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.utils.UIUtils;

import java.util.ArrayList;

public class NewImageLayout extends FrameLayout implements View.OnClickListener {

    ArrayList<PointSimple> points;

    FrameLayout layouPoints;

    int mWidth;
    int mHeight;

    Context mContext;
    PositionClickImp mPositionClickImp;
    private AnimationSet animationSet;

    public NewImageLayout(Context context) {
        this(context, null);
    }

    public NewImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {

        mContext = context;

        View imgPointLayout = inflate(context, R.layout.layout_imgview_newpoint, this);

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

    public void setImgBg(int width, int height, PositionClickImp positionClickImp) {

        this.mPositionClickImp = positionClickImp;

        addKuang(width, height);
//      addPoints(width, height);

    }

    public void setPoints(ArrayList<PointSimple> points) {

        this.points = points;
    }

    private void addKuang(final int width, final int height) {

        layouPoints.removeAllViews();

        if (points != null && points.size() > 0) {
            for (int i = 0; i < points.size(); i++) {
                initAnimation();
                double width_scale = points.get(i).width_scale;
                double height_scale = points.get(i).height_scale;
                RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_img_kuang, this, false);
                LayoutParams layoutParams = (LayoutParams) relativeLayout.getLayoutParams();
                int object_wide = (int) (points.get(i).width_object * width / 2);
                int object_heigh = (int) (points.get(i).height_object * height / 2);
                layoutParams.leftMargin = (int) (width * width_scale);
                layoutParams.topMargin = (int) (height * height_scale);
                layoutParams.width = object_wide * 2;
                layoutParams.height = object_heigh * 2;
                relativeLayout.setLayoutParams(layoutParams);

                relativeLayout.setTag(i);
                relativeLayout.startAnimation(animationSet);

                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        addPoints(width, height);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                relativeLayout.setOnClickListener(this);
                layouPoints.addView(relativeLayout, layoutParams);
            }

        }
    }

    int num = 0;

    private void initAnimation() {
        //1.AnimationSet
        animationSet = new AnimationSet(true);
        animationSet.setInterpolator(new LinearInterpolator());
        //透明度
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.5f);
        alphaAnimation.setRepeatMode(Animation.RESTART);
        //缩放（以某个点为中心缩放）
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.0f, 1, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setRepeatMode(Animation.RESTART);
        //添加动画
        animationSet.setFillAfter(true);
//        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(1500);
        animationSet.setRepeatMode(Animation.RESTART);
        animationSet.setRepeatCount(1);
        animationSet.setStartOffset(800);
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
                int object_wide = (int) (points.get(i).width_object * width / 2);
                int object_heigh = (int) (points.get(i).height_object * height / 2);
                layoutParams.leftMargin = (int) (width * width_scale) + object_wide - UIUtils.getDimens(R.dimen.font_10);
                layoutParams.topMargin = (int) (height * height_scale) + object_heigh - UIUtils.getDimens(R.dimen.font_10);

                imageView.setOnClickListener(this);
                layouPoints.addView(view, layoutParams);
            }
        }
    }


    int tag = -1;

    @Override
    public void onClick(View view) {
        int pos = (int) view.getTag();
        mPositionClickImp.onClickPosition(pos);
    }
}
