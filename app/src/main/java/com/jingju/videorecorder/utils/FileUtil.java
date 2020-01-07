package com.jingju.videorecorder.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.UUID;

/**
 * FileUtil
 */
public class FileUtil {
    public static String mCacheDirPath = "";

    public static String getCacheDir() {
        if (!TextUtils.isEmpty(mCacheDirPath)) {
            return mCacheDirPath;
        }
        String cacheDirPath = "";
        Context context = BaseContext.getContext();
        if (context == null) {
            return "";
        }
        File externalCacheDir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            externalCacheDir = context.getExternalCacheDir();
        } else {
            externalCacheDir = null;
        }
        if (externalCacheDir != null) {
            cacheDirPath = externalCacheDir.getAbsolutePath();
        } else {
            cacheDirPath = context.getCacheDir().getAbsolutePath();
        }
        mCacheDirPath = cacheDirPath;
        return cacheDirPath;
    }

    public static String getFilePath(String parentDir, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            LogUtils.i("FileUtil", "fileName is empty");
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        String cacheDirPath = getCacheDir();
        if (TextUtils.isEmpty(cacheDirPath)) {
            return null;
        }
        stringBuilder.append(cacheDirPath);
        if (!TextUtils.isEmpty(parentDir)) {
            stringBuilder.append(File.separator).append(parentDir);
        }
        stringBuilder.append(File.separator).append(fileName);
        return stringBuilder.toString();
    }

    @Nullable
    public static String getUpdateApkPath(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            LogUtils.i("FileUtil", "fileName is empty");
            return null;
        }
        Context context = BaseContext.getContext();
        if (context == null) {
            return null;
        }
        File cacheFile = context.getExternalFilesDir("");
        if (cacheFile != null) {
            String parent = cacheFile.getAbsolutePath() + File.separator + "Update";
            if (makeDirs(parent)) {
                return new File(parent, fileName).getAbsolutePath();
            }
        }
        return null;
    }

    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            LogUtils.i("FileUtil", "filePath is empty");
            return false;
        }
        File file = new File(filePath);
        return (file.exists());
    }

    public static boolean deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            LogUtils.i("FileUtil", "filePath is empty");
            return false;
        }
        return deleteDir(new File(filePath));
    }

    public static boolean createFile(String filePath) {
        return createFile(filePath, true);
    }

    public static boolean createFile(String filePath, boolean isOverride) {
        if (TextUtils.isEmpty(filePath)) {
            LogUtils.i("FileUtil", "filePath is empty");
            return false;
        }
        File file = new File(filePath);
        if ((file.exists())) {
            if (!isOverride) {
                return true;
            }
            deleteDir(file);
        }
        File parentFile = file.getParentFile();
        if (parentFile != null) {
            makeDirs(parentFile);
        }
        boolean isSuccess = false;
        try {
            isSuccess = file.createNewFile();
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return (isSuccess) && (file.exists());
    }

    public static long getFreeDiskSpace() {
        String state = Environment.getExternalStorageState();
        long freeSpace = 0L;
        if (state.equals("mounted")) {
            try {
                File filePath = Environment.getExternalStorageDirectory();
                StatFs statFs = new StatFs(filePath.getPath());
                long blockSize = statFs.getBlockSizeLong();
                long availableBlocks = statFs.getAvailableBlocksLong();
                freeSpace = blockSize * availableBlocks;
            } catch (Exception e) {
                LogUtils.e(e);
            }
        } else {
            return -1L;
        }
        return freeSpace;
    }

    public static long getTotalDiskSpace() {
        String state = Environment.getExternalStorageState();
        long totalSpace = 0L;
        if (state.equals("mounted")) {
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs statFs = new StatFs(path.getPath());
                long totalBlocks = statFs.getBlockCountLong();
                long block = statFs.getBlockSizeLong();
                totalSpace = block * totalBlocks;
            } catch (Exception e) {
                LogUtils.e(e);
            }
        } else {
            return -1L;
        }
        return totalSpace;
    }

    public static String FormatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0L) {
            return wrongSize;
        }
        if (fileS < 1024L) {
            fileSizeString = df.format(fileS) + "B";
        } else if (fileS < 1048576L) {
            fileSizeString = df.format(fileS / 1024.0D) + "KB";
        } else if (fileS < 1073741824L) {
            fileSizeString = df.format(fileS / 1048576.0D) + "MB";
        } else {
            fileSizeString = df.format(fileS / 1.073741824E9D) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 获取文件/文件夹大小
     * @param file file
     * @return size
     */
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            if (file != null && file.exists()) {
                if (file.isDirectory()) {
                    for (File item : file.listFiles()) {
                        // 如果下面还有文件
                        size += getFolderSize(item);
                    }
                } else {
                    size += file.length();
                }
            }
        } catch (Exception e) {
            size = 0;
            //LogUtil.d(TAG,"Exception:" + e.getMessage());
        }
        return size;
    }

    /**
     * 删除文件或目录
     * @param dir dir
     */
    public static boolean deleteDir(File dir) {
        return deleteDir(dir, true);
    }

    /**
     * 删除文件或目录
     * @param dir          dir
     * @param isDeletePath 是否删除当前目录
     */
    public static boolean deleteDir(File dir, boolean isDeletePath) {
        try {
            if (dir != null && dir.exists()) {
                if (dir.isDirectory()) {
                    boolean success = true;
                    // 目录
                    for (File item : dir.listFiles()) {
                        success = success && deleteDir(item, true);
                    }
                    if (!success) return false;
                    if (dir.listFiles().length == 0) {
                        // 目录下没有文件或者目录，isDeletePath为true删除
                        return !isDeletePath || dir.delete();
                    }
                } else {
                    // 如果是文件，删除
                    return dir.delete();
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 删除当前文件夹下其他文件，不包括子文件夹
     * @param file file
     */
    public static boolean deleteOther(File file) {
        boolean success = false;
        try {
            if (file != null && file.exists()) {
                File parent = file.getParentFile();
                if (parent == null || !parent.exists()) return false;
                String dir = parent.getAbsolutePath();
                // 非当前应用目录，不可任意删除！！
                if (canSafeDelete(dir)) {
                    if (parent.isDirectory()) {
                        success = true;
                        // 目录
                        for (File item : parent.listFiles()) {
                            if (item != null && item.isFile() &&
                                    !TextUtils.equals(item.getAbsolutePath(), file.getAbsolutePath())) {
                                success = success && item.delete();
                            }
                        }
                    }
                }
            }
            return success;
        } catch (Exception ignore) {
            return false;
        }
    }

    private static boolean canSafeDelete(String path) {
        boolean safe = false;
        if (path.startsWith(BaseContext.getContext().getCacheDir().getParentFile().getAbsolutePath())){
            safe = true;
        } else {
            if (checkSdcard()){
                if (path.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                    String appDir = Constant.getAppDir();
                    String externalAppDir = null;
                    File cacheFile = BaseContext.getContext().getExternalCacheDir();
                    if (cacheFile != null) externalAppDir = cacheFile.getParentFile().getAbsolutePath();
                    if (path.contains(appDir) || (externalAppDir != null && path.contains(externalAppDir))) {
                        // 可以删除
                        safe = true;
                    }
                }
            }
        }
        return safe;
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     * @param context context
     * @param uri     uri
     */
    public static String getFileAbsolutePath(Context context, Uri uri) {
        if (context == null || uri == null) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else if ("home".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/documents/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                if (TextUtils.isEmpty(id)) {
                    return null;
                }
                if (id.startsWith("raw:")) {
                    return id.substring(4);
                }
                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"
                };
                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    try {
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception ignore) {
                    }
                }
                try {
                    String path = getDataColumn(context, uri, null, null);
                    if (path != null) {
                        return path;
                    }
                } catch (Exception ignore) {
                }
                return null;
            } else if (isMediaDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri;
                switch (type.toLowerCase(Locale.ENGLISH)) {
                    case "image":
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "video":
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "audio":
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        break;
                    default:
                        contentUri = MediaStore.Files.getContentUri("external");
                        break;
                }
                String selection = MediaStore.MediaColumns._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
            // File
            return uri.getPath();
        }
        return null;
    }

    /**
     * 通过游标获取当前文件路径
     * @param context       context
     * @param uri           uri
     * @param selection     selection
     * @param selectionArgs selectionArgs
     * @return 路径，未找到返回null
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.MediaColumns.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos on android M.
     */
    public static boolean isNewGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.contentprovider".equals(uri.getAuthority());
    }

    /**
     * 生成一个图片文件名
     * @return name
     */
    public static String getRandomImageFileName() {
        return "image_" + EncryptUtil.MD5String(UUID.randomUUID().toString(), true, true);
    }

    /**
     * 是否图片文件
     * @param file file
     * @return true or false
     */
    public static boolean isImageFile(File file) {
        try {
            if (file != null && file.isFile() && file.exists()) {
                int[] wh = ImageUtil.getImageWH(file.getAbsolutePath());
                return wh[0] > 0 && wh[1] > 0;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean makeDirs(File dir) {
        return makeDirs(dir.getAbsolutePath());
    }

    /**
     * 建立目录
     * @param dir 目录
     * @return 是否成功创建
     */
    public static boolean makeDirs(String dir) {
        if (dir.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            //判断sd卡状态
            if (checkSdcard()) {
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    boolean flag = fDir.mkdirs();
                    if (!flag) {
                        ToastUtils.showShort("创建文件目录失败，请检查存储卡权限");
                    }
                    return flag;
                }
                return true;
            } else {
                ToastUtils.showShort("未检测到SD卡");
                return false;
            }
        } else {
            File fDir = new File(dir);
            if (!fDir.exists()) {
                boolean flag = fDir.mkdirs();
                if (!flag) {
                    ToastUtils.showShort("创建文件目录失败");
                }
                return flag;
            }
            return true;
        }
    }

    /**
     * 检查存储卡剩余空间
     * @param size size 单位MB
     * @return true or false
     */
    public static boolean checkSdcardSpace(int size) {
        if (checkSdcard()) {
            try {
                long freeSpace = Environment.getExternalStorageDirectory().getUsableSpace();
                return freeSpace >= size * 1024 * 1024;
            } catch (Exception ex) {
                ToastUtils.showShort("请检查存储卡权限");
                return false;
            }
        }
        return false;
    }

    private static boolean checkSdcard() {
        return TextUtils.equals(Environment.MEDIA_MOUNTED, Environment.getExternalStorageState());
    }

    /**
     * 转换图片数据
     * @param file       保存的文件
     * @param tempFile   待转换的文件
     * @param isCompress 是否压缩(图片文件)
     * @return true表示保存成功
     */
    public static boolean savePhotoCompress(File file, File tempFile, boolean isCompress, float fileSize) {
        if (tempFile != null && file != null) {
            if (!tempFile.exists() || !tempFile.isFile() || !tempFile.canRead()) return false;
            boolean saveSuccess;
            FileInputStream tempInputStream = null;
            FileOutputStream newOutputStream = null;
            try {
                tempInputStream = new FileInputStream(tempFile);
                newOutputStream = new FileOutputStream(file);
                if (isCompress) {
                    double times = tempFile.length() / 1024.0f / fileSize;
                    if (times > 1.0) {
                        Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getPath());
                        ByteArrayOutputStream byteArrayOs = new ByteArrayOutputStream();
                        ImageUtil.compressBitmap(bitmap, fileSize, byteArrayOs);
                        byteArrayOs.writeTo(newOutputStream); //把压缩后的数据baos读入到文件中
                        bitmap.recycle();
                        byteArrayOs.close();
                        byteArrayOs.flush();
                    } else {
                        copyFile(tempInputStream, newOutputStream);
                    }
                } else {
                    copyFile(tempInputStream, newOutputStream);
                }
                saveSuccess = true;
            } catch (Exception ex) {
                LogUtils.d("savePhotoCompress:" + ex.getMessage());
                saveSuccess = false;
            } finally {
                try {
                    if (tempInputStream != null) {
                        tempInputStream.close();
                    }
                    if (newOutputStream != null) {
                        newOutputStream.close();
                        newOutputStream.flush();
                    }
                } catch (Exception ex) {
                    LogUtils.d("close failed:" + ex.getMessage());
                }
            }
            return saveSuccess && tempFile.delete();
        }
        return false;
    }

    /**
     * 流处理
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @throws IOException IOException
     */
    public static void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int byteRead;
        while ((byteRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, byteRead);
        }
    }

    /**
     * 保存drawable为文件
     * @param context context
     * @param resId   resId
     * @param outFile outFile
     * @return true
     */
    public static boolean saveDrawable(Context context, @DrawableRes int resId, File outFile) {
        return saveDrawable(context, resId, outFile, false);
    }

    /**
     * 保存drawable为文件
     * @param context context
     * @param resId   resId
     * @param outFile outFile
     * @return true
     */
    public static boolean saveDrawable(Context context, @DrawableRes int resId, File outFile, boolean png) {
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
            return saveBitmap(bitmap, outFile, png);
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 保存bitmap
     * @param bitmap  bitmap
     * @param outFile outFile
     * @param png     png
     * @return 保存bitmap为文件
     */
    public static boolean saveBitmap(Bitmap bitmap, File outFile, boolean png) {
        if (outFile == null) {
            return false;
        }
        if (!makeDirs(outFile.getParentFile().getAbsolutePath())) {
            return false;
        }
        if (FileUtil.isImageFile(outFile)) {
            return true;
        } else {
            FileUtil.deleteDir(outFile);
        }
        boolean saveSuccess;
        FileOutputStream newOutputStream = null;
        try {
            newOutputStream = new FileOutputStream(outFile);
            ByteArrayOutputStream byteArrayOs = new ByteArrayOutputStream();
            bitmap.compress(png ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 95, byteArrayOs);
            byteArrayOs.writeTo(newOutputStream);
            bitmap.recycle();
            byteArrayOs.close();
            byteArrayOs.flush();
            saveSuccess = true;
        } catch (Exception ex) {
            saveSuccess = false;
        } finally {
            try {
                if (newOutputStream != null) {
                    newOutputStream.close();
                    newOutputStream.flush();
                }
            } catch (Exception ex) {
                LogUtils.d("close failed:" + ex.getMessage());
            }
        }
        return saveSuccess;
    }

    /**
     * 获取扩展名 带.
     * @param path 文件路径
     */
    public static String getExtensionName(String path) {
        if (!TextUtils.isEmpty(path) && (path.length() > 0)) {
            int dot = path.lastIndexOf('.');
            if ((dot > -1) && (dot < (path.length() - 1))) {
                return path.substring(dot).toLowerCase();
            }
        }
        return "";
    }

    /**
     * 获取扩展名 不带.
     * @param path 文件路径
     */
    public static String getExName(String path) {
        if (!TextUtils.isEmpty(path) && (path.length() > 0)) {
            int dot = path.lastIndexOf('.');
            if ((dot > -1) && (dot < (path.length() - 1))) {
                String result = path.substring(dot).toLowerCase();
                if (result.length() > 1) {
                    result = result.substring(1);
                } else {
                    result = "";
                }
                return result;
            }
        }
        return "";
    }

    //查看文件的后缀名，对应的MIME类型
    private static final String[][] MIME_MapTable = {
            //word文档
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            //excel文档
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            //ppt文档
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            //pdf文档
            {".pdf", "application/pdf"},
    };

    private static String getMimeType(File file) {
        String type = "*/*";
        String suffix = getExtensionName(file.getName());
        if (TextUtils.isEmpty(suffix)) {
            return type;
        }
        for (String[] item : MIME_MapTable) {
            if (TextUtils.equals(suffix, item[0])) {
                type = item[1];
                break;
            }
        }
        return type;
    }

    public static void openFile(Context context, File file) {
        if (file == null || !file.exists() || file.length() == 0) {
            ToastUtils.showShort(context, "文件不存在！");
            return;
        }
        try {
            Intent intent = CommonUtils.buildActionUri();
            String type = getMimeType(file);
            Uri fileUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fileUri = FileProvider.getUriForFile(context,
                        FileProviderUtil.getProvider(context), file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                fileUri = Uri.fromFile(file);
            }
            intent.setDataAndType(fileUri, type);
            context.startActivity(intent);
        } catch (Exception e) {
            String exName = getExName(file.getName());
            if (TextUtils.isEmpty(exName)) {
                exName = "此";
            }
            ToastUtils.showShort(context, "请安装能打开" + exName + "文件的程序！");
        }
    }

    public static boolean createNoMedia(String parent) {
        if (!makeDirs(parent)) return false;
        File file = new File(parent, ".nomedia");
        if (file.exists()) {
            if (file.isFile()) {
                return true;
            } else {
                FileUtil.deleteFile(file.getAbsolutePath());
            }
        }
        try {
            return file.createNewFile();
        } catch (Exception e) {
            LogUtils.e(e);
            return false;
        }
    }

    public static boolean checkApk(Context context, String filePath) {
        boolean result = false;
        try {
            PackageManager pm = context.getPackageManager();
            LogUtils.e("archiveFilePath", filePath);
            PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                result = true;//完整
            }
        } catch (Exception ignore) {
        }
        return result;
    }

    /**
     * 通知扫描文件
     * @param context context
     * @param file    file
     */
    public static void scanMediaFile(Context context, File file) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

}
