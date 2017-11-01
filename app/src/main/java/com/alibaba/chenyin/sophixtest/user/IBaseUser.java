package com.alibaba.chenyin.sophixtest.user;

import android.content.Context;

import java.util.Map;

/**
 * Created by chenyin on 17/10/18.
 * <p>
 * 抽象用户接口实现，便于泛型化设计
 */

public interface IBaseUser {

    /**
     * 登录抽象接口
     *
     * @param <U> 用户信息
     */
    public interface ILoginer<U extends IBaseUser> {
        //登录账号
        void login(U user);

        //注销账号
        void loginout(U user);
    }

    /**
     * 注册抽象接口
     *
     * @param <U> 用户信息
     */
    public interface IRegister<U extends IBaseUser> {
        //注册账号
        void registerAccount(U user);
    }

    /**
     * 找回密码抽象接口
     *
     * @param <U> 用户信息
     */
    public interface IFindPassWoder<U extends IBaseUser> {
        //找回密码
        void findPassWord(U user);
    }

    /**
     * 用户信息抽象接口
     *
     * @param <U> 用户信息
     */
    public interface IUserInfoer<U extends IBaseUser> {
        //获取用户信息
        U getUserInfo();

        //保存用户信息
        void saveUserInfo(U userInfo);
    }


}
