package com.alibaba.chenyin.sophixtest.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * Created by chenyin on 17/10/31.
 */

public interface IBaseFragment {

    @LayoutRes
    int getCreateViewLayoutId();//布局文件资源ID

    void initData();//初始化成员变量及获取Intent传递过来的数据,这个方法中不能调用所有的View，因为View还没有被初始化，要使用View在initView方法中调用

    void initView(View inflateView, Bundle savedInstanceState);//设置View显示数据

    void initListener();//事件监听

    void initDialog();//初始化对话框
}
