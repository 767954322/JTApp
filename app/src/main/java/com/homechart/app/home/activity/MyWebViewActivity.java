package com.homechart.app.home.activity;

import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.KeyConstans;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.UrlConstants;
import com.homechart.app.home.base.BaseActivity;

import java.util.ArrayList;

/**
 * Created by gumenghao on 18/1/23.
 */

public class MyWebViewActivity extends BaseActivity implements View.OnClickListener {
    private String weburl;
    private WebView mWeb;
    private ImageButton nav_left_imageButton;
    private TextView tv_tital_comment;
    private ProgressBar pb_progress;

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
        nav_left_imageButton = (ImageButton) findViewById(R.id.nav_left_imageButton);
        tv_tital_comment = (TextView) findViewById(R.id.tv_tital_comment);
        pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
    }

    @Override
    protected void initListener() {
        super.initListener();
        nav_left_imageButton.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        tv_tital_comment.setText("采集详情");
        initWebView();
        mWeb.loadUrl(weburl);
    }

    private void initWebView() {
        mWeb.removeJavascriptInterface("searchBoxJavaBredge_");//禁止远程代码执行
        WebSettings settings = mWeb.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setSupportMultipleWindows(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
//        settings.setSupportZoom(true);          //支持缩放
        settings.setBuiltInZoomControls(false);  //不启用内置缩放装置
        settings.setJavaScriptEnabled(true);    //启用JS脚本
        mWeb.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        settings.setUserAgentString(KeyConstans.USER_AGENT);

        mWeb.setWebChromeClient(new WebChromeClient() {
                                    @Override
                                    public void onReceivedTitle(WebView view, String title) {
                                        super.onReceivedTitle(view, title);
                                    }

                                    @Override
                                    public void onProgressChanged(WebView view, int newProgress) {
                                        super.onProgressChanged(view, newProgress);
                                        if (newProgress == 100) {
                                            // 网页加载完成
                                            pb_progress.setVisibility(View.GONE);
                                        } else {
                                            // 加载中
                                            pb_progress.setProgress(newProgress);
                                        }
                                    }
                                }
        );
        mWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_left_imageButton:
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
