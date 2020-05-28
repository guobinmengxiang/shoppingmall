package com.bin.shoppingmall.controller.Lmall;

import com.bin.shoppingmall.config.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class PersonalController {
    /**
     * 登陆页面跳转
     * @return
     */
    @GetMapping({"/login", "login.html"})
    public String loginPage() {
        return "Lmall/login";
    }

    /**
     * 注册页面跳转
     * @return
     */
    @GetMapping({"/register", "register.html"})
    public String registerPage() {
        return "Lmall/register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute(Constants.MALL_USER_SESSION_KEY);
        return "Lmall/login";
    }
}
