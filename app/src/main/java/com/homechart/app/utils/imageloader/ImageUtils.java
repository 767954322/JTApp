package com.homechart.app.utils.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.homechart.app.MyApplication;
import com.homechart.app.R;
import com.homechart.app.commont.FlexibleRoundedBitmapDisplayer;
import com.homechart.app.commont.PublicUtils;
import com.homechart.app.utils.glide.GlideImgManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

public class ImageUtils {

    //加载矩形大图的options
    private static final DisplayImageOptions rectangleoptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.color.white)
            .showImageOnFail(R.color.white)
            .showImageForEmptyUri(R.color.white)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.ALPHA_8)
            .imageScaleType(ImageScaleType.EXACTLY)
            .resetViewBeforeLoading(true)
            .build();

    //加载矩形大图的options
    private static final DisplayImageOptions rectangleoptionshuodong = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.datu)
            .showImageOnFail(R.drawable.datu)
            .showImageForEmptyUri(R.drawable.datu)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.ALPHA_8)
            .imageScaleType(ImageScaleType.EXACTLY)
            .resetViewBeforeLoading(true)
            .build();

    //加载矩形大图的options(加载中为透明)
    private static final DisplayImageOptions rectangleoptionstou = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.color.touming)
            .showImageOnFail(R.color.touming)
            .showImageForEmptyUri(R.color.touming)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.ALPHA_8)
            .imageScaleType(ImageScaleType.EXACTLY)
            .resetViewBeforeLoading(true)
            .build();

    //demo圆形的options
    private static final DisplayImageOptions roundOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .displayer(new RoundedBitmapDisplayer(360))
            .cacheOnDisk(true)
            .showImageOnFail(R.color.white)
            .showImageOnLoading(R.color.white)
            .showImageForEmptyUri(R.color.white)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    //demo圆角的options
    private static final DisplayImageOptions filletOptions = new DisplayImageOptions.Builder()
            .showStubImage(R.color.white)
            .showImageForEmptyUri(R.color.white)
            .showImageOnFail(R.color.white)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .bitmapConfig(Bitmap.Config.RGB_565)   //设置图片的解码类型
            .displayer(new RoundedBitmapDisplayer(8))
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    //demo半个圆角的options
    private static final DisplayImageOptions filletOptionsHalf = new DisplayImageOptions.Builder()
            .showStubImage(R.color.white)
            .showImageForEmptyUri(R.color.white)
            .showImageOnFail(R.color.white)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .bitmapConfig(Bitmap.Config.RGB_565)   //设置图片的解码类型
            .displayer(new FlexibleRoundedBitmapDisplayer(8, FlexibleRoundedBitmapDisplayer.CORNER_TOP_LEFT | FlexibleRoundedBitmapDisplayer.CORNER_TOP_RIGHT))
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    //某种情况加载的图片北京莫名其妙变成黑色，可以使用这种情况
    private static final DisplayImageOptions blackOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.color.white)
            .showImageOnFail(R.color.white)
            .showImageForEmptyUri(R.color.white)
            .cacheInMemory(false)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .resetViewBeforeLoading(true)
            .build();

    //demo圆角的options
    private static final DisplayImageOptions filletOptionsDefaul = new DisplayImageOptions.Builder()
            .showStubImage(R.drawable.jia)
            .showImageForEmptyUri(R.drawable.jia)
            .showImageOnFail(R.drawable.jia)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .bitmapConfig(Bitmap.Config.RGB_565)   //设置图片的解码类型
            .displayer(new RoundedBitmapDisplayer(8))
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    //demo圆形的options
    private static final DisplayImageOptions roundOptionsDefaul = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .displayer(new RoundedBitmapDisplayer(360))
            .cacheOnDisk(true)
            .showImageOnFail(R.drawable.jia)
            .showImageOnLoading(R.drawable.jia)
            .showImageForEmptyUri(R.drawable.jia)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();


    //加载矩形大图的options
    private static final DisplayImageOptions rectangleoptionsdefault = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.jia)
            .showImageOnFail(R.drawable.jia)
            .showImageForEmptyUri(R.drawable.jia)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.ALPHA_8)
            .imageScaleType(ImageScaleType.EXACTLY)
            .resetViewBeforeLoading(true)
            .build();



    //demo半个圆角的options(左上边圆角)
    private static final DisplayImageOptions filletOptionsLeftTop = new DisplayImageOptions.Builder()
            .showStubImage(R.color.bg_b2b2b2)
            .showImageForEmptyUri(R.color.bg_b2b2b2)
            .showImageOnFail(R.color.bg_b2b2b2)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .bitmapConfig(Bitmap.Config.RGB_565)   //设置图片的解码类型
            .displayer(new FlexibleRoundedBitmapDisplayer(15, FlexibleRoundedBitmapDisplayer.CORNER_TOP_LEFT ))
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    //demo半个圆角的options(右上边圆角)
    private static final DisplayImageOptions filletOptionsRightTop = new DisplayImageOptions.Builder()
            .showStubImage(R.color.bg_b2b2b2)
            .showImageForEmptyUri(R.color.bg_b2b2b2)
            .showImageOnFail(R.color.bg_b2b2b2)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .bitmapConfig(Bitmap.Config.RGB_565)   //设置图片的解码类型
            .displayer(new FlexibleRoundedBitmapDisplayer(15, FlexibleRoundedBitmapDisplayer.CORNER_TOP_RIGHT ))
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    //demo半个圆角的options(左下边圆角)
    private static final DisplayImageOptions filletOptionsLeftBottom = new DisplayImageOptions.Builder()
            .showStubImage(R.color.bg_b2b2b2)
            .showImageForEmptyUri(R.color.bg_b2b2b2)
            .showImageOnFail(R.color.bg_b2b2b2)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .bitmapConfig(Bitmap.Config.RGB_565)   //设置图片的解码类型
            .displayer(new FlexibleRoundedBitmapDisplayer(15, FlexibleRoundedBitmapDisplayer.CORNER_BOTTOM_LEFT ))
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    //demo半个圆角的options(右下边圆角)
    private static final DisplayImageOptions filletOptionsRightBottom = new DisplayImageOptions.Builder()
            .showStubImage(R.color.bg_b2b2b2)
            .showImageForEmptyUri(R.color.bg_b2b2b2)
            .showImageOnFail(R.color.bg_b2b2b2)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .bitmapConfig(Bitmap.Config.RGB_565)   //设置图片的解码类型
            .displayer(new FlexibleRoundedBitmapDisplayer(15, FlexibleRoundedBitmapDisplayer.CORNER_BOTTOM_RIGHT ))
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    /**
     * 加载圆形图片
     *
     * @param imageUrl
     * @param imageView
     */
    public static void displayRoundImage(String imageUrl, ImageView imageView) {
        if (PublicUtils.ifHasWriteQuan1(MyApplication.getInstance())) {
            //有权限
            if (imageView != null)
                ImageLoader.getInstance().displayImage(imageUrl, imageView, roundOptions);
        } else {
            GlideImgManager.glideLoader(MyApplication.getInstance(), imageUrl, R.color.white, R.color.white, imageView, 0);
        }
    }

    /**
     * 加载圆角图片
     *
     * @param imageUrl
     * @param imageView
     */
    public static void displayFilletImage(String imageUrl, ImageView imageView) {
        if (PublicUtils.ifHasWriteQuan1(MyApplication.getInstance())) {
            //有权限
            if (imageView != null)
                ImageLoader.getInstance().displayImage(imageUrl, imageView, filletOptions);
        } else {
            GlideImgManager.glideLoader(MyApplication.getInstance(), imageUrl, R.color.white, R.color.white, imageView, 1);
        }
    }

    /**
     * 加载圆角图片
     *
     * @param imageUrl
     * @param imageView
     */
    public static void displayFilletHalfImage(String imageUrl, ImageView imageView) {
        if (PublicUtils.ifHasWriteQuan1(MyApplication.getInstance())) {
            //有权限
            if (imageView != null)
                ImageLoader.getInstance().displayImage(imageUrl, imageView, filletOptionsHalf);
        } else {
            GlideImgManager.glideLoader(MyApplication.getInstance(), imageUrl, R.color.white, R.color.white, imageView, 1);
        }
    }

    public static void displayFilletLeftTopImage(String imageUrl, ImageView imageView) {
        if (PublicUtils.ifHasWriteQuan1(MyApplication.getInstance())) {
            //有权限
            if (imageView != null)
                ImageLoader.getInstance().displayImage(imageUrl, imageView, filletOptionsLeftTop);
        } else {
            GlideImgManager.glideLoader(MyApplication.getInstance(), imageUrl, R.color.bg_b2b2b2, R.color.bg_b2b2b2, imageView, 1);
        }
    }

    public static void displayFilletRightTopImage(String imageUrl, ImageView imageView) {
        if (PublicUtils.ifHasWriteQuan1(MyApplication.getInstance())) {
            //有权限
            if (imageView != null)
                ImageLoader.getInstance().displayImage(imageUrl, imageView, filletOptionsRightTop);
        } else {
            GlideImgManager.glideLoader(MyApplication.getInstance(), imageUrl, R.color.bg_b2b2b2, R.color.bg_b2b2b2, imageView, 1);
        }
    }

    public static void displayFilletLeftBottomImage(String imageUrl, ImageView imageView) {
        if (PublicUtils.ifHasWriteQuan1(MyApplication.getInstance())) {
            //有权限
            if (imageView != null)
                ImageLoader.getInstance().displayImage(imageUrl, imageView, filletOptionsLeftBottom);
        } else {
            GlideImgManager.glideLoader(MyApplication.getInstance(), imageUrl, R.color.bg_b2b2b2, R.color.bg_b2b2b2, imageView, 1);
        }
    }

    public static void displayFilletRightBottomImage(String imageUrl, ImageView imageView) {
        if (PublicUtils.ifHasWriteQuan1(MyApplication.getInstance())) {
            //有权限
            if (imageView != null)
                ImageLoader.getInstance().displayImage(imageUrl, imageView, filletOptionsRightBottom);
        } else {
            GlideImgManager.glideLoader(MyApplication.getInstance(), imageUrl, R.color.bg_b2b2b2, R.color.bg_b2b2b2, imageView, 1);
        }
    }

    /**
     * 加载矩形图片
     *
     * @param imageUrl
     * @param imageView
     */
    public static void disRectangleImage(String imageUrl, ImageView imageView) {
        if (PublicUtils.ifHasWriteQuan1(MyApplication.getInstance())) {
            //有权限
            if (imageView != null)
                ImageLoader.getInstance().displayImage(imageUrl, imageView, rectangleoptions);
        } else {
            GlideImgManager.glideLoader(MyApplication.getInstance(), imageUrl, R.color.white, R.color.white, imageView);
        }
    }

    /**
     * 加载矩形图片
     *
     * @param imageUrl
     * @param imageView
     */
    public static void disRectangleImageHuoDong(String imageUrl, ImageView imageView) {
        if (imageView != null)
            ImageLoader.getInstance().displayImage(imageUrl, imageView, rectangleoptionshuodong);
    }

    /**
     * 加载矩形图片(加载中的背景为透明的)
     *
     * @param imageUrl
     * @param imageView
     */
    public static void disRectangleImageTou(String imageUrl, ImageView imageView) {
        if (PublicUtils.ifHasWriteQuan1(MyApplication.getInstance())) {
            //有权限
            if (imageView != null)
                ImageLoader.getInstance().displayImage(imageUrl, imageView, rectangleoptionstou);
        } else {
            GlideImgManager.glideLoader(MyApplication.getInstance(), imageUrl, R.color.white, R.color.white, imageView);
        }
    }

    /**
     * 某种情况加载的图片北京莫名其妙变成黑色，可以使用这种情况
     *
     * @param imageUrl
     * @param imageView
     */
    public static void disBlackImage(String imageUrl, ImageView imageView) {
        if (imageView != null)
            ImageLoader.getInstance().displayImage(imageUrl, imageView, blackOptions);
    }


    //.................................初始化ImageLoader.................................

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(DISPLAY_WIDTH, DISPLAY_HEIGHT) // default = device screen dimensions
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .diskCache(new UnlimitedDiskCache(createDefaultCacheDir())) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .build();
        ImageLoader.getInstance().init(config);
    }

    private static File createDefaultCacheDir() {
        File cache = new File(getCacheDir());
        if (!cache.exists()) {
            if (cache.mkdirs()) {
                Log.d("tag", "Cache files to create success");
            } else {
                Log.d("tag", "The cache file creation failed");
            }
        }
        return cache;
    }

    public static String getCacheDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "demo/" + IMAGE_CACHE;
    }

    private static final String IMAGE_CACHE = "image_cache";
    public static final int DISPLAY_WIDTH = 720;
    public static final int DISPLAY_HEIGHT = 1280;


    /**
     * 加载圆角图片
     *
     * @param imageUrl
     * @param imageView
     */
    public static void displayFilletDefaulImage(String imageUrl, ImageView imageView) {
        if (PublicUtils.ifHasWriteQuan1(MyApplication.getInstance())) {
            //有权限
            if (imageView != null)
                ImageLoader.getInstance().displayImage(imageUrl, imageView, filletOptionsDefaul);
        } else {
            GlideImgManager.glideLoader(MyApplication.getInstance(), imageUrl, R.drawable.jia, R.drawable.jia, imageView, 1);
        }
    }


    /**
     * 加载圆形图片
     *
     * @param imageUrl
     * @param imageView
     */
    public static void displayRoundDefaultImage(String imageUrl, ImageView imageView) {
        if (PublicUtils.ifHasWriteQuan1(MyApplication.getInstance())) {
            //有权限
            if (imageView != null)
                ImageLoader.getInstance().displayImage(imageUrl, imageView, roundOptionsDefaul);
        } else {
            GlideImgManager.glideLoader(MyApplication.getInstance(), imageUrl, R.drawable.jia, R.drawable.jia, imageView, 0);
        }
    }

    /**
     * 加载矩形图片
     *
     * @param imageUrl
     * @param imageView
     */
    public static void disRectangleDefaultImage(String imageUrl, ImageView imageView) {
        if (PublicUtils.ifHasWriteQuan1(MyApplication.getInstance())) {
            //有权限
            if (imageView != null)
                ImageLoader.getInstance().displayImage(imageUrl, imageView, rectangleoptionsdefault);
        } else {
            GlideImgManager.glideLoader(MyApplication.getInstance(), imageUrl, R.drawable.jia, R.drawable.jia, imageView);
        }
    }


}
