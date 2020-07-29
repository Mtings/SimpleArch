package com.song.sakura.util;

import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.song.sakura.app.App;

import java.io.ByteArrayOutputStream;

public class GlideUtil {

    public static byte[] syncLoad(String url, String type) {
        boolean isGif = type.endsWith("gif");
        if (isGif) {
            try {
                FutureTarget<byte[]> target = Glide.with(App.getApplication())
                        .as(byte[].class)
                        .load(url)
                        .decode(GifDrawable.class).submit();
                return target.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        FutureTarget<Bitmap> target = Glide.with(App.getApplication())
                .asBitmap().load(url).submit();
        try {
            Bitmap bitmap = target.get();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
