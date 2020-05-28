package com.bin.shoppingmall.controller.Lmall;

import com.bin.shoppingmall.common.ServiceResultEnum;
import com.bin.shoppingmall.service.MallUserService;
import com.bin.shoppingmall.util.MD5Util;
import com.bin.shoppingmall.util.Result;
import com.bin.shoppingmall.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LmallPersonalController {
    @Autowired
    MallUserService mallUserService;

    @RequestMapping("/register")
    @ResponseBody
    public Result register(@RequestParam("loginName") String name, @RequestParam String password) {
        if (StringUtils.isEmpty(name)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_NULL.getResult());
        }
        if (StringUtils.isEmpty(password)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_PASSWORD_NULL.getResult());
        }
        String result = mallUserService.register(name, password);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        }
        //注册失败
        return ResultGenerator.genFailResult(result);

    }

    @RequestMapping("/login")
    @ResponseBody
    public Result login(@RequestParam("loginName") String name, @RequestParam String password, HttpSession httpSession) {
        if (StringUtils.isEmpty(name)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_NULL.getResult());
        }
        if (StringUtils.isEmpty(password)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_PASSWORD_NULL.getResult());
        }
        String loginResult = mallUserService.login(name, MD5Util.MD5Encode(password, "UTF-8"), httpSession);
        //登录成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(loginResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //登录失败
        return ResultGenerator.genFailResult(loginResult);
    }
}
