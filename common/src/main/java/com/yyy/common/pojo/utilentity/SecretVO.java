package com.yyy.common.pojo.utilentity;

import lombok.Data;

/**
 * @program: hospitalKpi
 * @description: 所有的vo都继承的类，传输加密
 * @author: Mr.Liu
 * @create: 2020-09-19 14:01
 **/
@Data
public class SecretVO {
    // 加密密文
    private String encryptStr;
}
