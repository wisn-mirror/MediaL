package com.wisn.medial.imagelist.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import static com.google.android.exoplayer2.ExoPlayerLibraryInfo.TAG;

/**
 * Created by Wisn on 2019-05-03 17:12.
 */
public class ImageUtils {

    public static int[] getWidthHeight(String imagePath) {
        if (imagePath.isEmpty()) {
            return new int[]{0, 0};
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            Bitmap originBitmap = BitmapFactory.decodeFile(imagePath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 使用第一种方式获取原始图片的宽高
        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;

        // 使用第二种方式获取原始图片的宽高
        if (srcHeight == -1 || srcWidth == -1) {
            try {
                ExifInterface exifInterface = new ExifInterface(imagePath);
                srcHeight =
                        exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);
                srcWidth =
                        exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 使用第三种方式获取原始图片的宽高
        if (srcWidth <= 0 || srcHeight <= 0) {
            Bitmap bitmap2 = BitmapFactory.decodeFile(imagePath);
            if (bitmap2 != null) {
                srcWidth = bitmap2.getWidth();
                srcHeight = bitmap2.getHeight();
                try {
                    if (!bitmap2.isRecycled()) {
                        bitmap2.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        int orient = getOrientation(imagePath);
        if (orient == 90 || orient == 270) {
            return new int[]{srcHeight, srcWidth};
        }
        return new int[]{srcWidth, srcHeight};
    }

    public static float getWideImageDoubleScale(Context context, String imagePath) {
        int[] wh = getWidthHeight(imagePath);
        float imageHei = wh[1];
        float phoneHei = PhoneUtil.getPhoneHei(context.getApplicationContext());
        return phoneHei / imageHei;
    }

    public static float getSmallImageMinScale(Context context, String imagePath) {
        int[] wh = getWidthHeight(imagePath);
        float imageWid = wh[0];
        float phoneWid = PhoneUtil.getPhoneWid(context.getApplicationContext());
        return phoneWid / imageWid;
    }

    public static float getSmallImageMaxScale(Context context, String imagePath) {
        return getSmallImageMinScale(context, imagePath) * 2;
    }

    public static float getLongImageMinScale(Context context, String imagePath) {
        int[] wh = getWidthHeight(imagePath);
        float imageWid = wh[0];
        float phoneWid = PhoneUtil.getPhoneWid(context.getApplicationContext());
        return phoneWid / imageWid;
    }

    public static float getLongImageMaxScale(Context context, String imagePath) {
        return getLongImageMinScale(context, imagePath) * 2;
    }




    public static int getOrientation(String imagePath) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(imagePath);
            int orientation =
                    exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isSmallImage(Context context, String imagePath) {
        int[] wh = getWidthHeight(imagePath);
        boolean isSmallImage = wh[0] < PhoneUtil.getPhoneWid(context.getApplicationContext());
        Log.d(TAG, "isSmallImage = " + isSmallImage);
        return isSmallImage;
    }


    public static boolean isLongImage(Context context, String imagePath) {
        int[] wh = getWidthHeight(imagePath);
        float w = wh[0];
        float h = wh[1];
        float imageRatio = (h / w);
        float phoneRatio = PhoneUtil.getPhoneRatio(context.getApplicationContext()) + 0.1F;
        boolean isLongImage = (w > 0 && h > 0) && (h > w) && (imageRatio >= phoneRatio);
        Log.d(TAG, "isLongImage = " + isLongImage);
        return isLongImage;
    }

    public static boolean isWideImage(Context context, String imagePath) {
        int[] wh = getWidthHeight(imagePath);
        float w = wh[0];
        float h = wh[1];
        float imageRatio = (w / h);
        //float phoneRatio = PhoneUtil.getPhoneRatio(context.getApplicationContext()) + 0.1F;
        boolean isWideImage = (w > 0 && h > 0) && (w > h) && (imageRatio >= 2);
        Log.d(TAG, "isWideImage = " + isWideImage);
        return isWideImage;
    }

    public static String getImageTypeWithMime(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        String type = options.outMimeType;
        // ”image/png”、”image/jpeg”、”image/gif”
        if (TextUtils.isEmpty(type)) {
            type = "";
        } else {
            type = type.substring(6, type.length());
        }
        return type;
    }

    public static boolean isGifImageWithMime(String path) {
        return "gif".equalsIgnoreCase(getImageTypeWithMime(path));
    }

    public static boolean isBmpImageWithMime(String path) {
        return "bmp".equalsIgnoreCase(getImageTypeWithMime(path));
    }

    public static boolean isGifImageWithUrl(String url) {
        return url.toLowerCase().endsWith("gif");
    }
}
