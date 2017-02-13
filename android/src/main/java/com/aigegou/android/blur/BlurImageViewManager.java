package com.aigegou.android.blur;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

/**
 * Created by herbert on 3/25/16.
 */
public class BlurImageViewManager extends SimpleViewManager<BlurImageView> {
    private Activity mCurrentActivity;

    public static final String REACT_CLASS = "RCTBlurImageView";

    public static final int COMMAND_SET_CARD_REF_HANDLE = 1;


    public BlurImageViewManager(Activity activity) {
        mCurrentActivity = activity;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected BlurImageView createViewInstance(ThemedReactContext reactContext) {
        return new BlurImageView(reactContext, reactContext);
    }

    @Override
    public void onDropViewInstance(BlurImageView view) {
        super.onDropViewInstance(view);
        view.cleanupBitmap();
    }

    @Override
    public Map<String,Integer> getCommandsMap() {
        Log.d("React"," View manager getCommandsMap:");
        return MapBuilder.of("setCardRefHandle", COMMAND_SET_CARD_REF_HANDLE);
    }

    @Override
    public void receiveCommand(BlurImageView view, int commandType, @Nullable ReadableArray args) {
        Log.d("BLR", "Command received, command type: " + commandType);
        Assertions.assertNotNull(view);
        Assertions.assertNotNull(args);
        switch (commandType) {
            case COMMAND_SET_CARD_REF_HANDLE: {
                Log.d("BLR", "Command set card ref handle received");
                int snapshotViewTag = args.getInt(0);
                view.loadSnapshotViewTag(snapshotViewTag);
                return;
            }
            default:
                throw new IllegalArgumentException(String.format("Unsupported command %d received by %s.", commandType, getClass().getSimpleName()));
        }
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


    @ReactProp(name = "snapshotViewId")
    public void setSnapshotViewId(BlurImageView view, String id) {
        view.setSnapshotViewId(id);
    }
}
