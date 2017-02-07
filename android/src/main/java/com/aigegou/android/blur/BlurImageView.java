package com.aigegou.android.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIBlock;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by herbert on 3/25/16.
 */
public class BlurImageView extends ImageView {

    private static final Map<String, Bitmap> bitmaps = new HashMap<>();

    private final Context context;
    private final ThemedReactContext reactContext;
    private int radius;
    private int sampling;
    private int color;
    private String imageUrl;
    private String androidDrawable;
    private String snapshotViewId;
    private GetHeadBitmapTask getheadBitmapTask;

    private boolean isDirty = false;


    public BlurImageView(Context context, ThemedReactContext reactContext) {
        super(context);
        Log.d("BLR", "Creating BlurImageView");
        this.reactContext = reactContext;
        this.context = context;
        setScaleType(ScaleType.FIT_XY);
        updateView();
    }

    public void setImageUrl(String imageUrl) {
        isDirty = ObjectUtils.notEqual(imageUrl, this.imageUrl);
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

    public void setSnapshotViewId(String id) {
        isDirty = true;
        this.snapshotViewId = id;
    }

    public void cleanupBitmap() {
        isDirty = true;
        Log.d("BLR", "View cleanupBitmap");
        bitmaps.clear();
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
        isDirty = false;
        if (imageUrl == null && androidDrawable == null && snapshotViewId == null) {
            throw new RuntimeException("BlurImageView: Must set imageUrl or androidDrawable or snapshotViewId");
        }
        // From http://stackoverflow.com/a/3466476
        if ((imageUrl != null ^ androidDrawable != null ^ snapshotViewId != null)  &&
            (imageUrl != null && androidDrawable != null && snapshotViewId != null)) {
            throw new RuntimeException("BlurImageView: Cannot set more than one of imageUrl, androidDrawable and snapshotViewId");
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

    public void loadSnapshotViewTag(int snapshotViewTag) {
        if (bitmaps.containsKey(snapshotViewId)) {
            Log.d("BLR", "Bitmap already loaded with snapshotViewTag: " + snapshotViewId);
            notifyChange("viewShotSet");
            return;
        }
        UIManagerModule uiManager = reactContext.getNativeModule(UIManagerModule.class);
        uiManager.addUIBlock(new ViewShot(snapshotViewTag));
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
            Bitmap bitmap = null;
            if (imageUrl != null) {
                bitmap = loadImgUrl();
            } else if (androidDrawable != null) {
                bitmap = loadAndroidDrawable();
            } else {
                bitmap = bitmaps.get(snapshotViewId);
                if (bitmap != null) {
                    Log.d("BLR", "Reusing snapshot" + bitmap);
                    return bitmap;
                }
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
                notifyChange("blurSet");
            }
        }
    }

    public void notifyChange(String message) {
        Log.d("BLR", "Notifying onChange " + message);
        WritableMap event = Arguments.createMap();
        event.putString("message", message);
        ReactContext reactContext = (ReactContext)getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                "topChange",
                event);

    }

    private class ViewShot implements UIBlock {

        private final int snapshotViewTag;

        public ViewShot(int snapshotViewTag) {
            this.snapshotViewTag = snapshotViewTag;
        }

        @Override
        public void execute(NativeViewHierarchyManager nativeViewHierarchyManager) {
            Log.d("BLR", "Executing ViewShot on " + Thread.currentThread().getName());
            View view = nativeViewHierarchyManager.resolveView(snapshotViewTag);
            if (view == null) {
                Log.d("BLR", "No view with setTag " + snapshotViewTag);
                return;
            }
            if (view.getWidth() <= 0 || view.getHeight() <= 0) {
                Log.d("BLR", "View not sized yet " + view);
                return;
            }
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            view.draw(c);

            bitmap = fastBlurImage(bitmap);
            bitmaps.put(snapshotViewId, bitmap);
            setImageBitmap(bitmap);
            notifyChange("viewShotSet");
        }
    }

}
