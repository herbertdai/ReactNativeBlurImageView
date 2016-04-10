package com.aigegou.android.blur;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by herbert on 3/25/16.
 */
public class BlurBuilder {
    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 7.5f;

    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

//        RenderScript rs = RenderScript.create(context);
//        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
//        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
//        theIntrinsic.setRadius(BLUR_RADIUS);
//        theIntrinsic.setInput(tmpIn);
//        theIntrinsic.forEach(tmpOut);
//        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }
}
