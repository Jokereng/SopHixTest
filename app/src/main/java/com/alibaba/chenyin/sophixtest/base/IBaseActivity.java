package com.alibaba.chenyin.sophixtest.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.List;

/**
 * Created by chenyin on 17/10/31.
 */

public interface IBaseActivity {

    int getFragmentContainerViewId();//返回Activity设置ContentView的布局文件资源ID

    void initData(@NonNull Bundle savedInstanceState);//初始化成员变量及获取Intent传递过来的数据,这个方法中不能调用所有的View，因为View还没有被初始化，要使用View在initView方法中调用

    void initView(@NonNull Bundle savedInstanceState);//设置View显示数据

    void initListener(@NonNull Bundle savedInstanceState);//事件监听

    void initDialog(@NonNull Bundle savedInstanceState);//初始化对话框

    Fragment getFragment();

    Fragment[] getFragments();

    List<Fragment> getFragmentList();

    String getFragmentTag();

}
