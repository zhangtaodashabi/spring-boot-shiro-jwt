package com.yyy.system;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author liu
 */
@SpringBootApplication
@EnableSwagger2
@ComponentScan({"com.yyy.common","com.yyy.system"})
@MapperScan({"com.yyy.common.dao","com.yyy.system.dao"})
@EnableAspectJAutoProxy()
public class SystemManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemManagementApplication.class, args);
    }
}