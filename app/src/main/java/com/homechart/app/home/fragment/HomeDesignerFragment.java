package com.homechart.app.home.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.base.BaseFragment;
import com.homechart.app.utils.umengutils.CustomShareListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;


@SuppressLint("ValidFragment")
public class HomeDesignerFragment extends BaseFragment implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private TextView tv_test;
    private CustomShareListener mShareListener;

    public HomeDesignerFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public HomeDesignerFragment() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_designer_pic;
    }

    @Override
    protected void initView() {

        tv_test = (TextView) rootView.findViewById(R.id.tv_test);

    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_test.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        mShareListener = new CustomShareListener(activity.getBaseContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_test:

                UMImage image = new UMImage(activity, "https://timgsa.baidu.com/timg?image&quality=80" +
                        "&size=b9999_10000&sec=1495875952247&di=909c86f2c7c7998e261892cdfe15e7c7&imgtype=0" +
                        "&src=http%3A%2F%2Fimg.upload.tom.com%2Fdata3%2Fupload%2F632%2F359%2F1270163575-636261716.jpg");
                image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
                image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
                UMWeb web = new UMWeb("http://www.idcool.com.cn");
                web.setTitle("测试密码");//标题
                web.setThumb(image);  //缩略图
                web.setDescription("测试详情描述");//描述
                new ShareAction(activity).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
                        SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA).withMedia(web).setCallback(mShareListener).open();


                break;
        }
    }
}
