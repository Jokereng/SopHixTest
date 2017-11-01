package com.alibaba.chenyin.sophixtest.user;


/**
 * Created by chenyin on 17/10/18.
 * <p>
 * 用户系统抽象工厂：登录，注册，找回密码，用户信息模块
 */

public interface IUserSystemFactory {

    //获取登录模块，登录器
    IBaseUser.ILoginer getLoginer();

    //获取注册模块，注册器
    IBaseUser.IRegister getRegister();

    //找回密码模块
    IBaseUser.IFindPassWoder getFindPassWoder();

    //用户信息模块
    IBaseUser.IUserInfoer getUserInfoer();

}

