package com.homechart.app.croplayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.homechart.app.R;
import com.homechart.app.croplayout.handler.OnBoxChangedListener;
import com.homechart.app.croplayout.model.ScalableBox;
import com.homechart.app.utils.ToastUtils;
import com.homechart.app.utils.imageloader.ImageUtils;

/**
 * Created by yulu on 12/3/15.
 * <p>
 * View to display photo and selection box
 */
public class EditPhotoViewMore extends FrameLayout {

    private static final int LINE_WIDTH = 2;
    private static final int CORNER_LENGTH = 30;

    private Context context;

    public ImageView imageView;
    public SelectionView selectionView;
    public EditableImage editableImage;

    private float lineWidth;
    private float cornerWidth;
    private float cornerLength;
    private int lineColor;
    private int cornerColor;
    private int shadowColor;

    private String urlImage ;

    public EditPhotoViewMore(Context context) {
        super(context);
        this.context = context;
    }

    public EditPhotoViewMore(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        obtainAttributes(context, attrs);
    }

    public EditPhotoViewMore(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        obtainAttributes(context, attrs);
    }

    private int mW;
    private int mH;

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //set the default image and selection view
        this.mH = h;
        this.mW = w;
        if (editableImage != null && selectionView != null ) {
            editableImage.setViewSize(w, h);
            if(editableImage.getOriginalImage() == null){
                ImageUtils.disRectangleImage(urlImage,imageView);
            }else {
                imageView.setImageBitmap(editableImage.getOriginalImage());
            }
            if(null != editableImage.getBox()){
                selectionView.setBoxSize(editableImage, editableImage.getBox(), w, h);
            }
        }

        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * update view with editable image
     *
     * @param context       activity
     * @param editableImage image to be edited
     */
    public void initView(Context context, EditableImage editableImage, boolean initSearch) {
        this.editableImage = editableImage;

        selectionView = new SelectionView(context,
                lineWidth, cornerWidth, cornerLength,
                lineColor, cornerColor, shadowColor, editableImage);
        imageView = new ImageView(context);

        imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        selectionView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        addView(imageView, 0);
        if (initSearch) {
            addView(selectionView, 1);
        }
    }

    public void removeView() {
        removeViewAt(1);
    }

    //TODO
    public void changeBox(ScalableBox box) {
        editableImage.getBox().setX1(box.getX1());
        editableImage.getBox().setY1(box.getY1());
        editableImage.getBox().setX2(box.getX2());
        editableImage.getBox().setY2(box.getY2());
        editableImage.setBox(box);
        invalidate();
    }

    public void setOnBoxChangedListener(OnBoxChangedListener onBoxChangedListener) {
        if (null != selectionView) {
            selectionView.setOnBoxChangedListener(onBoxChangedListener);
        } else {
            ToastUtils.showCenter(context, "无识别数据！");
        }
    }

    /**
     * rotate image
     */
    public void rotateImageView() {
        //rotate bitmap
        editableImage.rotateOriginalImage(90);

        //re-calculate and draw selection box
        editableImage.getBox().setX1(0);
        editableImage.getBox().setY1(0);
        editableImage.getBox().setX2(editableImage.getActualSize()[0]);
        editableImage.getBox().setY2(editableImage.getActualSize()[1]);
        selectionView.setBoxSize(editableImage, editableImage.getBox(), editableImage.getViewWidth(), editableImage.getViewHeight());

        //set bitmap as view
        imageView.setImageBitmap(editableImage.getOriginalImage());
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CropLayout);
        lineWidth = ta.getDimension(R.styleable.CropLayout_crop_line_width, dp2px(LINE_WIDTH));
        lineColor = ta.getColor(R.styleable.CropLayout_crop_line_color, Color.parseColor("#ffffff"));
        cornerWidth = ta.getDimension(R.styleable.CropLayout_crop_corner_width, dp2px(LINE_WIDTH * 2));
        cornerLength = ta.getDimension(R.styleable.CropLayout_crop_corner_length, dp2px(CORNER_LENGTH));
        cornerColor = ta.getColor(R.styleable.CropLayout_crop_corner_color, Color.parseColor("#ffffff"));
        shadowColor = ta.getColor(R.styleable.CropLayout_crop_shadow_color, Color.parseColor("#aa111111"));
    }

    protected int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public SelectionView getSelectionView() {
        return selectionView;
    }

    public EditableImage getEditableImage() {
        return editableImage;
    }
}
