package com.bin.shoppingmall.serviceImpl;

import com.bin.shoppingmall.common.ServiceResultEnum;
import com.bin.shoppingmall.config.Constants;
import com.bin.shoppingmall.controller.vo.MallUserVO;
import com.bin.shoppingmall.dao.MallUserMapper;
import com.bin.shoppingmall.entity.MallUser;
import com.bin.shoppingmall.service.MallUserService;
import com.bin.shoppingmall.util.BeanUtil;
import com.bin.shoppingmall.util.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Service
public class MallUserServiceImpl implements MallUserService {
    @Resource
    MallUserMapper mallUserMapper;
    @Override
    public String register(String name, String passWord) {
        if(mallUserMapper.selectByLoginName(name)!=null){
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        MallUser user = new MallUser();
        user.setLoginName(name);
        user.setNickName(name);
        String passwordMD5 = MD5Util.MD5Encode(passWord, "UTF-8");
        user.setPasswordMd5(passwordMD5);
        if(mallUserMapper.insertSelective(user)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String passwordMD5, HttpSession httpSession) {
        MallUser user = mallUserMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
        if (user != null && httpSession != null) {
            if (user.getLockedFlag() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED.getResult();
            }
            //昵称太长 影响页面展示
            if (user.getNickName() != null && user.getNickName().length() > 7) {
                String tempNickName = user.getNickName().substring(0, 7) + "..";
                user.setNickName(tempNickName);
            }
            MallUserVO mallUserVO = new MallUserVO();
            BeanUtil.copyProperties(user, mallUserVO);
            httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, mallUserVO);
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }
}
