package com.alibaba.chenyin.sophixtest.ui.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;

import com.alibaba.chenyin.sophixtest.R;
import com.alibaba.chenyin.sophixtest.base.BaseActivity;
import com.alibaba.chenyin.sophixtest.user.IUserSystemFactory;
import com.alibaba.chenyin.sophixtest.user.SystemAccountFactory;
import com.alibaba.chenyin.sophixtest.user.User;
import com.alibaba.chenyin.sophixtest.widget.CustomVideoView;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chenyin on 17/10/31.
 */

public class SplashActivity extends BaseActivity {

    @BindView(R.id.splash_video_view)
    CustomVideoView customVideoView;

    @BindView(R.id.splash_button_jump)
    Button button;

    @Override
    protected int getContentViewLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        showVedio();
        button.getBackground().setAlpha(80);
    }

    @OnClick(R.id.splash_button_jump)
    public void jumpOnClik() {
        IUserSystemFactory iUserSystemFactory = SystemAccountFactory.create(SplashActivity.this);
        User user = new User();
        user.setUserPhone("123456");
        user.setUserPassWord("123456");
        iUserSystemFactory.getLoginer().login(user);
        startActivity(LoginActivity.class);
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
        customVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.kr36));
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
