package com.yyy.common.utils;

import com.yyy.common.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @author luohuiqi
 * @Description:
 * @Date: 2020/6/30 16:05
 */
@Slf4j
public class EncryptorPasswordUtils {

	private static final String SALT = "G0CvDz7oJn6";

	/**
	 * 加密
	 *
	 * @param orign 原始数据
	 * @return 加密后的数据
	 */
	public static String encrypt(String orign) {
		if (ToolUtil.isEmpty(orign)) {
			throw new RuntimeException(Constant.THE_DATA_IS_WRONG);
		}
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(SALT);
		String encrypt = textEncryptor.encrypt(orign);
		log.info("{}加密后的值为：{}", orign, encrypt);
		return encrypt;
	}


	/**
	 * 数据解密
	 *
	 * @param orign 数据解密
	 * @return 解密后的数据
	 */
	public static String decrypt(String orign) {
		if (ToolUtil.isEmpty(orign)) {
			throw new RuntimeException(Constant.THE_DATA_IS_WRONG);
		}
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(SALT);
		String decrypt = textEncryptor.decrypt(orign);
		log.info("{}解密后的值为：{}", orign, decrypt);
		return decrypt;
	}




	/**
	 * 判断加密后的数据是否与原始数据匹配
	 *
	 * @param orign    原始数据
	 * @param finaData 加密后数据
	 * @return 是否匹配
	 */
	public static boolean judgeEqual(String orign, String finaData) {
		if (ToolUtil.isOneEmpty(orign, finaData)) {
			throw new RuntimeException(Constant.THE_DATA_IS_WRONG);
		}
		return orign.equals(decrypt(finaData));
	}

}
