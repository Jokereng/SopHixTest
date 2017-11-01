package com.alibaba.chenyin.sophixtest.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.VideoView;


/**
 * Created by chenyin on 17/10/31.
 */

public class CustomVideoView extends VideoView {

    private int videoWidth;
    private int videoHeight;
    private int displayAspectRatio;

    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    protected void init(Context context) {
        this.videoHeight = context.getResources().getDisplayMetrics().heightPixels;
        this.videoWidth = context.getResources().getDisplayMetrics().widthPixels;

        super.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {

                CustomVideoView.this.videoWidth = mp.getVideoWidth();
                CustomVideoView.this.videoHeight = mp.getVideoHeight();
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                            if (onCorveHideListener != null) {
                                onCorveHideListener.requestHide();
                            }
                        }
                        if (onInfoListener != null) {
                            onInfoListener.onInfo(mp, what, extra);
                        }
                        return false;
                    }
                });
            }
        });
    }

    MediaPlayer.OnPreparedListener onPreparedListener = null;

    public interface OnCorveHideListener {
        void requestHide();
    }

    @Override
    public void setOnInfoListener(MediaPlayer.OnInfoListener onInfoListener) {
        this.onInfoListener = onInfoListener;
    }

    MediaPlayer.OnInfoListener onInfoListener;

    public void setOnCorveHideListener(OnCorveHideListener onCorveHideListener) {
        this.onCorveHideListener = onCorveHideListener;
    }

    OnCorveHideListener onCorveHideListener;

    @Override
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener l) {
        this.onPreparedListener = l;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //重新计算高度
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);

    }

    public void setDisplayAspectRatio(int var1) {
        displayAspectRatio = var1;
        this.requestLayout();
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    public int getDisplayAspectRatio() {
        return displayAspectRatio;
    }

    public void setCorver(int resource) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resource, opts);
    }


}
