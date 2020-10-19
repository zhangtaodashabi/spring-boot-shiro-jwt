package com.yyy.common.annotation;


import java.lang.annotation.*;

/**
* @Description: 在Controller方法上加入该注解不会转义参数，
 *  *  如果不加该注解则会：<script>alert(1)</script> --> &lt;script&gt;alert(1)&lt;script&gt;
* @Author: Mr.Liu
* @Date: 2020/8/12
*/
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface ParamXssPass {
}