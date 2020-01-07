package com.jingju.videorecorder.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class BaseContext {
    @SuppressLint("StaticFieldLeak")
    private static final BaseContext INSTANCE = new BaseContext();

    private BaseContext() {
    }

    private Application application;
    private Context mContext;
    private boolean isAppInBackground = false;

    public static void setApplication(Application app) {
        INSTANCE.application = app;
    }

    public static void setContext(Context con) {
        INSTANCE.mContext = con;
    }

    public static Application getApplication() {
        return INSTANCE.application;
    }

    public static Context getContext() {
        return INSTANCE.mContext;
    }

    public static void isBackground(boolean background){
        INSTANCE.isAppInBackground = background;
    }
}

