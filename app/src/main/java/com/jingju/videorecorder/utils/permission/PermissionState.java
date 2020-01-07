package com.jingju.videorecorder.utils.permission;

/**
 * @author chends create on 2018/7/4.
 */


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({PermissionCallback.SUCCESS, PermissionCallback.DENIED,
        PermissionCallback.EXPLAIN,PermissionCallback.NEVER_ASK_AGAIN})
@Retention(RetentionPolicy.SOURCE)
public @interface PermissionState {
}
