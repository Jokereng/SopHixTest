package com.alibaba.chenyin.sophixtest.ui.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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

    @BindView(R.id.login_toolbar)
    Toolbar mToolbar;

    @Override
    protected int getContentViewLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        showVedio();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        mToolbar.setTitle("登录");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                LoginActivity.this.finish();
                break;
            case R.id.action_settings:
                return true;

        }
        return super.onOptionsItemSelected(item);
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
