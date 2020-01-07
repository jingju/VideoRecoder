package com.jingju.videorecorder.utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * CommonUtils
 * Created by guangju on 2018/6/19.
 */
public class CommonUtils {


    public static Intent buildActionUri() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static void openActivity(Context context, Class<?> pClass) {
        openActivity(context,pClass,null);
    }
    public static void openActivity(Context context, Class<?> pClass, Bundle bundle) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            Intent intent = new Intent(activity, pClass);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            activity.startActivity(intent);
        } else {
            LogUtils.e("mContext not a activity instance");
        }
    }

}