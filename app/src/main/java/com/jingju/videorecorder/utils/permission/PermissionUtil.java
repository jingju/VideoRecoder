package com.jingju.videorecorder.utils.permission;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;

import androidx.core.app.ActivityCompat;
import androidx.core.app.AppOpsManagerCompat;
import androidx.core.content.PermissionChecker;

import com.jingju.videorecorder.utils.LogUtils;
import com.jingju.videorecorder.utils.OSUtils;


/**
 * 判定权限,在小米手机上的特殊处理
 * @author chends create on 2018/8/6.
 */
public class PermissionUtil {

    public static boolean checkPermission(Context context, String permission) {
        int targetSdkVersion = getTargetVersion(context);
        boolean result = false;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (targetSdkVersion >= Build.VERSION_CODES.M) {
                    result = ActivityCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED;
                } else {
                    result = PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED;
                }
            } else {
                result = true;
            }
        }catch (Exception e){
            LogUtils.e(e);
        }
        return result;
    }

    private static int version = 0;
    private static int getTargetVersion(Context context) {
        if (version == 0) {
            try {
                final PackageInfo info = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), 0);
                version = info.applicationInfo.targetSdkVersion;
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
        return version;
    }

    public static boolean checkOp(Context context, String permission) {
        boolean haOp = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && OSUtils.isMiUi()) {
            String permissionToOp = AppOpsManagerCompat.permissionToOp(permission);
            if (permissionToOp == null) {
                haOp = true;
            } else {
                int noteOp = AppOpsManagerCompat.noteOp(context, permissionToOp, Process.myUid(), context.getPackageName());
                haOp = noteOp == AppOpsManagerCompat.MODE_ALLOWED;
            }
        }
        return haOp;
    }
}
