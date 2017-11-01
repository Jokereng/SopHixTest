package com.alibaba.chenyin.sophixtest.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Created by chenyin on 17/10/31.
 */

public class LayoutUtil {
    public final static int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    public final static int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

    /**
     * Listener for size availability
     */
    public interface SizeAvailableListener {
        /**
         * Fires when size is available
         */
        void onSizeAvailable();
    }

    /**
     * When you absolutely need a size of a view. Fires the listener immediately if the view size is available, otherwise, it waits until the end of the initial layout. This will only give you the first available size. The listener only fires up to one time.
     *
     * @param view     View to watch for a size
     * @param listener Listener to fire when size is available
     */
    public static void OnSizeAvailable(final View view, final SizeAvailableListener listener) {
        // if the size is already available
        if (view.getHeight() != 0 || view.getWidth() != 0) {
            // fire the listener
            listener.onSizeAvailable();
        } else {
            // wait for the view to lay out
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // do not listen to events twice
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    // size is available
                    listener.onSizeAvailable();
                }
            });
        }
    }

    /**
     * Inflate Views
     *
     * @param context    app
     * @param resourceId resourceID
     * @return inflated view
     */
    public static View inflate(Context context, int resourceId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(resourceId, null);
        return view;
    }
}
