package com.yyy.api.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.http.HttpStatus;

/**
 * HTTP结果封装
 *
 * @author Louis
 * @date Jan 12, 2019
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel("返回类")
public class HttpResult<T> {

	@Getter
	@Setter
	@ApiModelProperty("code")
	private int code;

	@Getter
	@Setter
	@ApiModelProperty("描述")
	private String msg;

	@Getter
	@Setter
	@ApiModelProperty("对象")
	private T data;


	public HttpResult(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public HttpResult(int code, T data) {
		this.data = data;
		this.code = code;
	}

	public static <T> HttpResult<T> error() {
		return restResult( HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}

	public static <T> HttpResult<T> error(String msg) {
		return restResult( HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}

	public static <T> HttpResult<T> error(int code, String msg) { return restResult(code, msg); }

	public static <T> HttpResult<T> ok(String msg) { return restResult( HttpStatus.SC_OK, msg); }

	public static <T> HttpResult<T> ok(T data) { return restResult(data, HttpStatus.SC_OK); }

	public static <T> HttpResult<T> ok(T data, String msg) {
		return restResult(data, HttpStatus.SC_OK, msg);
	}

	private static <T> HttpResult<T> restResult(T data, int code, String msg) {
		return new HttpResult<>(code, msg, data);
	}
	/**
	 * 解决error、ok传值设置为null出现的漏洞，新增的方法
	 */
	private static <T> HttpResult<T> restResult(int code, String msg) {
		return new HttpResult<>(code, msg);
	}
	private static <T> HttpResult<T> restResult(T data, int code) {
		return new HttpResult<>(code, data);
	}
}
