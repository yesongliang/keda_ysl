package com.kedacom.ysl.controller;

import com.kedacom.ysl.annotation.MyController;
import com.kedacom.ysl.annotation.MyRequestMapping;
import com.kedacom.ysl.annotation.MyRequestParam;
import com.kedacom.ysl.vo.JsonResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@MyController
public class HomeController {

	@MyRequestMapping(value = "/login", method = "POST")
	public JsonResult<String> login(@MyRequestParam("username") String username, @MyRequestParam("password") String password) {
		log.info("---login--- username={},password={}", username, password);
		JsonResult<String> jsonResult = new JsonResult<>();
		jsonResult.setData("success");
		return jsonResult;
	}

	@MyRequestMapping(value = "/hello", method = "GET")
	public JsonResult<String> hello() {
		log.info("---hello---");
		JsonResult<String> jsonResult = new JsonResult<>();
		jsonResult.setData("hello world");
		return jsonResult;
	}

}
