package com.alibaba.chenyin.sophixtest.user;

import android.content.Context;

/**
 * Created by chenyin on 17/10/18.
 * <p>
 * 账户登录时的用户系统工厂
 */

public class SystemAccountFactory extends BaserUserSystemFactory {
    public SystemAccountFactory(Context mContext) {
        super(mContext);
    }

    public static IUserSystemFactory create(Context context) {
        return new SystemAccountFactory(context);
    }

    /**
     * 账户登录
     *
     * @return 登录对象
     */
    @Override
    public IBaseUser.ILoginer getLoginer() {
        return new SystemAccountLoginer(getmContext());
    }

    /**
     * 注册
     *
     * @return 注册对象
     */
    @Override
    public IBaseUser.IRegister getRegister() {
        return null;
    }

    /**
     * 找回密码
     *
     * @return 找回密码对象
     */
    @Override
    public IBaseUser.IFindPassWoder getFindPassWoder() {
        return null;
    }

    /**
     * 用户信息
     *
     * @return 用户信息对象
     */
    @Override
    public IBaseUser.IUserInfoer getUserInfoer() {
        return null;
    }


}
