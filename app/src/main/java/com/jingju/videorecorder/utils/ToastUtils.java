package com.jingju.videorecorder.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ToastUtils
 */
public class ToastUtils {

    private ToastUtils() {
        throw new AssertionError();
    }

    public static boolean isShow = true;
    private static Toast toast;

    @SuppressLint("ShowToast")
    private static Toast make(Context context, CharSequence str, int duration) {
        if (context != null) {
            Toast toast = Toast.makeText(context, "", duration);
            toast.setText(str);
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                hookN(toast);
            }
            if (!isNotificationEnabled(context)) {
                hook(toast);
            }
            return toast;
        }
        return null;
    }

    /**
     * 是否有通知权限
     * @param context context
     * @return true
     */
    private static boolean isNotificationEnabled(Context context) {
        return context != null && NotificationManagerCompat.from(context).areNotificationsEnabled();
    }

    /**
     * showShort
     * @param message message
     */
    public static void showShort(final CharSequence message) {
        showShort(BaseContext.getContext(), message);
    }

    /**
     * showShort
     * @param context context
     * @param message message
     */
    public static void showShort(Context context, CharSequence message) {
        if (context == null) return;
        realShowShort(context.getApplicationContext(), message);
    }

    /**
     * realShowShort
     * @param context context
     * @param message message
     */
    private static void realShowShort(final Context context, final CharSequence message) {
        if (context == null) return;
        if (TextUtils.isEmpty(message)) return;
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            ThreadUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    realShow(context,message,Toast.LENGTH_SHORT);
                }
            });
        } else {
            realShow(context, message, Toast.LENGTH_SHORT);
        }
    }

    /**
     * realShow
     * @param context context
     * @param message message
     */
    private static void realShow(@NonNull Context context, CharSequence message, int duration) {
        if (isShow && !TextUtils.isEmpty(message)) {
            try {
                if (toast != null) {
                    toast.cancel();
                }
                toast = make(context, message, duration);
                if (toast != null) {
                    toast.show();
                }
            } catch (Exception ex) {
                LogUtils.e("Toast select error:" + ex.getMessage());
            }
        }
    }

    /**
     * showLong
     * @param message message
     */
    public static void showLong(final CharSequence message) {
        if (TextUtils.isEmpty(message)) return;
        showLong(BaseContext.getContext(), message);
    }

    /**
     * showLong
     * @param context context
     * @param message message
     */
    public static void showLong(Context context, CharSequence message) {
        if (context == null) return;
        if (TextUtils.isEmpty(message)) return;
        realShowLong(context.getApplicationContext(), message);
    }
    /**
     * realShowLong
     * @param context context
     * @param message message
     */
    private static void realShowLong(final Context context, final CharSequence message) {
        if (context == null) return;
        if (TextUtils.isEmpty(message)) return;
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            ThreadUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    realShow(context, message, Toast.LENGTH_LONG);
                }
            });
        } else {
            realShow(context, message, Toast.LENGTH_LONG);
        }
    }

    private static Object iNotificationManagerObj;

    /**
     * 无通知权限hook
     * @param toast toast
     */
    private static void hook(Toast toast) {
        try {
            //hook INotificationManager
            if (iNotificationManagerObj == null) {
                Method getServiceMethod = Toast.class.getDeclaredMethod("getService");
                getServiceMethod.setAccessible(true);
                iNotificationManagerObj = getServiceMethod.invoke(null);

                Class iNotificationManagerCls = Class.forName("android.app.INotificationManager");
                Object iNotificationManagerProxy = Proxy.newProxyInstance(toast.getClass().getClassLoader(), new Class[]{iNotificationManagerCls}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //强制使用系统Toast
                        if ("enqueueToast".equals(method.getName())) {
                            args[0] = "android";
                        }
                        return method.invoke(iNotificationManagerObj, args);
                    }
                });
                Field sServiceFiled = Toast.class.getDeclaredField("sService");
                sServiceFiled.setAccessible(true);
                sServiceFiled.set(null, iNotificationManagerProxy);
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    /**
     * android N hook
     * @param toast toast
     */
    private static void hookN(Toast toast) {
        // Hook toast field
        try {
            Field field_tn = Toast.class.getDeclaredField("mTN");
            field_tn.setAccessible(true);

            Object mTN = field_tn.get(toast);
            Field field_handler = field_tn.getType().getDeclaredField("mHandler");
            field_handler.setAccessible(true);

            Handler handler = (Handler) field_handler.get(mTN);
            field_handler.set(mTN, new SafeHandler(handler)); // 偷梁换柱
        } catch (Exception ignored) {
        }
    }

    static final class SafeHandler extends Handler {

        private Handler mHandler;

        SafeHandler(Handler handler) {
            mHandler = handler;
        }

        @Override
        public void handleMessage(Message msg) {
            // 捕获这个异常，避免程序崩溃
            try {
                /*
                 目前发现 Android 7.1 主线程被阻塞之后弹吐司会导致崩溃
                 查看源码得知 Google 已经在 8.0 已经修复了此问题
                 因为主线程阻塞之后 Toast 也会被阻塞
                 Toast 超时 Window token 会失效
                 可使用 Thread.sleep(5000) 进行复现
                 */
                mHandler.handleMessage(msg);
            } catch (WindowManager.BadTokenException ignored) {
                // android.view.WindowManager$BadTokenException:
                // Unable to add window -- token android.os.BinderProxy@94ae84f is not valid; is your activity running?
            }
        }
    }
}


