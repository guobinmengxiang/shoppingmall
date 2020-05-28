package com.bin.shoppingmall.service;

import javax.servlet.http.HttpSession;

public interface MallUserService {

    String register(String name ,String passWord);
    String login(String loginName, String passwordMD5,HttpSession httpSession);
}
