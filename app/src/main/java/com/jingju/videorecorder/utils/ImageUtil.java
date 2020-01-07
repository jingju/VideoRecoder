package com.jingju.videorecorder.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by saiyuan on 2016/10/26.
 */
public class ImageUtil {

    public static Bitmap screenShootForArenaMore(View view) {
        if (view == null) {
            return null;
        }
        Bitmap bmp = null;
        try {
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap tmp = view.getDrawingCache(true);
            LogUtils.i("ScreenShoot: " + tmp.getWidth() + ", " + tmp.getHeight());
            int width = tmp.getWidth();
            int height = tmp.getHeight();
            tmp.setHasAlpha(false);
            bmp = Bitmap.createBitmap(tmp, 0, DisplayUtil.dp2px(48), width, height - DisplayUtil.dp2px(48));
//            bmp = Bitmap.createScaledBitmap(tmp, width, height - DisplayUtil.dp2px(48), false);
            view.setDrawingCacheEnabled(false);
            view.destroyDrawingCache();
        } catch (IllegalArgumentException e) {
            LogUtils.e(e);
        } catch (IllegalStateException e) {
            LogUtils.e(e);
        } catch (OutOfMemoryError e) {
            LogUtils.e(e);
        } catch (Exception e) {
            LogUtils.e(e);
        }
//        File externalCache = MKContext.getContext().getExternalFilesDir(null);
//        String fileName = null;
//        if (externalCache != null) {
//            fileName = externalCache.getAbsolutePath() + "/img.jpg";
//        }
//        File file = new File(fileName);
//        if (!file.getParentFile().exists())
//            file.getParentFile().mkdir();
//
//        if (file.exists())
//            file.delete();
//        try {
//            file.createNewFile();
//            FileOutputStream outStream = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 60, outStream);
//            outStream.flush();
//            outStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return bmp;
    }

    public static Bitmap screenShoot(View view) {
        if (view == null) {
            return null;
        }
        Bitmap bmp = null;
        try {
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap tmp = view.getDrawingCache(true);
            LogUtils.i("ScreenShoot: " + tmp.getWidth() + ", " + tmp.getHeight());
            int width = tmp.getWidth();
            int height = tmp.getHeight();
            bmp = Bitmap.createScaledBitmap(tmp, width, height, false);
            view.setDrawingCacheEnabled(false);
            view.destroyDrawingCache();
        } catch (IllegalArgumentException e) {
            LogUtils.e(e);
        } catch (IllegalStateException e) {
            LogUtils.e(e);
        } catch (OutOfMemoryError e) {
            LogUtils.e(e);
        } catch (Exception e) {
            LogUtils.e(e);
        }
//        File externalCache = MKContext.getContext().getExternalFilesDir(null);
//        String fileName = null;
//        if (externalCache != null) {
//            fileName = externalCache.getAbsolutePath() + "/img.jpg";
//        }
//        File file = new File(fileName);
//        if (!file.getParentFile().exists())
//            file.getParentFile().mkdir();
//
//        if (file.exists())
//            file.delete();
//        try {
//            file.createNewFile();
//            FileOutputStream outStream = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 60, outStream);
//            outStream.flush();
//            outStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return bmp;
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    public static Bitmap inputStreamToBitmap(InputStream inputStream)
            throws Exception {
        return BitmapFactory.decodeStream(inputStream);
    }

    public static Bitmap byteToBitmap(byte[] byteArray) {
        if (byteArray.length != 0) {
            return BitmapFactory
                    .decodeByteArray(byteArray, 0, byteArray.length);
        } else {
            return null;
        }
    }

    public static Drawable byteToDrawable(byte[] byteArray) {
        ByteArrayInputStream ins = null;
        if (byteArray != null) {
            ins = new ByteArrayInputStream(byteArray);
        }
        return Drawable.createFromStream(ins, null);
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        byte[] bytes = null;
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bytes = baos.toByteArray();
        }
        return bytes;
    }

    public static byte[] drawableToBytes(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        byte[] bytes = bitmapToBytes(bitmap);
        return bytes;
    }

    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
                h / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(w, h);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float sx = ((float) w / width);
        float sy = ((float) h / height);
        matrix.postScale(1, 1);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(newbmp);
    }

    public static Bitmap getPhotoFromSDCard(String path, String photoName) {
        Bitmap photoBitmap = BitmapFactory.decodeFile(path + "/" + photoName + ".png");
        if (photoBitmap == null) {
            return null;
        } else {
            return photoBitmap;
        }
    }

    public static boolean checkSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static boolean findPhotoFromSDCard(String path, String photoName) {
        boolean flag = false;

        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (dir.exists()) {
                File folders = new File(path);
                File[] photoFile = folders.listFiles();
                for (int i = 0; i < photoFile.length; i++) {
                    String fileName = photoFile[i].getName().split("\\.")[0];
                    if (fileName.equals(photoName)) {
                        flag = true;
                    }
                }
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }
        return flag;
    }

    public static void deleteAllPhoto(String path) {
        if (checkSDCardAvailable()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            if (null == files || files.length == 0) {
                return;
            }
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
    }

    public static void deletePhotoAtPathAndName(String path, String fileName) {
        if (checkSDCardAvailable()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++) {
                System.out.println(files[i].getName());
                if (files[i].getName().equals(fileName)) {
                    files[i].delete();
                }
            }
        }
    }

    /**
     * 根据需要的大小生成bitmap图片
     * @param bitmap      bitmap
     * @param requireSize 需要的大小
     */
    public static void compressBitmap(Bitmap bitmap, double requireSize, ByteArrayOutputStream byteArrayOs) {
        int quality = 100;
        int sub;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOs);
        double byteSize = byteArrayOs.toByteArray().length / 1024.0;
        while (byteSize > requireSize * 1.1 && quality > 10) {
            double times = ((byteSize * 1.0) / requireSize);
            switch ((int) Math.round(times)) {
                case 1:
                    if (times > 1.2) {
                        quality = (int) (quality * 0.93f);
                    } else {
                        quality = (int) (quality * 0.96f);
                    }
                    sub = 1;
                    break;
                case 2:
                    quality = (int) (quality * 0.9f);
                    sub = 2;
                    break;
                case 3:
                case 4:
                    quality = (int) (quality * 0.82f);
                    sub = 4;
                    break;
                default:
                    quality = (int) (quality * 0.75f);
                    sub = 6;
                    break;
            }
            byteArrayOs.reset();
            quality -= sub;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOs);
            byteSize = byteArrayOs.toByteArray().length / 1024.0;
            LogUtils.d("quality:" + quality);
        }
    }

    /**
     * 获取图片的宽高
     * @param imagePath imagePath
     * @return int[]，0：宽，1：高
     */
    public static int[] getImageWH(String imagePath) {
        int[] wh = new int[]{0, 0};
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, options);
            wh[0] = options.outWidth;
            wh[1] = options.outHeight;
            if (wh[0] <= 0 || wh[1] <= 0) {
                ExifInterface exifInterface = new ExifInterface(imagePath);
                wh[1] = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                wh[0] = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
            }
        } catch (Exception ex) {
            LogUtils.e("getImageWH error:" + ex.getMessage());
        }
        return wh;
    }

    /**
     * 获取图片宽高比
     * @param context context
     * @param resId   resId
     * @return 宽高比，失败时返回0
     */
    public static float getImageRatio(Context context, @DrawableRes int resId) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), resId, options);
            return (float) options.outWidth / (float) options.outHeight;
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * 获取图片宽高比
     * @param imagePath 图片路径
     * @return 宽高比，失败时返回0
     */
    public static float getImageRatio(String imagePath) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, options);
            return (float) options.outWidth / (float) options.outHeight;
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * 获取图片的宽高
     * @param context context
     * @param resId   resId
     * @return int[]，0：宽，1：高
     */
    public static int[] getImageWH(Context context, @DrawableRes int resId) {
        int[] wh = new int[]{0, 0};
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), resId, options);
            wh[0] = options.outWidth;
            wh[1] = options.outHeight;
        } catch (Exception ex) {
            LogUtils.e("getImageWH error:" + ex.getMessage());
        }
        return wh;
    }

    /**
     * 获取bitmap大小
     * @param bitmap bitmap
     * @return size
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (bitmap == null) return 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //API 19
            return bitmap.getAllocationByteCount();
        }/*else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            //API 12
            return bitmap.getByteCount();
        }*/ else {
            //earlier version
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }

    /**
     * 是否图片文件
     * @param file file
     * @return true or false
     */
    public static boolean isImage(File file) {
        try {
            if (file != null && file.isFile() && file.exists()) {
                int[] wh = getImageWH(file.getAbsolutePath());
                return wh[0] > 0 && wh[1] > 0;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 获取当前图片的旋转角度
     * @param path 图片路径
     */
    public static int getImageAngle(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            LogUtils.e(e);
        }
        return degree;
    }

    /**
     * 从数据库查询视频缩略图
     * @param context   context
     * @param videoPath path
     * @return bitmap
     */
    @Nullable
    public static Bitmap getVideoImage(Context context, String videoPath) {
        // MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
        String[] videoColumns = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA};
        String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID};

        Cursor cursor = null, thumbCursor = null;
        try {
            cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    videoColumns, MediaStore.Video.Media.DATA + "=?",
                    new String[]{videoPath}, null);
            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                thumbCursor = context.getApplicationContext().getContentResolver().query(
                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                                + "=" + id, null, null);
                if (thumbCursor != null && thumbCursor.moveToFirst()) {
                    String albumPath = thumbCursor.getString(thumbCursor
                            .getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                    return BitmapFactory.decodeFile(albumPath);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        } finally {
            try {
                if (cursor != null) {
                    cursor.close();
                }
                if (thumbCursor != null) {
                    thumbCursor.close();
                }
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
        return null;
    }
}
