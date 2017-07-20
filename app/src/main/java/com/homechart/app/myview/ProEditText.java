package com.homechart.app.myview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Jack on 2016/10/9 09:20.
 * Email: zhuochangjing@foxmail.com
 */
public class ProEditText extends AppCompatEditText {

    /**
     * EditText右侧的图标
     */
    public Drawable mRightDrawable;

    public RightPicOnclickListener rightPicOnclickListener;

    public ProEditText(Context context) {
        super(context);
        init(context);
    }

    public ProEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(final Context context) {
        mRightDrawable = getCompoundDrawables()[2];

        if (mRightDrawable == null) {
            return;
        }

        mRightDrawable.setBounds(0, 0, mRightDrawable.getIntrinsicWidth(), mRightDrawable.getIntrinsicHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {

                    //设置点击EditText右侧图标EditText失去焦点，
                    // 防止点击EditText右侧图标EditText获得焦点，软键盘弹出
                    setFocusableInTouchMode(false);
                    setFocusable(false);

                    //点击EditText右侧图标事件接口回调
                    if (rightPicOnclickListener != null) {
                        rightPicOnclickListener.rightPicClick();
                    }
                } else {
                    //设置点击EditText输入区域，EditText请求焦点，软键盘弹出，EditText可编辑
                    setFocusableInTouchMode(true);
                    setFocusable(true);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void setRightPicOnclickListener(RightPicOnclickListener rightPicOnclickListener) {
        this.rightPicOnclickListener = rightPicOnclickListener;
    }

    public interface RightPicOnclickListener {
        void rightPicClick();
    }
}