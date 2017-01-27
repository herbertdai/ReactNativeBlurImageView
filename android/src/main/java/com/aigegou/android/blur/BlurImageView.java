package com.aigegou.android.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by herbert on 3/25/16.
 */
public class BlurImageView extends ImageView {

    private final Context context;
    private int radius;
    private int sampling;
    private int color;
    private String imageUrl;
    private String androidDrawable;
    private GetHeadBitmapTask getheadBitmapTask;

    private boolean isDirty = false;

    public BlurImageView(Context context) {
        this(context, null);
    }

    public BlurImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        setScaleType(ScaleType.FIT_XY);
    }

    public void setImageUrl(String imageUrl) {
        isDirty = true;
        this.imageUrl = imageUrl;
    }

    public void setAndroidDrawable(String androidDrawable) {
        isDirty = true;
        this.androidDrawable = androidDrawable;
    }

    public void setRadius(int radius) {
        isDirty = true;
        this.radius = radius;
    }

    public void setSampling(int sampling) {
        isDirty = true;
        this.sampling = sampling;
    }

    public void setColor(int color) {
        isDirty = true;
        this.color = color;
    }

    public void setScaleType(String scaleType) {
        switch (scaleType.toUpperCase().replace("_", "")) {
            case "CENTER":
                setScaleType(ScaleType.CENTER);
                break;
            case "CENTERCROP":
                setScaleType(ScaleType.CENTER_CROP);
                break;
            case "CENTERINSIDE":
                setScaleType(ScaleType.CENTER_INSIDE);
                break;
            case "FITCENTER":
                setScaleType(ScaleType.FIT_CENTER);
                break;
            case "FITEND":
                setScaleType(ScaleType.FIT_END);
                break;
            case "FITSTART":
                setScaleType(ScaleType.FIT_START);
                break;
            case "FITXY":
                setScaleType(ScaleType.FIT_XY);
                break;
            case "MATRIX":
                setScaleType(ScaleType.MATRIX);
                break;
            default:
                throw new RuntimeException("Invalid scaleType " + scaleType);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            updateView();
        }
    }

    private void updateView() {
        if (!isDirty) {
            return;
        }
        if (imageUrl == null && androidDrawable == null) {
            throw new RuntimeException("BlurImageView: Must set imageUrl or androidDrawable");
        }
        if (imageUrl != null && androidDrawable != null) {
            throw new RuntimeException("BlurImageView: Must set either imageUrl or androidDrawable, not both");
        }
        if (sampling == 0) {
            throw new RuntimeException("BlurImageView: Must set sampling to non-zero value");
        }
        if (getheadBitmapTask != null) {
            getheadBitmapTask.cancel(true);
            getheadBitmapTask = null;
        }
        getheadBitmapTask = new GetHeadBitmapTask();
        getheadBitmapTask.execute();
    }

    private Bitmap loadAndroidDrawable() {
        int id = context.getResources().getIdentifier(androidDrawable, "drawable", context.getPackageName());
        if (id == 0) {
            throw new RuntimeException("Could not get drawable " + androidDrawable + " package " + context.getPackageName());
        }
        return BitmapFactory.decodeResource(getContext().getApplicationContext().getResources(), id);
    }

    public Bitmap loadImgUrl() {
        try {
            return Picasso.with(context).load(imageUrl).get();
        } catch (IOException ex) {
            Log.e("BLR", "Could not load imageUrl", ex);
            return null;
        }
    }

    private Bitmap fastBlurImage(@NonNull Bitmap bitmap) {
        BlurFactor factor = new BlurFactor();
        factor.width = bitmap.getWidth();
        factor.height = bitmap.getHeight();
        factor.radius = radius;
        factor.sampling = sampling;
        factor.color = color;

        return FastBlur.of(context, bitmap, factor);
    }

    private class GetHeadBitmapTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... strings) {
            Bitmap bitmap;
            if (imageUrl != null) {
                bitmap = loadImgUrl();
            } else {
                bitmap = loadAndroidDrawable();
            }
            if (bitmap == null) {
                return null;
            }
            return fastBlurImage(bitmap);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                setImageBitmap(bitmap);
            }
        }
    }


}
