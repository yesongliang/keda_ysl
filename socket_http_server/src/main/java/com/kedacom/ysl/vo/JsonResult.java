package com.kedacom.ysl.vo;

import lombok.Data;

@Data
public class JsonResult<T> {

	private int code;
	private String message;
	private T data;

	public static JsonResult<?> OK() {
		return new JsonResult<>();
	}

	public static JsonResult<?> ERROR(int code, String message) {
		JsonResult<?> jsonResult = new JsonResult<>();
		jsonResult.setCode(code);
		jsonResult.setMessage(message);
		return jsonResult;
	}
}
