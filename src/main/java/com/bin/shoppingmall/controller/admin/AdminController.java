package com.bin.shoppingmall.controller.admin;

import com.bin.shoppingmall.entity.AdminUser;
import com.bin.shoppingmall.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminservice;

    @GetMapping({"/login"})
    public String login() {
        return "admin/login";
    }

    //安全退出
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        //清除缓存
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("errorMsg");
        return "admin/login";
    }

    /**
     * 编辑页面
     * 在这一步通过id获取用户信息用于回显
     * @param request
     * @return
     */
    @GetMapping("/profile")
    public String profile(HttpServletRequest request) {
        Integer id = (Integer) request.getSession().getAttribute("loginUserId");
        AdminUser user = adminservice.selectByPrimaryKey(id);
        if (user == null) {
            return "admin/login";
        }
        request.setAttribute("path", "profile");
        request.setAttribute("loginUserName", user.getLoginUserName());
        request.setAttribute("nickName", user.getNickName());
        return "admin/profile";
    }

    /**
     * 修改用户名和昵称
     * @param request
     * @param userName
     * @param nickName
     * @return
     */
    @PostMapping("/profile/name")
    @ResponseBody
    public String updateName(HttpServletRequest request,
                             @RequestParam("loginUserName")
                             String userName, @RequestParam("nickName")
                             String nickName) {
        //获取用户id，用于修改当前用户
        Integer id = (Integer) request.getSession().getAttribute("loginUserId");
        AdminUser user = new AdminUser();
        user.setAdminUserId(id);
        user.setNickName(nickName);
        user.setLoginUserName(userName);

        if (adminservice.updateByPrimaryKeySelective(user)) {
            return "success";
        } else {
            return "修改失败";
        }

    }

    /**
     * 修改用户密码
     * @param request
     * @param password 原密码
     * @param newpassword 新密码
     * @return
     */
    @PostMapping("/profile/password")
    @ResponseBody
    public String updatePassword(HttpServletRequest request,
                                 @RequestParam("originalPassword") String password,
                                 @RequestParam("newPassword") String newpassword) {
        Integer id = (Integer) request.getSession().getAttribute("loginUserId");
        AdminUser user = new AdminUser();
        user.setLoginPassword(newpassword);
        user.setAdminUserId(id);
        if (adminservice.updatePassWrod(id, password, newpassword)) {
            //修改成功后，清除所有缓存信息
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("loginUser");
            request.getSession().removeAttribute("errorMsg");
            return "success";
        } else {
            return "修改失败";
        }


    }

    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "index");
        return "admin/index";
    }

    /**
     * 登录
     * @param name  姓名
     * @param password 密码
     * @param session  session
     * @return 登录成功，返回后台页面
     */
    @PostMapping(value = "/login")
    public String login(@RequestParam("userName") String name,
                        @RequestParam("password") String password,
                        HttpSession session) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            session.setAttribute("errorMsg", "账号和密码不能为空");
        }
        AdminUser user = adminservice.login(name, password);
        if (user != null) {
            //存一个别名，一个id，用于后台显示及后续回显操作
            session.setAttribute("loginUser", user.getNickName());
            session.setAttribute("loginUserId", user.getAdminUserId());
            //session过期时间设置为7200秒 即两小时
            session.setMaxInactiveInterval(60 * 60 * 2);
            //重定向到 index
            return "redirect:/admin/index";
        } else {
            session.setAttribute("errorMsg", "用户名或密码错误");
            return "admin/login";
        }

    }
}
