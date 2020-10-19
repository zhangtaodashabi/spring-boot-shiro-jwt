package com.yyy.common.utils;


import com.yyy.common.exception.SystemException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

/**
 * @author GKQ
 * @Classname CheckParamUtils
 * @Description 验证参数工具类
 * @Date 2020/4/16
 */
public class AssertUtils {



	private AssertUtils() {
		throw new IllegalStateException("Utility class");
	}
	/**
	 * @param param
	 * @param message
	 * @return
	 * @Description 验证字符串类是否是整数
	 * @auther GKQ
	 */
	public static void verifyStrOfInt(String param, String message) {
		if (!NumberUtils.isInteger(param)) {
			throw new SystemException(message, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * @param param
	 * @param message
	 * @return
	 * @Description 验证对象参数
	 * @auther GKQ
	 */
	public static void notNull(Object param, String message) {
		if (Objects.isNull(param)) {
			throw new SystemException(message, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * @param param
	 * @param message
	 * @return
	 * @Description 验证对象参数
	 * @auther GKQ
	 */
	public static void notEmpty(String param, String message) {
		if (StringUtils.isBlank(param)) {
			throw new SystemException(message, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * @param id
	 * @param message
	 * @return
	 * @Description 验证ID是否规范
	 * @auther GKQ
	 */
	public static void matcherId(Long id, String message) {
		if (Objects.isNull(id)) {
			throw new SystemException(message, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		if (id <= 0) {
			throw new SystemException("格式不正确", HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * @param list
	 * @param message
	 * @return
	 * @Description 验证对象参数
	 * @auther GKQ
	 */
	public static void verifyListEmpty(List<?> list, String message) {
		if (CollectionUtils.isEmpty(list)) {
			throw new SystemException(message, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * @param list
	 * @param message
	 * @return
	 * @Description 验证对象参数
	 * @auther GKQ
	 */
	public static void verifyListNotEmpty(List<?> list, String message) {
		if (CollectionUtils.isNotEmpty(list)) {
			throw new SystemException(message, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * @param count
	 * @param message
	 * @return
	 * @Description 验证统计数字是 > 0
	 * @auther GKQ
	 */
	public static void verifyCount(Integer count, String message) {
		if (count > 0) {
			throw new SystemException(message, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}


}
