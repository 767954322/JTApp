package com.homechart.app.commont;

/**
 * Created by gumenghao on 17/6/11.
 */

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class ActivityManager {


    private List<Activity> activityList = new LinkedList<Activity>();
    private static ActivityManager instance;

    private ActivityManager() {
    }

    // 单例模式中获取唯一的MyApplication实例
    public static ActivityManager getInstance() {
        if (null == instance) {
            instance = new ActivityManager();
        }
        return instance;
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }
    // 删除单个Activity
    public void removeActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit(Activity curentActivity) {
        for (Activity activity : activityList) {
            if(activity != curentActivity){
                activity.finish();
            }
        }
    }

}