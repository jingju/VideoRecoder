package com.jingju.videorecorder.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.jingju.videorecorder.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author cds create on 2018/7/3.
 */
public class ContextUtil {

    /**
     * app版本号
     */
    private static int versionCode = 0;
    /**
     * app版本名
     */
    private static String versionName = "";

    /**
     * 应用未打开时或在后台时打开应用，否则返回
     * @param context context
     */
    public static void startApp(Context context) {
        if (!appIsInTop(context)) {
            // 应用在后台，或没有打开应用
            foregroundApp(context);
        }
        // 应用在前台
    }

    public static void foregroundApp(Context context) {
        Intent launch;
        if ((launch = getLaunch(context)) != null) {
            context.startActivity(launch);
        }
    }

    @Nullable
    public static Intent getLaunch(Context context) {
        Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (LaunchIntent != null) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(LaunchIntent.getComponent());
            return intent;
        }
        return null;
    }

    /**
     * 应用是否在运行
     * @param context context
     * @return true（正在运行）or false（没有运行）
     */
    public static boolean appIsRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        if (manager == null) return false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(Integer.MAX_VALUE);
            if (runningTasks != null) {
                for (ActivityManager.RunningTaskInfo taskInfo : runningTasks) {
                    if (TextUtils.equals(taskInfo.topActivity.getPackageName(), context.getPackageName())) {
                        return true;
                    }
                }
            }
        } else {
            Set<String> activePackages = getActivePackages(manager, false);
            for (String activePackage : activePackages) {
                if (activePackage.equals(context.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 应用是否在前台
     * @param context context
     * @return 是否在前台
     */
    public static boolean appIsInTop(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        if (manager == null) return false;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
            if (runningTasks != null && runningTasks.get(0) != null && runningTasks.get(0).topActivity != null) {
                return TextUtils.equals(runningTasks.get(0).topActivity.getPackageName(), context.getPackageName());
            }
        } else {
            Set<String> activePackages = getActivePackages(manager, true);
            for (String activePackage : activePackages) {
                if (TextUtils.equals(activePackage, context.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 当前activity是否在前台
     * @param context context
     * @return 是否在前台
     */
    public static boolean activityIsInTop(Context context, Class<?> activityClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        if (manager == null) return false;
        List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
        return runningTasks != null && TextUtils.equals(runningTasks.get(0).topActivity.getClassName(),
                activityClass.getName());
    }

    /**
     * android5.0以上获取运行的应用
     * @param manager      ActivityManager
     * @param isForeground 是否前台
     * @return 集合
     */
    private static Set<String> getActivePackages(ActivityManager manager, boolean isForeground) {
        Set<String> activePackages = new HashSet<>();
        List<ActivityManager.RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfoList) {
            if (isForeground) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    activePackages.addAll(Arrays.asList(processInfo.pkgList));
                }
            } else {
                activePackages.addAll(Arrays.asList(processInfo.pkgList));
            }
        }
        return activePackages;
    }

    /**
     * 结束notification
     * @param context         context
     * @param isAll           是否全部
     * @param notificationIds isALl为false时结束当前id集合的所有id
     */
    public static void cancelNotification(Context context, boolean isAll, int... notificationIds) {
        try {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager == null) return;
            if (isAll) {
                manager.cancelAll();
            } else {
                if (notificationIds != null && notificationIds.length > 0) {
                    for (int notificationId : notificationIds) {
                        manager.cancel(notificationId);
                    }
                }
            }
        } catch (Exception ex) {
            LogUtils.e("cancelNotification", "error:" + ex);
        }
    }

    /**
     * 获取默认SharedPreferences
     * @param context context
     * @return SharedPreferences
     */
    public static SharedPreferences getDefaultSp(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 获取当前进程名称
     * @param context context
     * @return processName
     */
    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();//获取进程pid
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);//获取系统的ActivityManager服务
        if (am == null) return "";
        for (ActivityManager.RunningAppProcessInfo appProcess : am.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processName = appProcess.processName;
                break;
            }
        }
        return processName;
    }

    /**
     * 获取进程号对应的进程名
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable t) {
            LogUtils.e(t);
            //t.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                LogUtils.e(e);
                //e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取VersionName,VersionCode
     * @param context context
     */
    private static void getAppVersionInfo(Context context) {
        String name = "";
        int code = -1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            name = pi.versionName;
            code = pi.versionCode;
            if (name == null || name.length() <= 0) {
                name = "";
            }
        } catch (Exception e) {
            LogUtils.e("VersionInfo", "Exception:" + e.getMessage());
        }
        versionName = name;
        versionCode = code;
    }

    /**
     * 获取app的版本号
     * @param context context
     * @return versionCode
     */
    public static int getAppVersionCode(Context context) {
        if (versionCode <= 0) {
            getAppVersionInfo(context);
        }
        return versionCode;
    }

    /**
     * 获取app的版本名
     * @param context context
     * @return versionCode
     */
    public static String getAppVersionName(Context context) {
        if (TextUtils.isEmpty(versionName)) {
            getAppVersionInfo(context);
        }
        return versionName;
    }

    public static void setOverScroll(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int glowDrawableId = context.getResources().getIdentifier("overscroll_glow", "drawable", "android");
            Drawable androidGlow = ContextCompat.getDrawable(context, glowDrawableId);
            if (androidGlow != null) {
                androidGlow.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
            int edgeDrawableId = context.getResources().getIdentifier("overscroll_edge", "drawable", "android");
            Drawable androidEdge = ContextCompat.getDrawable(context, edgeDrawableId);
            if (androidEdge != null) {
                androidEdge.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public static boolean isAlive(Object object) {
        if (object == null) return false;
        if (object instanceof Activity) {
            return isAlive((Activity) object);
        } else if (object instanceof Fragment) {
            return isAlive((Fragment) object);
        } else if (object instanceof Service) {
            return isAlive((Service) object);
        } else if (object instanceof View){
            return isAlive(((View) object).getContext());
        } else if (object instanceof Context){
            try{
                return isAlive(getActivity((Context)object));
            } catch (Exception ignore){}
        }
        return true;
    }

    public static boolean isAlive(Activity activity) {
        return activity != null && !activity.isFinishing() && !activity.isDestroyed();
    }

    public static boolean isAlive(Fragment fragment) {
        return fragment != null && isAlive(fragment.getActivity()) && fragment.isAdded() && !fragment.isDetached();
    }

    public static boolean isAlive(Service service) {
        return isAlive(service, service.getClass().getName());
    }

    public static boolean isAlive(Context context, String serviceClassName) {
        if (context != null) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (manager == null) return false;
            List<ActivityManager.RunningServiceInfo> services = manager.getRunningServices(Integer.MAX_VALUE);
            if (services != null) {
                for (ActivityManager.RunningServiceInfo s : services) {
                    if (TextUtils.equals(serviceClassName, s.service.getClassName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void settingPermissionActivity(Activity activity, int requestCode) {
        try {
            Intent intent;
            //判断是否为小米系统
            if (TextUtils.equals(BrandUtils.getSystemInfo().getOs(), BrandUtils.SYS_MIUI)) {
                intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent.putExtra("extra_pkgname", activity.getPackageName());
                //检测是否有能接受该Intent的Activity存在
                List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                if (list.size() > 0) {
                    activity.startActivityForResult(intent, requestCode);
                    return;
                }
            }
            //如果不是小米系统 则打开Android系统的应用设置页
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                activity.startActivityForResult(intent, requestCode);
                return;
            }
            intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception ex) {
            ToastUtils.showShort(activity, "无法打开设置页");
        }
    }

    public static void openSdcardSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 显示dialog
     * @param dialog  dialog
     * @param context context
     */
    public static void safeShowDialog(Dialog dialog, Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!ContextUtil.isAlive(activity)) return;
            if (dialog == null || dialog.isShowing()) return;
            dialog.show();
        }
    }

    /**
     * dialog消失
     * @param dialog  dialog
     * @param context context
     */
    public static void safeDismissDialog(Dialog dialog, Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!ContextUtil.isAlive(activity)) return;
            if (dialog == null || !dialog.isShowing()) return;
            dialog.dismiss();
        }
    }

    /**
     * 显示键盘
     * @param view view
     */
    public static void showKeyboard(View view) {
        try {
            view.requestFocus();
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            // 显示输入法
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        } catch (Exception e) {
            LogUtils.e("显示键盘失败", "Exception:" + e.getMessage());
        }
    }

    /**
     * 隐藏输入法
     * @param activity activity
     */
    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getApplicationContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            //隐藏输入法
            if (imm != null && activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        } catch (Exception e) {
            LogUtils.e("隐藏输入法失败", "Exception:" + e.getMessage());
        }
    }

    /**
     * 显示键盘
     * @param context context
     * @param dialog  dialog
     */
    public static void showKeyboard(Context context, Dialog dialog) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getApplicationContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            //隐藏输入法
            if (imm != null && dialog.getCurrentFocus() != null) {
                imm.showSoftInput(dialog.getCurrentFocus(), InputMethodManager.SHOW_FORCED);
            }
        } catch (Exception e) {
            LogUtils.e("显示键盘失败", "Exception:" + e.getMessage());
        }
    }

    /**
     * 隐藏输入法
     * @param context context
     * @param dialog  dialog
     */
    public static void hideKeyboard(Context context, Dialog dialog) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getApplicationContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            //隐藏输入法
            if (imm != null && dialog.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        } catch (Exception e) {
            LogUtils.e("隐藏输入法失败", "Exception:" + e.getMessage());
        }
    }

    /**
     * context 转为Activity
     * @param context context
     */
    public static Activity getActivity(Context context) {
        Context in = context;
        if (in instanceof ContextWrapper) {
            while (in instanceof ContextWrapper) {
                if (in instanceof Activity) {
                    return (Activity) in;
                }
                in = ((ContextWrapper) in).getBaseContext();
            }
        }
        throw new IllegalStateException("The context is not an Activity.");
    }

}
