package com.aigegou.android.blur;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

/**
 * Created by herbert on 3/25/16.
 */
public class BlurImageViewManager extends SimpleViewManager<BlurImageView> {
    public static final String REACT_CLASS = "RCTBlurImageView";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected BlurImageView createViewInstance(ThemedReactContext reactContext) {
        return new BlurImageView(reactContext);
    }
    @ReactProp(name = "radius", defaultInt = 5)
    public void setRadius(BlurImageView view, int radius) {
        view.setRadius(radius);
    }

    /**
     * Must be a power of 2.
     */
    @ReactProp(name = "sampling", defaultInt = 1)
    public void setSampling(BlurImageView view, int sampling) {
        view.setSampling(sampling);
    }

    @ReactProp(name = "imageUrl")
    public void setImageUrl(BlurImageView view, String url) {
        view.setImageUrl(url);
    }

    @ReactProp(name = "androidDrawable")
    public void setAndroidDrawable(BlurImageView view, String androidDrawable) {
        view.setAndroidDrawable(androidDrawable);
    }

    @ReactProp(name = "scaleType")
    public void setScaleType(BlurImageView view, String scaleType) {
        view.setScaleType(scaleType);
    }

    @ReactProp(name = "color")
    public void setColor(BlurImageView view, int color) {
        view.setColor(color);
    }
}
