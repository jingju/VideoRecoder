package com.jingju.videorecorder.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * EncryptUtil
 * @author cds create on 2018/6/23.
 */
public class EncryptUtil {
    private static final int UrlSafe_flags = Base64.URL_SAFE | Base64.NO_WRAP;

    /**
     * MD5加密
     * @param string String
     * @return byte[]
     */
    public static byte[] MD5(String string) {
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        byte[] result = null;
        try {
            result = MD5(string.getBytes(Constant.UTF8));
        } catch (Exception ex) {
            LogUtils.d("MD5 error:" + ex.getMessage());
        }
        return result;
    }

    /**
     * MD5加密
     * @param bytes bytes
     * @return byte[]
     */
    public static byte[] MD5(byte[] bytes) {
        byte[] result = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            result = md5.digest(bytes);
        } catch (NoSuchAlgorithmException ex) {
            LogUtils.d("MD5 error:" + ex.getMessage());
        }
        return result;
    }

    /**
     * base64 decode
     * @param string string
     * @return string
     */
    public static byte[] Base64Decode(String string) {
        try {
            return Base64.decode(string, Base64.DEFAULT);
        } catch (Exception ex) {
            //  无法转换
        }
        return null;
    }

    /**
     * base64 decode
     * @param string string
     * @return string
     */
    public static String Base64DecodeToString(String string) {
        try {
            return new String(Base64.decode(string, Base64.DEFAULT), Constant.UTF8);
        } catch (Exception ex) {
            //  无法转换
        }
        return string;
    }

    /**
     * base64 encode
     * @param bytes bytes
     * @return str
     */
    public static String Base64EncodeToString(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    /**
     * base64 encode
     * @param bytes bytes
     * @return byte[]
     */
    public static byte[] Base64Encode(byte[] bytes) {
        return Base64.encode(bytes, Base64.NO_WRAP);
    }

    /**
     * base64 urlSafe encode
     * @param string string
     * @return string
     */
    public String Base64UrlSafeEncodeToString(String string) {
        try {
            return Base64.encodeToString(string.getBytes(Constant.UTF8), UrlSafe_flags);
        } catch (Exception e) {
            throw new IllegalStateException("UTF-8 not supported", e);
        }
    }

    /**
     * base64 urlSafe decode
     * @param string string
     * @return string
     */
    public String Base64UrlSafeDecode(String string) {
        byte[] bytes = Base64.decode(string, UrlSafe_flags);
        try {
            return new String(bytes, Constant.UTF8);
        } catch (Exception e) {
            throw new IllegalStateException("UTF-8 not supported", e);
        }
    }

    private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] HEX_LOWER_CASE = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * md5加密返回16/32,小写/大写字符串
     * @param string string
     * @return string
     */
    public static String MD5String(String string, boolean is16Bit, boolean isLowerCase) {
        if (TextUtils.isEmpty(string)) {
            return string;
        }
        try {
            int i = 0;
            byte[] buffer = string.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            while (i < j) {
                byte byte0 = md[i];
                if (isLowerCase) {
                    str[k++] = HEX_LOWER_CASE[byte0 >>> 4 & 0xf];
                    str[k++] = HEX_LOWER_CASE[byte0 & 0xf];
                } else {
                    str[k++] = HEX[byte0 >>> 4 & 0xf];
                    str[k++] = HEX[byte0 & 0xf];
                }
                i++;
            }
            String result = new String(str);
            if (is16Bit) {
                return result.substring(8, 24);
            } else {
                return result;
            }
        } catch (Exception e) {
            return string;
        }
    }
}
