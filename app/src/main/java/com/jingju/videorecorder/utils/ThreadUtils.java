package com.jingju.videorecorder.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 线程工具类
 * @author on 2018/7/20.
 */
public class ThreadUtils {
    private static final Handler sHandler = new Handler(Looper.getMainLooper());
    private static final Executor sExecutor = Executors.newSingleThreadExecutor();

    public static void runOnSubThread(Runnable runnable) {
        sExecutor.execute(runnable);
    }

    public static void runOnUIThread(Runnable runnable) {
        sHandler.post(runnable);
    }

    public static void runOnUIThreadDelay(Runnable runnable, long delay) {
        sHandler.postDelayed(runnable, delay);
    }

    public static void removeCallbacks(Runnable runnable) {
        sHandler.removeCallbacks(runnable);
    }
}
