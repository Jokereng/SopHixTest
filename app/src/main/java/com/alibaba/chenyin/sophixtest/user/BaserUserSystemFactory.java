package com.alibaba.chenyin.sophixtest.user;

import android.content.Context;

/**
 * Created by chenyin on 17/10/18.
 * <p>
 * 用户系统工厂基类
 */

public abstract class BaserUserSystemFactory implements IUserSystemFactory {

    private Context mContext;

    public BaserUserSystemFactory(Context mContext) {
        this.mContext = mContext;
    }

    public Context getmContext() {
        return mContext;
    }
}
