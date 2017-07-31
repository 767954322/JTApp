package com.homechart.app.upapk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;

/**
 * Created by Gu FanFan.
 * Date: 2017/4/13, 11:45.
 * 对话框utils.
 */

public class DialogUtils {

    private static Dialog createDialog(Context context, int layoutId, int themeId) {
        View view = View.inflate(context, layoutId, null);
        Dialog dialog;
        dialog = new Dialog(context, themeId);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        return dialog;
    }

    public static Dialog startDownloadDialog(Context context) {
        return createDialog(context, R.layout.layout_download_loading, R.style.CustomDialog);
    }
}
