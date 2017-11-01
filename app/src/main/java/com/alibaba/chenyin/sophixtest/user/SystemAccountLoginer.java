package com.alibaba.chenyin.sophixtest.user;

import android.content.Context;
import android.util.Log;

/**
 * Created by chenyin on 17/10/18.
 * <p>
 * 使用账户登录
 */

public class SystemAccountLoginer extends BaseLoginer<User> {

    public SystemAccountLoginer(Context mContext) {
        super(mContext);
    }

    /**
     * 账户登录
     *
     * @param user
     */
    @Override
    public void login(User user) {
        Log.e("SystemAccountLoginer:", "账户登录" + user.getUserPhone().toString());
    }

    /**
     * 账户注销
     *
     * @param user
     */
    @Override
    public void loginout(User user) {
        Log.e("SystemAccountLoginer:", "账户注销");
    }
}
