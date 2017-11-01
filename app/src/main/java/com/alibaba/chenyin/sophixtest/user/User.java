package com.alibaba.chenyin.sophixtest.user;

/**
 * Created by chenyin on 17/10/18.
 */

public class User implements IBaseUser {

    private String UserId;
    private String UserPhone;
    private String UserPassWord;

    public String getUserId() {
        return UserId;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public String getUserPassWord() {
        return UserPassWord;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public void setUserPassWord(String userPassWord) {
        UserPassWord = userPassWord;
    }
}
