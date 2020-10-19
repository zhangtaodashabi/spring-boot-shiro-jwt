package com.yyy.common.utils;

import com.yyy.common.constants.Constant;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author luohuiqi
 * @Description:
 * @Date: 2019/12/13 9:54
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

	@Override
	public  void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(Constant.getApplicationContext() == null){
			Constant.setApplicationContext(applicationContext);
		}
	}

	public static ApplicationContext getApplicationContext() {
		return Constant.getApplicationContext();
	}

	public static Object getBean(String name){
		return getApplicationContext().getBean(name);
	}

	public static <T> T getBean(Class<T> clazz){
		return getApplicationContext().getBean(clazz);
	}

	public static <T> T getBean(String name,Class<T> clazz){
		return getApplicationContext().getBean(name, clazz);
	}

}
