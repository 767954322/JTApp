package com.homechart.app.home.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.CaiJi;
import com.homechart.app.commont.ClassConstant;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.home.base.BaseActivity;
import com.homechart.app.home.bean.caijiimg.CaiJiImageBean;
import com.homechart.app.utils.SharedPreferencesUtils;
import com.homechart.app.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gumenghao on 18/1/23.
 */

public class MyWebViewActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initExtraBundle() {
        super.initExtraBundle();
//        weburl = "http://app.jstyle.cn/jm_interface_1_2/index.php/home/article/app_articledesc?rid=44246";
        weburl = getIntent().getStringExtra("weburl");
//        weburl = "http://mp.weixin.qq.com/s/K6JmSxrVjCn2ZH2-NBF_Cg";//微信
    }

    @Override
    protected void initView() {

        mWeb = (WebView) findViewById(R.id.wb_webview);
        iv_quxiao = (ImageView) findViewById(R.id.iv_quxiao);
        bt_caiji = (Button) findViewById(R.id.bt_caiji);
        cet_clearedit = (EditText) findViewById(R.id.cet_clearedit);
        tv_textview1 = (TextView) findViewById(R.id.tv_textview1);
        tv_textview2 = (TextView) findViewById(R.id.tv_textview2);

        iv_back_icon = (ImageView) findViewById(R.id.iv_back_icon);
        iv_next_icon = (ImageView) findViewById(R.id.iv_next_icon);

    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_quxiao.setOnClickListener(this);
        bt_caiji.setOnClickListener(this);
        iv_back_icon.setOnClickListener(this);
        iv_next_icon.setOnClickListener(this);
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
        weburl = searchContext;
        if (TextUtils.isEmpty(searchContext.trim())) {
            ToastUtils.showCenter(this, "请输入网址");
        } else {
            tv_textview1.setVisibility(View.GONE);
            tv_textview2.setVisibility(View.GONE);
            mWeb.setVisibility(View.VISIBLE);

            String str = "123456";
            str = str.substring(0, 4);

            if (!weburl.trim().substring(0, 4).contains("http")) {
                if (!weburl.trim().substring(0, 4).contains("www.")) {
                    weburl = "www." + weburl;
                }
                weburl = "http://" + weburl;
            }
            mWeb.loadUrl(weburl);
        }

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(weburl)) {

            cet_clearedit.setText(weburl);
        }
        initWebView();
        WebSettings settings = mWeb.getSettings();
        settings.setDomStorageEnabled(true);
        if (TextUtils.isEmpty(weburl)) {
            mWeb.setVisibility(View.GONE);
        } else {
            tv_textview1.setVisibility(View.GONE);
            tv_textview2.setVisibility(View.GONE);
            mWeb.setVisibility(View.VISIBLE);
            if (!weburl.contains("http")) {
                weburl = "http://" + weburl;
            }
            mWeb.loadUrl(weburl);
        }

        mHandler.postDelayed(runnable, 100);
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


                loginStatus = SharedPreferencesUtils.readBoolean(ClassConstant.LoginSucces.LOGIN_STATUS);

                if (!loginStatus) {

                    Intent intent = new Intent(MyWebViewActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 1);

                } else {

                    if (weburl.contains("mp.weixin.qq.com")) {

                        mWeb.loadUrl(CaiJi.WEIXIN);

                    } else if (weburl.contains("www.shejiben.com")) {

                        mWeb.loadUrl(CaiJi.SHEJIBEN);

                    } else {

                        mWeb.loadUrl(CaiJi.PUBLICK);

                    }

                }
                break;
            case R.id.iv_back_icon:
                if (mWeb.canGoBack()) {
                    mWeb.goBack();
                    changeNextStatus();
                }
                break;
            case R.id.iv_next_icon:
                if (mWeb.canGoForward()) {
                    mWeb.goForward();
                    changeNextStatus();
                }
                break;
        }
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
                pageTitle = view.getTitle();
                changeUi(url);
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

    private void changeUi(String url) {
        if (!TextUtils.isEmpty(url)) {
            cet_clearedit.setText(url);
        }
    }

    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void openImage(String imageUrl, String img) {

            List<CaiJiImageBean> mListImage = new ArrayList<>();
            String[] imgs = imageUrl.split(CaiJi.tagItem);
            for (String s : imgs) {
                if (!TextUtils.isEmpty(s)) {
                    String[] imgDes = s.split(CaiJi.tagDesc);
                    if (imgDes.length > 0) {
                        String url = imgDes[0].contains("http") ? imgDes[0] : ("http:" + imgDes[0]);
                        if (url.length() > 5 &&
                                PublicUtils.isValidUrl(url) &&
                                !s.substring(url.length() - 3, url.length()).
                                        equalsIgnoreCase("gif")) {
                            CaiJiImageBean caiJiImageBean = new CaiJiImageBean();
                            caiJiImageBean.setUrl(url);
                            caiJiImageBean.setDes(imgDes.length > 1 ? imgDes[1] : "");
                            mListImage.add(caiJiImageBean);
                        }
                    }
                }
            }
            if (mListImage.size() > 0) {
                if (mListImage.size() == 1 && mListImage.get(0).getUrl().equals("")) {
                    ToastUtils.showCenter(MyWebViewActivity.this, "未采集到图片信息");
                } else {
                    Intent intent = new Intent(MyWebViewActivity.this, CaiJiImgListActivity.class);
                    intent.putExtra("number", 0);
                    intent.putExtra("pic_url_list", (Serializable) mListImage);
                    intent.putExtra("title", pageTitle);
                    intent.putExtra("webUrl", cet_clearedit.getText().toString());
                    startActivity(intent);
                }
            } else {
                ToastUtils.showCenter(MyWebViewActivity.this, "未采集到图片信息");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mWeb.canGoBack()) {
                mWeb.goBack();
                return true;
            } else {
                MyWebViewActivity.this.finish();
            }

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

    public void changeNextStatus() {
        if (mWeb.canGoBack()) {
            iv_back_icon.setImageResource(R.drawable.forward_liang);
        } else {
            iv_back_icon.setImageResource(R.drawable.forward_an);
        }
        if (mWeb.canGoForward()) {
            iv_next_icon.setImageResource(R.drawable.back_liang);
        } else {
            iv_next_icon.setImageResource(R.drawable.back_an);
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            changeNextStatus();
            mHandler.postDelayed(this, 200);
        }
    };

    Handler mHandler = new Handler() {
    };
    private TextView tv_tital_comment;
    private String pageTitle = "";
    private EditText cet_clearedit;
    private TextView tv_textview1;
    private TextView tv_textview2;
    private ImageView iv_back_icon;
    private ImageView iv_next_icon;
    private ImageView iv_quxiao;
    private Button bt_caiji;
    private String weburl;
    private WebView mWeb;
    private Boolean loginStatus;

}

