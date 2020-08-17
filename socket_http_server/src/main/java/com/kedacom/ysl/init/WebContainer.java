package com.kedacom.ysl.init;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.StringUtils;

import com.kedacom.ysl.annotation.MyController;
import com.kedacom.ysl.annotation.MyRequestMapping;
import com.kedacom.ysl.annotation.MyRequestParam;
import com.kedacom.ysl.dto.Request;
import com.kedacom.ysl.dto.Response;
import com.kedacom.ysl.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * web容器
 * 
 * @author 95488
 * @createDate 2019年6月13日
 *
 */
@Slf4j
public class WebContainer {

	private List<String> classNames = new ArrayList<>();

	private Map<String, Object> ioc = new HashMap<>();

	private Map<String, Method> handlerMapping = new HashMap<>();

	private Map<String, Object> controllerMap = new HashMap<>();

	public static WebContainer webContainer = new WebContainer();

	private WebContainer() {

	}

	public static WebContainer getWebContainer() {
		return webContainer;
	}

	/**
	 * 初始化web容器
	 * 
	 * @author 95488
	 * @createDate 2019年6月13日
	 * @param packageName 扫描路径
	 */
	public void init(String packageName) {
		doScanner(packageName);
		doInstance();
		initHandlerMapping();
	}

	/**
	 * 处理请求
	 * 
	 * @author 95488
	 * @createDate 2019年6月13日
	 * @param request
	 * @param response
	 */
	public void doDispatch(Request request, Response response) {
		if (handlerMapping.isEmpty()) {
			response.writeHtml(404, "Not Found", "server cannot handle any request");
			return;
		}
		String url = request.getUri();
		if (!this.handlerMapping.containsKey(url)) {
			response.writeHtml(404, "Not Found", "The requested URL " + url + " was not found on this server.");
			return;
		}
		Method method = this.handlerMapping.get(url);
		MyRequestMapping methodAnnotation = method.getAnnotation(MyRequestMapping.class);
		String requestMethod = methodAnnotation.method();
		if (!StringUtils.isEmpty(requestMethod)) {
			if (!requestMethod.equalsIgnoreCase(request.getMethod())) {
				response.writeHtml(400, "Bad Request", "request:" + url + "不支持请求方法:" + request.getMethod());
				return;
			}
		}
		String consume = methodAnnotation.consume();
		// 获取方法的参数列表
		Class<?>[] parameterTypes = method.getParameterTypes();

		Parameter[] parameters = method.getParameters();

		// 保存参数值
		Object[] paramValues = new Object[parameterTypes.length];

		// 方法的参数列表
		for (int i = 0; i < parameterTypes.length; i++) {
			Class<?> paramClass = parameterTypes[i];
			Parameter parameter = parameters[i];
			if (!parameter.isAnnotationPresent(MyRequestParam.class)) {
				continue;
			}
			// 根据参数名称，做某些处理
			String requestParam = paramClass.getName();
			if (consume.equalsIgnoreCase(Request.requestContentTypes.get(0))) {
				if (requestParam.equals("java.lang.String")) {
					MyRequestParam paramAnnotation = parameter.getAnnotation(MyRequestParam.class);
					String paramName = paramAnnotation.value();
					paramValues[i] = request.getParam(paramName);
				} else {
					// TODO
					response.writeHtml(400, "Bad Request", "The server cannot or will not process the request due to .....\\n" + "接口编写格式,协议暂不支持");
					return;
				}
			} else {
				if (requestParam.equals("java.lang.String")) {
					paramValues[i] = request.getParam("param");
				} else {
					String json = request.getParam("param");
					Object object = JsonUtil.getObject(json, paramClass);
					if (object == null) {
						log.error("参数转化错误");
						response.writeHtml(400, "Bad Request", "The server cannot or will not process the request due to .....\\n" + "参数转化错误");
						return;
					}
					paramValues[i] = object;
				}
			}
		}
		// 利用反射机制来调用
		try {
			Object invoke = method.invoke(this.controllerMap.get(url), paramValues);
			response.writeJson(200, "Ok", invoke);
		} catch (Exception e) {
			e.printStackTrace();
			response.writeHtml(500, "Internal Server Error", e.getMessage());
		}
	}

	/**
	 * 扫描获取class
	 * 
	 * @author 95488
	 * @createDate 2019年6月13日
	 * @param packageName 扫描包路径
	 */
	private void doScanner(String packageName) {
		// 获取项目路径
		String projectPath = getClass().getClassLoader().getResource("").toString();
		// ubuntu
		if (projectPath.contains(":")) {
			projectPath = projectPath.replace("file:", "");
		}
		log.debug("项目路径,projectPath={}", projectPath);
		// 把所有的.替换成/
		File dir = new File(projectPath + packageName.replaceAll("\\.", File.separator));
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				// 递归读取包
				doScanner(packageName + "." + file.getName());
			} else {
				String className = packageName + "." + file.getName().replace(".class", "");
				classNames.add(className);
				log.debug("className={}", className);
			}
		}
	}

	/**
	 * 获取web bean
	 * 
	 * @author 95488
	 * @createDate 2019年6月13日
	 */
	private void doInstance() {
		if (classNames.isEmpty()) {
			return;
		}
		for (String className : classNames) {
			try {
				// 把类搞出来,反射来实例化(只有加@MyController需要实例化)
				Class<?> clazz = Class.forName(className);
				if (clazz.isAnnotationPresent(MyController.class)) {
					ioc.put(toLowerFirstWord(clazz.getSimpleName()), clazz.newInstance());
					log.debug("beanName={}", clazz.getSimpleName());
				} else {
					continue;
				}

			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		classNames = null;
	}

	/**
	 * 建立请求url与方法的映射关系
	 * 
	 * @author 95488
	 * @createDate 2019年6月13日
	 */
	private void initHandlerMapping() {
		if (ioc.isEmpty()) {
			return;
		}
		try {
			for (Entry<String, Object> entry : ioc.entrySet()) {
				Class<? extends Object> clazz = entry.getValue().getClass();
				if (!clazz.isAnnotationPresent(MyController.class)) {
					continue;
				}
				// 拼url时,是controller头的url拼上方法上的url
				String baseUrl = "";
				if (clazz.isAnnotationPresent(MyRequestMapping.class)) {
					MyRequestMapping annotation = clazz.getAnnotation(MyRequestMapping.class);
					baseUrl = annotation.value();
				}
				Method[] methods = clazz.getMethods();
				for (Method method : methods) {
					if (!method.isAnnotationPresent(MyRequestMapping.class)) {
						continue;
					}
					MyRequestMapping annotation = method.getAnnotation(MyRequestMapping.class);
					String url = annotation.value();
					url = (baseUrl + "/" + url).replaceAll("/+", "/");
					if (handlerMapping.containsKey(url)) {
						log.error("RequestMapping={} is alrealy exist!", url);
						System.exit(1);
					}
					handlerMapping.put(url, method);
					log.debug("handlerMapping={}", url);
					controllerMap.put(url, clazz.newInstance());
					log.debug("controllerMap={}", url);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ioc = null;
	}

	/**
	 * 把字符串的首字母小写
	 * 
	 * @param name
	 * @return
	 */
	private String toLowerFirstWord(String name) {
		char[] charArray = name.toCharArray();
		charArray[0] += 32;
		return String.valueOf(charArray);
	}

}
