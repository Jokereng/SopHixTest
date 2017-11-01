package com.alibaba.chenyin.sophixtest.ui.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.chenyin.sophixtest.R;
import com.alibaba.chenyin.sophixtest.base.BaseActivity;
import com.alibaba.chenyin.sophixtest.widget.CustomVideoView;

import butterknife.BindView;

/**
 * Created by chenyin on 17/10/31.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_vedio_view)
    CustomVideoView customVideoView;

    @Override
    protected int getContentViewLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        showVedio();
    }

    //暂停
    @Override
    protected void onPause() {
        customVideoView.start();
        super.onPause();
    }

    @Override
    protected void onResume() {
        customVideoView.start();
        super.onResume();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        customVideoView.pause();
        super.onStop();
    }

    private void showVedio() {
        //设置播放加载路径
        customVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
        if (customVideoView.isPlaying() == false) {
            //播放
            customVideoView.start();
            //循环播放
            customVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    customVideoView.seekTo(0);
                    customVideoView.start();
                }
            });
        } else {
            customVideoView.stopPlayback();
        }

    }
}
