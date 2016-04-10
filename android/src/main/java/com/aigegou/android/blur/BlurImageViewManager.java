package com.aigegou.android.blur;

import com.aigegou.android.blur.BlurImageView;
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
    @ReactProp(name = "radius", defaultInt = 0)
    public void setRadius(BlurImageView view, int radius) {
        view.setRadiusAndUpdate(radius);
    }

    @ReactProp(name = "sampling", defaultInt = 0)
    public void setSampling(BlurImageView view, int sampling) {
        view.setSamplingAndUpdate(sampling);
    }

    @ReactProp(name = "imageUrl")
    public void setImageUrl(BlurImageView view, String url) {
        view.setImageUrlAndUpdate(url);
    }
}
