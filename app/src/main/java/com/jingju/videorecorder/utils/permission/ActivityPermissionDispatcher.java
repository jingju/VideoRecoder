package com.jingju.videorecorder.utils.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.SparseArray;

import androidx.collection.SimpleArrayMap;
import androidx.core.app.ActivityCompat;


import com.jingju.videorecorder.utils.Constant;
import com.jingju.videorecorder.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * Activity 权限请求
 */
public class ActivityPermissionDispatcher {
    private SimpleArrayMap<Activity, SparseArray<String>> permissionArray = new SimpleArrayMap<>();
    // 多个权限
    private SimpleArrayMap<Activity, SparseArray<String[]>> permissionsArray = new SimpleArrayMap<>();
    private SimpleArrayMap<Activity, PermissionCallback> callbackArray = new SimpleArrayMap<>();

    private static class Singleton {
        private static ActivityPermissionDispatcher mInstance = new ActivityPermissionDispatcher();
    }

    public static ActivityPermissionDispatcher getInstance() {
        return Singleton.mInstance;
    }

    private ActivityPermissionDispatcher() {
    }

    /**
     * 设置权限回调<br/>
     * 必须调用{@link #onRequestPermissionResult(Activity, int, int[])}
     * @param activity activity
     * @param callback 权限回调
     */
    public void setPermissionCallback(Activity activity, PermissionCallback callback) {
        callbackArray.put(activity, callback);
    }

    /**
     * SD卡读写权限
     * @param activity activity
     */
    public void checkedWithStorage(Activity activity) {
        checkedWithPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, Constant.PERMISSION_STORAGE_REQUEST_CODE);
    }

    /**
     * 录音权限
     * @param activity activity
     */
    public void checkedWithRecordAudio(Activity activity) {
        checkedWithPermission(activity, Manifest.permission.RECORD_AUDIO, Constant.PERMISSION_READ_AUDIO_REQUEST_CODE);
    }

    /**
     * camera 权限
     * @param activity activity
     */
    public void checkedWithCamera(Activity activity) {
        checkedWithPermission(activity, Manifest.permission.CAMERA, Constant.PERMISSION_CAMERA_REQUEST_CODE);
    }

    /**
     * 短信操作权限
     * @param activity activity
     */
    public void checkedWithSMS(Activity activity) {
        checkedWithPermission(activity, Manifest.permission.READ_SMS, Constant.PERMISSION_SMS_REQUEST_CODE);
    }

    /**
     * 电话权限
     * @param activity activity
     */
    public void checkedWithPhone(Activity activity) {
        checkedWithPermission(activity, Manifest.permission.READ_PHONE_STATE, Constant.PERMISSION_PHONE_REQUEST_CODE);
    }

    /**
     * 打电话权限
     * @param activity activity
     */
    public void checkedWithCallPhone(Activity activity) {
        checkedWithPermission(activity, Manifest.permission.CALL_PHONE, Constant.PERMISSION_CALL_PHONE_REQUEST_CODE);
    }

    /**
     * 联系人权限
     * @param activity activity
     */
    public void checkedWithContacts(Activity activity) {
        checkedWithPermission(activity, Manifest.permission.READ_CONTACTS, Constant.PERMISSION_CONTACTS_REQUEST_CODE);
    }

    /**
     * 位置权限
     * @param activity activity
     */
    public void checkedWithLocation(Activity activity) {
        checkedWithPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION, Constant.PERMISSION_LOCATION_REQUEST_CODE);
    }

    /**
     * 多个权限
     * @param activity    activity
     * @param permissions permissions
     * @param requestCode requestCode
     */
    public void checkedPermissions(Activity activity, String[] permissions, int requestCode) {
        checkedWithPermissions(activity, permissions, requestCode);
    }


    /**
     * 多个权限
     * @param activity    activity
     * @param permissions permissions
     * @param requestCode requestCode
     */
    public void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 请求存储卡权限
     * @param activity activity
     */
    public void requestStorage(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                Constant.PERMISSION_STORAGE_REQUEST_CODE);
    }

    /**
     * camera 权限
     * @param activity activity
     */
    public void requestCamera(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                Constant.PERMISSION_CAMERA_REQUEST_CODE);
    }

    /**
     * 请求audio权限
     * @param activity activity
     */
    public void requestAudio(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO},
                Constant.PERMISSION_READ_AUDIO_REQUEST_CODE);
    }

    /**
     * 请求read phone state权限
     * @param activity activity
     */
    public void requestPhoneState(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE},
                Constant.PERMISSION_PHONE_REQUEST_CODE);
    }

    /**
     * 请求call phone权限
     * @param activity activity
     */
    public void requestCallPhone(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE},
                Constant.PERMISSION_CALL_PHONE_REQUEST_CODE);
    }

    /**
     * 请求安装权限
     * @param activity activity
     */
    public void requestInstall(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES},
                    Constant.PERMISSION_INSTALL_REQUEST_CODE);
        }
    }

    /**
     * 权限处理
     * @param activity   activity
     * @param permission permission
     * @param request    权限请求码
     */
    public void checkedWithPermission(Activity activity, String permission, int request) {
        if (permissionArray.get(activity) == null) {
            SparseArray<String> array = new SparseArray<>();
            array.put(request, permission);
            permissionArray.put(activity, array);
        } else {
            permissionArray.get(activity).put(request, permission);
        }
        if (!PermissionUtil.checkPermission(activity, permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                if (callbackArray.get(activity) != null) {
                    callbackArray.get(activity).onPermission(request, PermissionCallback.EXPLAIN);
                }
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, request);
            }
        } else {
            try {
                if (callbackArray.get(activity) != null) {
                    callbackArray.get(activity).onPermission(request, PermissionCallback.SUCCESS);
                }
            } catch (Exception ex) {
                ToastUtils.showShort(activity, "请检查权限设置");
            }
        }
    }

    /**
     * 权限处理
     * @param activity    activity
     * @param permissions permissions
     * @param request     权限请求码
     */
    public void checkedWithPermissions(Activity activity, String[] permissions, int request) {
        if (permissionsArray.get(activity) == null) {
            SparseArray<String[]> array = new SparseArray<>();
            array.put(request, permissions);
            permissionsArray.put(activity, array);
        } else {
            permissionsArray.get(activity).put(request, permissions);
        }
        List<String> requests = new ArrayList<>();
        for (String permission : permissions) {
            if (!PermissionUtil.checkPermission(activity, permission)) {
                requests.add(permission);
            }
        }
        if (!requests.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissions, request);
        } else {
            try {
                if (callbackArray.get(activity) != null) {
                    callbackArray.get(activity).onPermission(request, PermissionCallback.SUCCESS);
                }
            } catch (Exception ex) {
                ToastUtils.showShort(activity, "请检查权限设置");
            }
        }
    }

    /**
     * 权限请求结果处理
     * @param activity     activity
     * @param requestCode  请求码
     * @param grantResults result
     */
    public void onRequestPermissionResult(Activity activity, int requestCode, int[] grantResults) {
        if (callbackArray.get(activity) != null) {
            if (verifyPermission(grantResults)) {
                callbackArray.get(activity).onPermission(requestCode, PermissionCallback.SUCCESS);
            } else {
                //denied
                if (permissionArray.get(activity) != null) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            permissionArray.get(activity).get(requestCode))) {
                        callbackArray.get(activity).onPermission(requestCode, PermissionCallback.NEVER_ASK_AGAIN);
                        return;
                    }
                } else if (permissionsArray.get(activity) != null) {
                    String[] permissions = permissionsArray.get(activity).get(requestCode);
                    if (permissions != null && permissions.length > 0) {
                        for (String permission : permissions) {
                            if (!PermissionUtil.checkPermission(activity, permission) &&
                                    !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                                callbackArray.get(activity).onPermission(requestCode, PermissionCallback.NEVER_ASK_AGAIN);
                                return;
                            }
                        }
                    }
                }
                callbackArray.get(activity).onPermission(requestCode, PermissionCallback.DENIED);
            }
        }
    }

    /**
     * 验证请求结果
     * @param grantResults result
     * @return true为通过
     */
    private boolean verifyPermission(int... grantResults) {
        if (grantResults.length == 0) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void clear(Activity activity) {
        permissionArray.remove(activity);
        callbackArray.remove(activity);
    }
}
