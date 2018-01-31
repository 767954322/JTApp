package com.homechart.app.home.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.KeyConstans;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.UrlConstants;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by gumenghao on 18/1/23.
 */

public class MyWebViewActivity extends BaseActivity implements View.OnClickListener {
    private String weburl;
    private WebView mWeb;
    private TextView tv_tital_comment;
    private ProgressBar pb_progress;
    private ImageView iv_quxiao;
    private ClearEditText cet_clearedit;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
        weburl = getIntent().getStringExtra("weburl");
    }

    @Override
    protected void initView() {

        mWeb = (WebView) findViewById(R.id.wb_webview);
        iv_quxiao = (ImageView) findViewById(R.id.iv_quxiao);
        pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
        cet_clearedit = (ClearEditText) findViewById(R.id.cet_clearedit);

        cet_clearedit.setClearIconVisible(false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_quxiao.setOnClickListener(this);
        cet_clearedit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(MyWebViewActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    search();
                    return true;
                }

                return false;
            }
        });
    }

    // 搜索功能
    private void search() {

        String searchContext = cet_clearedit.getText().toString().trim();
        if (TextUtils.isEmpty(searchContext.trim())) {
            ToastUtils.showCenter(this, "请输入网址");
        } else {
            mWeb.loadUrl(searchContext);
        }

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        cet_clearedit.setText(weburl);
        initWebView();
        WebSettings settings = mWeb.getSettings();
        settings.setDomStorageEnabled(true);
        mWeb.loadUrl(weburl);
    }

    private void initWebView() {
        mWeb.removeJavascriptInterface("searchBoxJavaBredge_");//禁止远程代码执行
        WebSettings settings = mWeb.getSettings();
        settings.setDomStorageEnabled(true);
        // 缓存(cache)
        settings.setAppCacheEnabled(true);      // 默认值 false

// 存储(storage)
        settings.setDatabaseEnabled(true);      // 默认值 false

// 是否支持viewport属性，默认值 false
// 页面通过`<meta name="viewport" ... />`自适应手机屏幕
        settings.setUseWideViewPort(true);
// 是否使用overview mode加载页面，默认值 false
// 当页面宽度大于WebView宽度时，缩小使页面宽度等于WebView宽度
        settings.setLoadWithOverviewMode(true);

// 是否支持Javascript，默认值false
        settings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0以上允许加载http和https混合的页面(5.0以下默认允许，5.0+默认禁止)
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

//        settings.setSupportMultipleWindows(false);
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
//        settings.setSupportZoom(true);          //支持缩放
//        settings.setBuiltInZoomControls(false);  //不启用内置缩放装置
//        settings.setJavaScriptEnabled(true);    //启用JS脚本
//        mWeb.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
//        settings.setUserAgentString(KeyConstans.USER_AGENT);

//        mWeb.setWebChromeClient(new WebChromeClient() {
//                                    @Override
//                                    public void onReceivedTitle(WebView view, String title) {
//                                        super.onReceivedTitle(view, title);
//                                    }
//
//                                    @Override
//                                    public void onProgressChanged(WebView view, int newProgress) {
//                                        super.onProgressChanged(view, newProgress);
//                                        if (newProgress == 100) {
//                                            // 网页加载完成
//                                            pb_progress.setVisibility(View.GONE);
//                                        } else {
//                                            // 加载中
//                                            pb_progress.setProgress(newProgress);
//                                        }
//                                    }
//                                }
//        );
//        mWeb.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//            }
//
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_quxiao:
                // 先隐藏键盘
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(MyWebViewActivity.this.getCurrentFocus()
                                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                MyWebViewActivity.this.finish();
                break;
        }
    }

    /**
     * js接口
     */
    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            getHtmlContent(html);
        }
    }

    /**
     * 获取内容
     *
     * @param html
     */
    protected void getHtmlContent(final String html) {
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && mWeb.canGoBack()) {
            mWeb.goBack();
            return true;
        } else {
            MyWebViewActivity.this.finish();
        }
        return true;
    }
}
