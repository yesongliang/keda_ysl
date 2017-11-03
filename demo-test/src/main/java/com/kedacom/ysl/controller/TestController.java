package com.kedacom.ysl.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kedacom.ysl.model.User;
import com.kedacom.ysl.service.TestService;
import com.kedacom.ysl.util.Base64;
import com.kedacom.ysl.util.MD5Util;

@Controller
@RequestMapping(value = "test")
public class TestController {
    private static Logger logger = LoggerFactory.getLogger("ysl_test");

    @Autowired
    private TestService testService;

    @RequestMapping("/info")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
	model.addAttribute("ysl", "xxx");
	return "info";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject login(String username, String password) {
	JSONObject data = new JSONObject();
	// 测试aop
	User user = testService.login(username);
	if (user != null) {
	    // 密码是否正确
	    if (MD5Util.GetMD5Code(password).equals(user.getPassword())) {
		logger.info(String.format("[UserController] login, 登录成功"));
		// 登录成功
		data.put("result", "2");
	    } else {
		logger.error(String.format("[UserController] login, 密码错误"));
		// 密码错误
		data.put("result", "1");
	    }
	} else {
	    logger.error(String.format("[UserController] login, 用户名不存在"));
	    // 用户名不存在
	    data.put("result", "0");
	}
	return data;
    }

    @RequestMapping(value = "/chatRoom", method = RequestMethod.GET)
    public String login(String username, Model model) {
	model.addAttribute("username", username);
	User user = testService.login(username);
	String photo = Base64.encode(user.getPhoto());
	model.addAttribute("photo", photo);
	return "chatMain";
    }
}
