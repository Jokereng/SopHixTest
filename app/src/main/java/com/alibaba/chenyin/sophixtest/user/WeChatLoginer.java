package com.alibaba.chenyin.sophixtest.user;

import android.content.Context;
import android.util.Log;

import java.util.Map;

/**
 * Created by chenyin on 17/10/18.
 * <p>
 * 微信登录
 */

public class WeChatLoginer extends BaseLoginer<User> {
    public WeChatLoginer(Context mContext) {
        super(mContext);
    }

    /**
     * 微信登录
     *
     * @param user
     */
    @Override
    public void login(User user) {
        Log.e("WeChatLoginer:", "微信登录");
    }

    /**
     * 微信注销
     *
     * @param user
     */
    @Override
    public void loginout(User user) {
        Log.e("WeChatLoginer:", "微信注销");
    }
}
