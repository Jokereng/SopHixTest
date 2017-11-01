package com.alibaba.chenyin.sophixtest.user;

import android.content.Context;

/**
 * 登录模块的基类
 *
 * @param <U> 用户信息
 */
public abstract class BaseLoginer<U extends IBaseUser> implements IBaseUser.ILoginer<U> {
    private Context mContext;

    public BaseLoginer(Context mContext) {
        this.mContext = mContext;
    }
}
