package com.homechart.app.home.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.KeyConstans;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.commont.UrlConstants;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.imagedetail.ImageDetailsActivity;
import com.homechart.app.myview.ClearEditText;
import com.homechart.app.utils.ToastUtils;

import java.io.Serializable;
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
    private Button bt_caiji;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
//        weburl = "http://app.jstyle.cn/jm_interface_1_2/index.php/home/article/app_articledesc?rid=44246";
        weburl = getIntent().getStringExtra("weburl");
    }

    @Override
    protected void initView() {

        mWeb = (WebView) findViewById(R.id.wb_webview);
        iv_quxiao = (ImageView) findViewById(R.id.iv_quxiao);
        bt_caiji = (Button) findViewById(R.id.bt_caiji);
        pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
        cet_clearedit = (ClearEditText) findViewById(R.id.cet_clearedit);

        cet_clearedit.setClearIconVisible(false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_quxiao.setOnClickListener(this);
        bt_caiji.setOnClickListener(this);
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
        WebSettings settings = mWeb.getSettings();

        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWeb.setBackgroundColor(0); // 设置背景色
        mWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onPageFinished(WebView view, String url) {
                //网页加载完成 走JS代码
//                clickImage();
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });
        mWeb.addJavascriptInterface(new JavascriptInterface(), "imageListener");
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
            case R.id.bt_caiji:
                mWeb.loadUrl("javascript:(function(){" +
                        "var objs = document.getElementsByTagName(\"img\"); " +
                        "var imgUrl = \"\";" +
                        "var filter = [\"img//EventHead.png\",\"img//kong.png\",\"hdtz//button.png\"];" +
                        "var isShow = true;" +
                        "for(var i=0;i<objs.length;i++){" +
                        "if(objs[i].width>80){" +
                        "imgUrl += objs[i].src + ',';isShow = true;" +
                        "}" +
                        "}" +
                        "window.imageListener.openImage(imgUrl,'');" +
                        "})()"
                );
                break;
        }
    }

    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void openImage(String imageUrl, String img) {
            int position = 0;
            String[] imgs = imageUrl.split(",");
            ArrayList<String> imgUrlList = new ArrayList<>();
            for (String s : imgs) {
                imgUrlList.add(s);
            }
            if(imgUrlList.size() > 0){
                Intent intent = new Intent(MyWebViewActivity.this, CaiJiImgListActivity.class);
                intent.putExtra("number", position);
                intent.putExtra("pic_url_list", (Serializable) imgUrlList);
                intent.putExtra("click_position", 0);
                startActivity(intent);
            }else {
                ToastUtils.showCenter(MyWebViewActivity.this,"未采集到图片信息");
            }
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

    private void clickImage() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        //if(isShow && objs[i].width>80)是将小图片过滤点,不显示小图片,如果没有限制,可去掉
        mWeb.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "var imgUrl = \"\";" +
                "var filter = [\"img//EventHead.png\",\"img//kong.png\",\"hdtz//button.png\"];" +
                "var isShow = true;" +
                "for(var i=0;i<objs.length;i++){" +
                "for(var j=0;j<filter.length;j++){" +
                "if(objs[i].src.indexOf(filter[j])>=0) {" +
                "isShow = false; break;}}" +
                "if(isShow && objs[i].width>80){" +
                "imgUrl += objs[i].src + ',';isShow = true;" +
                "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imageListener.openImage(imgUrl,this.src);" +
                "    }" +
                "}" +
                "}" +
                "})()"
        );
    }


}

