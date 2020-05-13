package com.bin.shoppingmall.serviceImpl;

import com.bin.shoppingmall.dao.AdminUserMapper;
import com.bin.shoppingmall.entity.AdminUser;
import com.bin.shoppingmall.service.AdminService;
import com.bin.shoppingmall.util.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    private AdminUserMapper adminUserMapper;

    @Override
    public AdminUser login(String name, String password) {
        //MD5 加密
        String passWord = MD5Util.MD5Encode(password, "UTF-8");
        return adminUserMapper.login(name, passWord);
    }

    @Override
    public AdminUser selectByPrimaryKey(Integer adminUserId) {
        return adminUserMapper.selectByPrimaryKey(adminUserId);
    }

    /**
     * 修改用户名信息
     * @param record
     * @return
     */
    @Override
    public Boolean updateByPrimaryKeySelective(AdminUser record) {
        AdminUser user = adminUserMapper.selectByPrimaryKey(record.getAdminUserId());
        if (user != null) {
            user.setLoginUserName(record.getLoginUserName());
            user.setNickName(record.getNickName());
            if (adminUserMapper.updateByPrimaryKeySelective(user) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 修改用户密码信息
     * @param id
     * @param password
     * @param newPassword
     * @return
     */
    @Override
    public Boolean updatePassWrod(Integer id, String password, String newPassword) {
        AdminUser user = adminUserMapper.selectByPrimaryKey(id);
        if (user != null) {
            String originalPasswordMd5 = MD5Util.MD5Encode(password, "UTF-8");
            String newPasswordMd5 = MD5Util.MD5Encode(newPassword, "UTF-8");
            //比较原密码是否正确
            if (originalPasswordMd5.equals(user.getLoginPassword())) {
                //设置新密码并修改
                user.setLoginPassword(newPasswordMd5);
                if (adminUserMapper.updateByPrimaryKeySelective(user) > 0) {
                    //修改成功则返回true
                    return true;
                }
            }
        }
        return false;
    }

}
