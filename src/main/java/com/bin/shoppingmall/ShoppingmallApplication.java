package com.bin.shoppingmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//mvn mybatis-generator:generate
@SpringBootApplication
@MapperScan("com.bin.shoppingmall.dao") //添加 @Mapper 注解
public class ShoppingmallApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingmallApplication.class, args);
    }

}
