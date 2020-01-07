package com.jingju.videorecorder.utils.permission;

/**
 * @author chends create on 2018/7/4.
 */
public interface PermissionCallback {
    int SUCCESS = 1, DENIED = 2, EXPLAIN = 3, NEVER_ASK_AGAIN = 4;

    void onPermission(int request, @PermissionState int state);
}
