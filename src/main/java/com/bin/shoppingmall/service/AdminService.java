package com.bin.shoppingmall.service;

import com.bin.shoppingmall.entity.AdminUser;
import org.apache.ibatis.annotations.Param;

public interface AdminService {
    AdminUser login(String name ,String password);
    AdminUser selectByPrimaryKey(Integer adminUserId);
    Boolean updateByPrimaryKeySelective(AdminUser record);
    Boolean updatePassWrod(Integer id,String password,String newPassword);
}
