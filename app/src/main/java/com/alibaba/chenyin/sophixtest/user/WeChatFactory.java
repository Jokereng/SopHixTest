package com.alibaba.chenyin.sophixtest.user;

import android.content.Context;

/**
 * Created by chenyin on 17/10/18.
 * <p>
 * 微信登录时的用户工厂
 */

public class WeChatFactory extends BaserUserSystemFactory {
    public WeChatFactory(Context mContext) {
        super(mContext);
    }

    public static IUserSystemFactory create(Context context) {
        return new WeChatFactory(context);
    }

    /**
     * 微信登录
     *
     * @return 登录对象
     */
    @Override
    public IBaseUser.ILoginer getLoginer() {
        return new WeChatLoginer(getmContext());
    }

    @Override
    public IBaseUser.IRegister getRegister() {
        return null;
    }

    @Override
    public IBaseUser.IFindPassWoder getFindPassWoder() {
        return null;
    }

    /**
     * 获取微信信息
     *
     * @return 微信信息对象
     */
    @Override
    public IBaseUser.IUserInfoer getUserInfoer() {
        return null;
    }
}
