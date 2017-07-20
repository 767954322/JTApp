package com.homechart.app.utils.alertview;

import android.view.Gravity;

import com.homechart.app.R;

/**
 * @author he.liu .
 * @version v1.0 .
 * @date 2016-6-20 .
 * @file AlertAnimateUtil.java .
 * @brief 弹出动画工具类 .
 */
public class AlertAnimateUtil {
    private static final int INVALID = -1;

    /**
     * Get default animation resource when not defined by the user
     *
     * @param gravity       the gravity of the dialog
     * @param isInAnimation determine if is in or out animation. true when is is
     * @return the id of the animation resource
     */
    static int getAnimationResource(int gravity, boolean isInAnimation) {
        switch (gravity) {
            case Gravity.BOTTOM:
                return isInAnimation ? R.anim.slide_in_bottom : R.anim.slide_out_bottom;
            case Gravity.CENTER:
                return isInAnimation ? R.anim.fade_in_center : R.anim.fade_out_center;
        }
        return INVALID;
    }
}
