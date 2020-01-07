package com.jingju.videorecorder.utils.permission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.SparseArray;

import androidx.collection.SimpleArrayMap;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.jingju.videorecorder.utils.Constant;
import com.jingju.videorecorder.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment权限请求
 */
public class FragmentPermissionDispatcher {
    private SimpleArrayMap<Fragment, SparseArray<String>> permissionArray = new SimpleArrayMap<>();
    private SimpleArrayMap<Fragment, SparseArray<String[]>> permissionsArray = new SimpleArrayMap<>();
    private SimpleArrayMap<Fragment, PermissionCallback> callbackArray = new SimpleArrayMap<>();

    private static class Singleton {
        private static FragmentPermissionDispatcher mInstance = new FragmentPermissionDispatcher();
    }

    public static FragmentPermissionDispatcher getInstance() {
        return Singleton.mInstance;
    }

    private FragmentPermissionDispatcher() {
    }

    /**
     * 设置权限回调
     * 必须调用{@link #onRequestPermissionResult(Fragment, int, int[])}
     * @param fragment fragment
     * @param callback 权限回调
     */
    public void setPermissionCallback(Fragment fragment, PermissionCallback callback) {
        callbackArray.put(fragment, callback);
    }


    /**
     * SD卡读写权限
     * @param fragment fragment
     */
    public void checkedWithStorage(Fragment fragment) {
        checkedWithPermission(fragment, Manifest.permission.WRITE_EXTERNAL_STORAGE, Constant.PERMISSION_STORAGE_REQUEST_CODE);
    }

    /**
     * camera 权限
     * @param fragment fragment
     */
    public void checkedWithCamera(Fragment fragment) {
        checkedWithPermission(fragment, Manifest.permission.CAMERA, Constant.PERMISSION_CAMERA_REQUEST_CODE);
    }

    /**
     * 短信操作权限
     * @param fragment fragment
     */
    public void checkedWithSMS(Fragment fragment) {
        checkedWithPermission(fragment, Manifest.permission.READ_SMS, Constant.PERMISSION_SMS_REQUEST_CODE);
    }

    /**
     * 电话权限
     * @param fragment fragment
     */
    public void checkedWithPhone(Fragment fragment) {
        checkedWithPermission(fragment, Manifest.permission.READ_PHONE_STATE, Constant.PERMISSION_PHONE_REQUEST_CODE);
    }

    /**
     * 联系人权限
     * @param fragment fragment
     */
    public void checkedWithContacts(Fragment fragment) {
        checkedWithPermission(fragment, Manifest.permission.READ_CONTACTS, Constant.PERMISSION_CONTACTS_REQUEST_CODE);
    }

    /**
     * 位置权限
     * @param fragment fragment
     */
    public void checkedWithLocation(Fragment fragment) {
        checkedWithPermission(fragment, Manifest.permission.ACCESS_FINE_LOCATION, Constant.PERMISSION_LOCATION_REQUEST_CODE);
    }

    /**
     * 打电话权限
     * @param fragment fragment
     */
    public void checkedWithCallPhone(Fragment fragment) {
        checkedWithPermission(fragment, Manifest.permission.CALL_PHONE, Constant.PERMISSION_CALL_PHONE_REQUEST_CODE);
    }

    /**
     * 多个权限
     * @param fragment    fragment
     * @param permissions permissions
     * @param requestCode requestCode
     */
    public void checkedPermissions(Fragment fragment, String[] permissions, int requestCode) {
        checkedWithPermissions(fragment, permissions, requestCode);
    }


    /**
     * 多个权限
     * @param fragment    fragment
     * @param permissions permissions
     * @param requestCode requestCode
     */
    public void requestPermissions(Fragment fragment, String[] permissions, int requestCode) {
        fragment.requestPermissions(permissions, requestCode);
    }

    /**
     * 请求存储卡权限
     * @param fragment fragment
     */
    public void requestStorage(Fragment fragment){
        fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                Constant.PERMISSION_STORAGE_REQUEST_CODE);
    }

    /**
     * camera 权限
     * @param fragment fragment
     */
    public void requestCamera(Fragment fragment) {
        fragment.requestPermissions(new String[]{Manifest.permission.CAMERA},
                Constant.PERMISSION_CAMERA_REQUEST_CODE);
    }
    /**
     * 录音权限
     * @param  fragment fragment
     */
    public void checkedWithRecordAudio(Fragment fragment) {
        checkedWithPermission(fragment, Manifest.permission.RECORD_AUDIO, Constant.PERMISSION_READ_AUDIO_REQUEST_CODE);
    }
    /**
     * 权限处理
     * @param fragment   fragment
     * @param permission Manifest.permission.
     * @param request    权限请求码
     */
    public void checkedWithPermission(Fragment fragment, String permission, int request) {
        if (permissionArray.get(fragment) == null) {
            SparseArray<String> array = new SparseArray<>();
            array.put(request, permission);
            permissionArray.put(fragment, array);
        } else {
            permissionArray.get(fragment).put(request, permission);
        }
        if (!PermissionUtil.checkPermission(fragment.getActivity(), permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), permission)) {
                if (callbackArray.get(fragment) != null) {
                    callbackArray.get(fragment).onPermission(request, PermissionCallback.EXPLAIN);
                }
            } else {
                fragment.requestPermissions(new String[]{permission}, request);
            }
        } else {
            try {
                if (callbackArray.get(fragment) != null) {
                    callbackArray.get(fragment).onPermission(request, PermissionCallback.SUCCESS);
                }
            } catch (Exception ex) {
                ToastUtils.showShort(fragment.getActivity(), "请检查权限设置");
            }
        }
    }

    /**
     * 权限处理
     * @param fragment    fragment
     * @param permissions permissions
     * @param request     权限请求码
     */
    public void checkedWithPermissions(Fragment fragment, String[] permissions, int request) {
        if (permissionsArray.get(fragment) == null) {
            SparseArray<String[]> array = new SparseArray<>();
            array.put(request, permissions);
            permissionsArray.put(fragment, array);
        } else {
            permissionsArray.get(fragment).put(request, permissions);
        }
        List<String> requests = new ArrayList<>();
        for (String permission : permissions) {
            if (!PermissionUtil.checkPermission(fragment.getActivity(), permission)) {
                requests.add(permission);
            }
        }
        if (!requests.isEmpty()) {
            fragment.requestPermissions(permissions, request);
        } else {
            try {
                if (callbackArray.get(fragment) != null) {
                    callbackArray.get(fragment).onPermission(request, PermissionCallback.SUCCESS);
                }
            } catch (Exception ex) {
                ToastUtils.showShort(fragment.getActivity(), "请检查权限设置");
            }
        }
    }

    /**
     * 权限请求结果处理
     * @param fragment     fragment
     * @param requestCode  请求码
     * @param grantResults result
     */
    public void onRequestPermissionResult(Fragment fragment, int requestCode, int[] grantResults) {
        if (callbackArray.get(fragment) != null) {
            if (verifyPermission(grantResults)) {
                callbackArray.get(fragment).onPermission(requestCode, PermissionCallback.SUCCESS);
            } else {
                //denied
                if (permissionArray.get(fragment) != null) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(),
                            permissionArray.get(fragment).get(requestCode))) {
                        callbackArray.get(fragment).onPermission(requestCode, PermissionCallback.NEVER_ASK_AGAIN);
                        return;
                    }
                } else if (permissionsArray.get(fragment) != null) {
                    String[] permissions = permissionsArray.get(fragment).get(requestCode);
                    if (permissions != null && permissions.length > 0) {
                        for (String permission : permissions) {
                            if (!PermissionUtil.checkPermission(fragment.getActivity(), permission) &&
                                    !ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), permission)) {
                                callbackArray.get(fragment).onPermission(requestCode, PermissionCallback.NEVER_ASK_AGAIN);
                                return;
                            }
                        }
                    }
                }
                callbackArray.get(fragment).onPermission(requestCode, PermissionCallback.DENIED);
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

    public void clear(Fragment fragment) {
        permissionArray.remove(fragment);
        callbackArray.remove(fragment);
    }

}
