package com.rock.drawboard;

import android.app.Application;
import android.content.Context;

/**
 * Created by 昌宜 on 2017/3/24.
 */

public class MyApplication extends Application{
    private static Context context;
    public static boolean isVisitor = false;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
    /**
     * 全局获取Context
     *
     * @return
     */
    public static Context getAppContext() {
        return context;
    }
}
