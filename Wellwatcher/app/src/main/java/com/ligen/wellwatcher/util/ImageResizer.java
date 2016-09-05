package com.ligen.wellwatcher.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by ligen on 2016/8/30.
 */
public class ImageResizer {

    static int reqWidth;
    static int reqHeight;

    public static Bitmap decodeSampledBitmap(String pathName) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        if(reqWidth == 0 || reqHeight == 0) {
            return 1;
        }

        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if(height > reqHeight || width>reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight/inSampleSize >= reqHeight && (halfWidth/inSampleSize >= reqWidth)) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}

