package com.alibaba.chenyin.sophixtest;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alibaba.chenyin.sophixtest.user.IUserSystemFactory;
import com.alibaba.chenyin.sophixtest.user.SystemAccountFactory;
import com.alibaba.chenyin.sophixtest.user.User;
import com.alibaba.chenyin.sophixtest.widget.CustomVideoView;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
//    private ViewGroup linearLayout;
//    private CustomVideoView customVideoView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

//        Button button = (Button) findViewById(R.id.btn);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //账户登录
//                IUserSystemFactory iUserSystemFactory = SystemAccountFactory.create(MainActivity.this);
//                User user = new User();
//                user.setUserPhone("123456");
//                user.setUserPassWord("654321");
//                iUserSystemFactory.getLoginer().login(user);
//            }
//        });

//        initView();
//    }

}
