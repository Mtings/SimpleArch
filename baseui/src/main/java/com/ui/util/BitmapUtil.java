package com.ui.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

public class BitmapUtil {

    public static SoftReference<Bitmap> getCropBitmapFromAssets(Context context, String src,
                                                                int width, int height) {
        if (src == null) {
            return null;
        }
        if (src.equals("")) {
            return null;
        }
        AssetManager am = context.getResources().getAssets();
        SoftReference<Bitmap> bitmap = null;
        BitmapFactory.Options opts = null;
        opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        InputStream is = null;
        try {
            is = am.open(src);
            BitmapFactory.decodeStream(is, null, opts);
        } catch (IOException e) {
            return null;
        }
        int maxth = Math.max(width, height);
        int maxth1 = Math.max(opts.outWidth, opts.outHeight);
        if (maxth > maxth1) {
            try {
                is = am.open(src);
            } catch (IOException e) {
                return null;
            }
            opts.inJustDecodeBounds = false;
            bitmap = new SoftReference<Bitmap>(BitmapFactory.decodeStream(is,
                    null, opts));
        } else {
            final int minSideLength = Math.min(width, height);
            opts.inSampleSize = computeSampleSize(opts, minSideLength, width
                    * height);
            opts.inJustDecodeBounds = false;
            opts.inDither = false;
            opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
            opts.inInputShareable = true;
            opts.inPurgeable = true;
            try {
                is = am.open(src);
                bitmap = new SoftReference<Bitmap>(BitmapFactory.decodeStream(is, null, opts));
            } catch (IOException e) {
                return null;
            } catch (OutOfMemoryError e) {
            }
        }
        return bitmap;
    }


    public static SoftReference<Bitmap> getCropBitmapFromFile(String src,
                                                              int width, int height) {
        if (src == null) {
            return null;
        }
        if (src.equals("")) {
            return null;
        }
        SoftReference<Bitmap> bitmap = null;
        BitmapFactory.Options opts = null;
        opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(src, opts);
        BufferedInputStream in = null;
        int maxth = Math.max(width, height);
        int maxth1 = Math.max(opts.outWidth, opts.outHeight);
        if (maxth > maxth1) {
            try {
                in = new BufferedInputStream(new FileInputStream(new File(src)));
            } catch (FileNotFoundException e) {
                return null;
            }
            opts.inJustDecodeBounds = false;
            bitmap = new SoftReference<Bitmap>(BitmapFactory.decodeStream(in,
                    null, opts));
        } else {
            final int minSideLength = Math.min(width, height);
            opts.inSampleSize = computeSampleSize(opts, minSideLength, width
                    * height);
            opts.inJustDecodeBounds = false;
            opts.inDither = false;
            opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
            opts.inInputShareable = true;
            opts.inPurgeable = true;
            try {
                bitmap = new SoftReference<Bitmap>(BitmapFactory.decodeFile(
                        src, opts));
            } catch (OutOfMemoryError e) {
            }
        }
        return bitmap;
    }

    private static int computeSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
