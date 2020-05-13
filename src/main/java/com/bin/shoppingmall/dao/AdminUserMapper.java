package com.bin.shoppingmall.dao;

import com.bin.shoppingmall.entity.AdminUser;
import org.apache.ibatis.annotations.Param;

public interface AdminUserMapper {

    /**
     * 查询用户信息用于页面回显
     *
     * @param adminUserId
     * @return
     */
    AdminUser selectByPrimaryKey(Integer adminUserId);

    /**
     * 修改用户信息
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(AdminUser record);

    int updateByPrimaryKey(AdminUser record);

    /**
     * 登录
     *
     * @param name
     * @param password
     * @return
     */
    AdminUser login(@Param("userName") String name, @Param("passWord") String password);

}