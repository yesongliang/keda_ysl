package com.kedacom.ysl.dto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.kedacom.ysl.utils.JsonUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author 95488
 * @createDate 2019年6月13日
 *
 */
@Data
@Slf4j
public class Request {

	/** 支持的请求方法 **/
	public static List<String> requestMethods = Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS");
	/** 支持的请求参数格式 **/
	public static List<String> requestContentTypes = Arrays.asList("application/x-www-form-urlencoded", "application/json");

	/** 请求路径 **/
	private String uri;
	/** 请求方法 **/
	private String method;
	/** 请求参数 **/
	private Map<String, String> params;
	/** 请求参数格式 **/
	private String contentType;

	public String getParam(String key) {
		String value = null;
		if (params == null) {
			return value;
		}
		value = params.get(key);
		return value;
	}

	/**
	 * 解析请求流
	 * 
	 * @author 95488
	 * @createDate 2019年6月13日
	 * @param input
	 *            请求流
	 */
	public String parse(InputStream input) {
		String message = null;
		/* 获取客户端输入流，就是请求过来的基本信息：请求头，换行符，请求体 */
		BufferedReader bd = new BufferedReader(new InputStreamReader(input));
		try {
			/**
			 * 解析请求URL<br/>
			 * 
			 * GET /login HTTP/1.1<br/>
			 * Host: localhost:8080<br/>
			 * Connection: keep-alive<br/>
			 * Cache-Control: max-age=0<br/>
			 * ...
			 */
			// 请求头，一行一行读取
			String requestHeader;
			// post参数长度
			int contentLength = 0;
			// 请求参数
			String requestParam = null;
			while ((requestHeader = bd.readLine()) != null && !requestHeader.isEmpty()) {
				log.debug(requestHeader);
				if (requestHeader.contains("HTTP/")) {
					method = requestHeader.substring(0, requestHeader.indexOf(" "));
					int begin = requestHeader.indexOf("/");
					int end = requestHeader.indexOf(" HTTP/");
					uri = requestHeader.substring(begin, end);
				}
				if ("GET".equalsIgnoreCase(method)) {
					int indexOf = uri.indexOf("?");
					if (indexOf != -1) {
						requestParam = uri.substring(indexOf + 1);
						uri = uri.substring(0, indexOf);
					}
				}
				// 获得请求参数内容长度
				if (requestHeader.startsWith("Content-Length:")) {
					int begin = requestHeader.indexOf("Content-Length:") + "Content-Length:".length();
					String postParamterLength = requestHeader.substring(begin).trim();
					contentLength = Integer.parseInt(postParamterLength);
				}
				// 获得请求参数格式
				if (requestHeader.startsWith("Content-Type:")) {
					int begin = requestHeader.indexOf("Content-Type:") + "Content-Type:".length();
					contentType = requestHeader.substring(begin).trim();
				}
			}
			// 获取请求参数
			if (contentLength > 0) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < contentLength; i++) {
					sb.append((char) bd.read());
				}
				requestParam = sb.toString();
			}
			log.debug("请求方法:{},请求路径:{},请求参数:{},请求参数格式:{}", method, uri, requestParam, contentType);
			// 解析请求参数
			message = parseParam(requestParam.trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			log.error("解析请求异常--格式转换,message={}", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("解析请求异常--I/O流,message={}", e.getMessage());
		}
		return message;
	}

	/**
	 * 解析请求
	 * 
	 * @author 95488
	 * @createDate 2019年6月14日
	 * @param requestParam
	 */
	private String parseParam(String requestParam) {
		String message = null;
		// 请求路径判断
		if (StringUtils.isEmpty(uri)) {
			message = "不符合http请求格式,没有解析到请求路径-url";
		}
		// 请求方法判断
		if (StringUtils.isEmpty(method)) {
			message = "不符合http请求格式,没有解析到请求方法-method";
		}
		if (!requestMethods.contains(method.toUpperCase())) {
			message = "暂不支持" + method + "请求";
		}
		// 请求参数
		if (!StringUtils.isEmpty(requestParam)) {
			if ("GET".equalsIgnoreCase(method) || "HEAD".equalsIgnoreCase(method)) {
				message = parseKey2Value(requestParam);
			} else {
				if (StringUtils.isEmpty(contentType)) {
					message = "请求参数类型为空";
				}
				// 键值对
				if (contentType.equalsIgnoreCase(requestContentTypes.get(0))) {
					message = parseKey2Value(requestParam);
					// json
				} else if (contentType.equalsIgnoreCase(requestContentTypes.get(1))) {
					JSONObject jsonObject = JsonUtil.toJSONObject(requestParam);
					if (jsonObject == null) {
						message = "请求参数格式=" + contentType + ",参数不符合json格式";
					}
					params.put("param", requestParam);
				} else {
					message = "暂不支持" + contentType + "请求参数格式";
				}
			}
		}
		return message;
	}

	/**
	 * 键值对参数解析
	 * 
	 * @author 95488
	 * @createDate 2019年6月14日
	 * @param requestParam
	 * @return
	 */
	private String parseKey2Value(String requestParam) {
		String message = null;
		params = new HashMap<>();
		String[] split = requestParam.split("&");
		for (String str : split) {
			if (StringUtils.isEmpty(str.trim())) {
				continue;
			}
			String[] split2 = str.trim().split("=");
			if (split2.length != 2) {
				message = "参数不符合传输格式";
				break;
			}
			// 参数名称不能为空
			if (StringUtils.isEmpty(split2[0].trim())) {
				message = "参数名称不能为空";
				break;
			}
			params.put(split2[0].trim(), split2[1].trim());
		}
		return message;
	}

}
