package com.jingju.videorecorder.utils;

import android.content.Context;

/**
 * @author chends create on 2018/8/23.
 */
public class FileProviderUtil {

    public static String getProvider(Context context){
        return context.getPackageName() + ".provider";
    }
}
