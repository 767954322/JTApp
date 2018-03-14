package com.homechart.app.utils.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;

public class CustomProgressTouMing extends Dialog {
    public static CustomProgressTouMing dialog;

    public CustomProgressTouMing(Context context) {
        super(context);
    }

    public CustomProgressTouMing(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 当窗口焦点改变时调用
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        // 获取ImageView上的动画背景
        AnimationDrawable spinner = (AnimationDrawable) imageView
                .getBackground();
        // 开始动画
        spinner.start();
    }

    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.message).setVisibility(View.VISIBLE);
            TextView txt = (TextView) findViewById(R.id.message);
            txt.setText(message);
            txt.invalidate();
        }
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param context        上下文
     * @param message        提示
     * @param onTouchOutside 是否点击屏幕消失
     * @param cancelListener 按下返回键监听
     * @return
     */
    public static CustomProgressTouMing show(Context context, CharSequence message,
                                             boolean onTouchOutside, OnCancelListener cancelListener) {
        dialog = new CustomProgressTouMing(context, R.style.Custom_Progress);
        dialog.setTitle("");
        dialog.setContentView(R.layout.view_dialog_progress_custom);
        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) dialog.findViewById(R.id.message);
            txt.setText(message);
        }
        // 设置点击屏幕dialog的消失
        dialog.setCanceledOnTouchOutside(onTouchOutside);
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.1f;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }

    public static void cancelDialog() {
        if (CustomProgressTouMing.dialog != null) {
            CustomProgressTouMing.dialog.cancel();
        }
    }

    /**
     * 默认的进度条
     *
     * @param activity
     */
    public static void showDefaultProgress(Activity activity) {
        if (null != CustomProgressTouMing.dialog && !CustomProgressTouMing.dialog.isShowing()) {
            CustomProgressTouMing.show(activity, "", false, null);
        }
    }

    /**
     * 是否显示
     * @return
     */
    public static boolean isOnShowing(){
        if (dialog != null){
            return dialog.isShowing();
        }
        return false;
    }
}
