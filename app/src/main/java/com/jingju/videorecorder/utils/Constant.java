package com.jingju.videorecorder.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Constant
 */
public class Constant {
    private static final int BASIC_NUMBER = 0x0A000;
    /**
     * 公司目录
     */
    private static final String COMPANY_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "jingju";
    private static final String APP_DIR = COMPANY_DIR + File.separator + "FFmpegPrac";

    public static final int PERMISSION_STORAGE_REQUEST_CODE = BASIC_NUMBER + 1;
    public static final int PERMISSION_CAMERA_REQUEST_CODE = BASIC_NUMBER + 2;
    public static final int PERMISSION_SENSORS_REQUEST_CODE = BASIC_NUMBER + 3;
    public static final int PERMISSION_PHONE_REQUEST_CODE = BASIC_NUMBER + 4;
    public static final int PERMISSION_SMS_REQUEST_CODE = BASIC_NUMBER + 5;
    public static final int PERMISSION_CONTACTS_REQUEST_CODE = BASIC_NUMBER + 6;
    public static final int PERMISSION_CALENDAR_REQUEST_CODE = BASIC_NUMBER + 7;
    public static final int PERMISSION_LOCATION_REQUEST_CODE = BASIC_NUMBER + 8;
    public static final int PERMISSION_MICROPHONE_REQUEST_CODE = BASIC_NUMBER + 9;
    public static final int PERMISSION_READ_STORAGE_REQUEST_CODE = BASIC_NUMBER + 10;
    public static final int PERMISSION_READ_AUDIO_REQUEST_CODE = BASIC_NUMBER + 11;
    public static final int PERMISSION_INSTALL_REQUEST_CODE = BASIC_NUMBER + 12;
    public static final int PERMISSION_CALL_PHONE_REQUEST_CODE = BASIC_NUMBER + 13;
    public static final int PERMISSIONS_REQUEST_CODE = BASIC_NUMBER + 14;


    public static final Charset UTF8 = Charset.forName("UTF-8");
    public static String getAppDir(){
        Log.d("appdir",APP_DIR);
        return APP_DIR;
    }


}
