package com.zyf.device;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import java.util.Stack;


public class MyApplication extends MultiDexApplication {

    private static MyApplication sInstance;
    public Stack<Activity> activities = new Stack<>();
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        context = getApplicationContext();
        MultiDex.install(context);
    }

    public static Context getContext() {
        return context;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public void finishAllActivites() {
        for (Activity activity : activities) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }

        activities.clear();
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getMyApplicationContext() {
        return context;
    }

    public void exit(boolean exitApp) {
        finishAllActivites();
        if (exitApp) {
            //这种方式退出应用，会结束本应用程序的一切活动,因为本方法会根据应用程序的包名杀死所有进程包括Activity,Service,Notifications等。
            ActivityManager am = (ActivityManager) getApplicationContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);
            am.killBackgroundProcesses(getApplicationContext().getPackageName());
            System.exit(0);//等同于Runtime.getRuntime().exit(n)
        }

    }
}
