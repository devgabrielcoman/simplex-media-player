package com.gabrielcoman.simplexmediaplayer.aux.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import static android.graphics.BitmapFactory.decodeByteArray;

public class SimplexBitmap {

    static Bitmap createBitmap (int width, int height, int color, float radius) {
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, output.getWidth(), output.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(output, rect, rect, paint);

        return output;
    }

    static Drawable createDrawable (Bitmap bitmap) {
        return new SimplexDrawable(bitmap, 0, 0);
    }

    public static Bitmap createVideoGradientBitmap () {

        String imageString =
                "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAICAYAAADA+m62AAAAAXNSR0IArs4c6QAA" +
                        "ABxpRE9UAAAAAgAAAAAAAAAEAAAAKAAAAAQAAAAEAAAAYzTSV/QAAAAvSURBVCgV" +
                        "YmBgYBAhEjOYAhXiwmZIcgyhQE4YEINodIwszlAGVADDpUhsmBiYBgAAAP//1nMT" +
                        "5wAAAChJREFUY2BgYJiKhKcB2SCMLgbiM2wF4m1EYIarQEXXiMAM34CKCGIAAN0p" +
                        "shJZ248AAAAASUVORK5CYII=";

        try {
            byte [] encodeByte= Base64.decode(imageString, Base64.DEFAULT);
            return decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static Drawable createVideoGradientDrawable () {
        return createDrawable(createVideoGradientBitmap());
    }
}
