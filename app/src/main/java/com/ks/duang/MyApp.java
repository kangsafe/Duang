package com.ks.duang;

import android.app.Application;
import android.content.Context;

/**
 * Created by Admin on 2018/6/18 0018 13:22.
 * Author: kang
 * Email: kangsafe@163.com
 */
public class MyApp extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
    }

    public static Context getContext(){
        return mContext;
    }
}
