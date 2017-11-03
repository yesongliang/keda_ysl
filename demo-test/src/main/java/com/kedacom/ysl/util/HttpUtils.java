package com.kedacom.ysl.util;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author administrator HTTP请求工具类，包含POST和GET请求,
 *         对于POST请求：包含以JSON格式和以From表单形式提交的两种POST请求
 *         如果请求出现异常或尝试，则返回包含exception字段的json数据，调用者根据此字段判断是否调用正常。
 * @see TestHttpUtils中的单元测试
 */
public final class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger("birdie");

    public static final int CONNECTION_REQUEST_TIMEOUT = 50 * 1000;
    public static final int CONNECT_TIMEOUT = 50 * 1000;
    public static final int SOCKET_TIMEOUT = 50 * 1000;

    public static final String EXCEPTION_NODE = "exception";
    public static final String CONNECTTIMEDOUT_NODE = "connect_timed_out";

    public static final String EXCEPTION_JSON_PREFIX = "{\"exception";
    public static final String EXCEPTION_XML_PREFIX = "<exception";
    public static final String HTTP_HOST_CONNECT_ERROR = "服务器无法连接";
    public static final String CONNECT_TIMED_OUT = "connect timed out";

    /**
     * HTTP Get请求
     * 
     * @param urlWithParams
     *            包含queryString的URL
     * @return jsonResponse 返回值为json格式的字符串
     */
    public static String httpGet(String urlWithParams) {
	HttpGet httpGet = null;
	try {
	    CloseableHttpClient httpclient = HttpClientBuilder.create().build();
	    httpGet = new HttpGet(urlWithParams);
	    httpGet.setConfig(config());
	    CloseableHttpResponse response = httpclient.execute(httpGet);
	    logger.info(String.format("the get request statuscode is %s for url:%s", response.getStatusLine().getStatusCode(), urlWithParams));
	    HttpEntity entity = response.getEntity();
	    String jsonResp = EntityUtils.toString(entity, "utf-8");
	    return jsonResp;
	} catch (Exception e) {
	    e.printStackTrace();
	    logger.error(String.format("[httpGet has exception] error url is %s", urlWithParams));
	    return exceptionJson(e.getMessage());
	} finally {
	    if (httpGet != null) {
		httpGet.releaseConnection();
		httpGet = null;
	    }
	}
    }

    private static String checkHttpStatusCode(int statusCode) {
	if (statusCode == 502) {
	    return exceptionJson("Bad GateWay:请确认httpagent.fcgi是否启动!");
	}
	if (statusCode == 500) {
	    return exceptionJson("Server Error:服务器错误，或apiserver进程未启动!");
	}
	if (statusCode == 504) {
	    return exceptionJson("Gateway Time-out:网关超时异常!");
	}
	if (statusCode == 404) {
	    return exceptionJson("无效的URL!");
	}
	if ((statusCode + "").startsWith("50")) {
	    return exceptionJson("Server Error:服务器错误, 请联系开发人员!");
	}
	return null;
    }

    /**
     * HTTP Post请求，以JSON格式提交请求参数
     * 
     * @param url
     *            post请求的url
     * @param jsonParams
     *            JSON格式的参数值
     * @return jsonResponse 返回值为json格式的字符串
     */
    public static String httpPostByJson(String url, String jsonParams) {
	HttpPost httpPost = null;
	try {
	    CloseableHttpClient httpclient = HttpClientBuilder.create().build();
	    httpPost = new HttpPost(url);
	    httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");
	    httpPost.setConfig(config());
	    logger.info(jsonParams);
	    httpPost.setEntity(new StringEntity(jsonParams, "UTF-8"));
	    return execHttpReqeust(url, httpPost, httpclient);
	} catch (HttpHostConnectException e) {
	    logger.error(String.format("[httpPostByJson has exception] error url is %s, the params is %s", url, jsonParams));
	    return exceptionJson(HTTP_HOST_CONNECT_ERROR, true);
	} catch (SocketTimeoutException e) {
	    e.printStackTrace();
	    logger.error(String.format("[httpPostByJson has exception] error url is %s, the params is %s", url, jsonParams));
	    return exceptionJson("访问服务器超时，请重新操作!");
	} catch (Exception e) {
	    e.printStackTrace();
	    logger.error(String.format("[httpPostByJson has exception] error url is %s, the params is %s", url, jsonParams));
	    return exceptionJson(e.getMessage());
	} finally {
	    if (httpPost != null) {
		httpPost.releaseConnection();
		httpPost = null;
	    }
	}
    }

    private static String execHttpReqeust(String url, HttpPost httpPost, CloseableHttpClient httpclient) throws IOException, ClientProtocolException {
	CloseableHttpResponse response = httpclient.execute(httpPost);
	logger.info(String.format("the post request statuscode is %s for url:%s", response.getStatusLine().getStatusCode(), url));
	if (checkHttpStatusCode(response.getStatusLine().getStatusCode()) != null) {
	    return checkHttpStatusCode(response.getStatusLine().getStatusCode());
	}
	HttpEntity entity = response.getEntity();
	String jsonResponse = EntityUtils.toString(entity, "utf-8");
	return jsonResponse;
    }

    /**
     * 错误处理不完善
     * 
     * @param url
     * @param param
     * @return
     */
    public static String httpPostWithXMl(String url, String param) {
	HttpPost httpPost = null;
	try {
	    CloseableHttpClient httpclient = HttpClientBuilder.create().build();
	    httpPost = new HttpPost(url + "?" + Math.random());
	    httpPost.setConfig(config());
	    httpPost.setEntity(new StringEntity(param, "UTF-8"));
	    CloseableHttpResponse response = httpclient.execute(httpPost);
	    response.setHeader("Content-Type", "application/xml; charset=UTF-8");
	    logger.info(String.format("the post request statuscode is %s for url:%s", response.getStatusLine().getStatusCode(), url));
	    HttpEntity entity = response.getEntity();
	    String xmlResponse = EntityUtils.toString(entity, "utf-8");
	    return xmlResponse;
	} catch (Exception e) {
	    e.printStackTrace();
	    logger.error(String.format("[httpPostWithXMl has exception] error url is %s, the params is %s", url, param));
	    return exceptionXml(e.getMessage());
	} finally {
	    if (httpPost != null) {
		httpPost.releaseConnection();
		httpPost = null;
	    }
	}
    }

    /**
     * HTTP Post请求，以Form表单形式提交请求参数
     * 
     * @param url
     *            post请求的url
     * @param params
     *            键值对格式参数值
     * @return jsonResponse 返回值为json格式的字符串
     */
    public static String httpPostByForm(String url, Map<String, String> params) {
	HttpPost httpPost = null;
	try {
	    CloseableHttpClient httpclient = HttpClientBuilder.create().build();
	    httpPost = new HttpPost(url);
	    httpPost.setConfig(config());
	    httpPost.setEntity(new UrlEncodedFormEntity(convertParams(params), "UTF-8"));
	    return execHttpReqeust(url, httpPost, httpclient);
	} catch (Exception e) {
	    e.printStackTrace();
	    logger.error(String.format("[httpPostByForm has exception] error url is %s, the params is %s", url, params.toString()));
	    return exceptionJson(e.getMessage());
	} finally {
	    if (httpPost != null) {
		httpPost.releaseConnection();
		httpPost = null;
	    }
	}
    }

    private static RequestConfig config() {
	// 配置请求的超时设置
	RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setConnectTimeout(CONNECTION_REQUEST_TIMEOUT)
		.setSocketTimeout(CONNECTION_REQUEST_TIMEOUT).build();
	return requestConfig;
    }

    public static RequestConfig buildConfig(int timeoutInMilliSecond) {
	RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeoutInMilliSecond).setConnectTimeout(timeoutInMilliSecond).setSocketTimeout(timeoutInMilliSecond).build();
	return requestConfig;
    }

    private static String exceptionJson(String errorMsg) {
	JSONObject jobj = new JSONObject();
	jobj.put(EXCEPTION_NODE, errorMsg);
	return jobj.toString();
    }

    private static String exceptionJson(String errorMsg, boolean conntectTimedOut) {
	JSONObject jobj = new JSONObject();
	jobj.put(EXCEPTION_NODE, errorMsg);
	jobj.put(CONNECTTIMEDOUT_NODE, conntectTimedOut);
	return jobj.toString();
    }

    private static String exceptionXml(String errorMsg) {
	String xml = String.format("<%s>%s</%s>", EXCEPTION_NODE, errorMsg, EXCEPTION_NODE);
	return xml;
    }

    public static boolean isException(String responseString) {
	if (responseString != null) {
	    if (responseString.startsWith(EXCEPTION_JSON_PREFIX) || responseString.startsWith(EXCEPTION_XML_PREFIX)) {
		return true;
	    }
	}
	return false;
    }

    private static List<NameValuePair> convertParams(Map<String, String> params) {
	List<NameValuePair> nvPairList = new ArrayList<NameValuePair>();
	Iterator<Entry<String, String>> its = params.entrySet().iterator();
	while (its.hasNext()) {
	    Entry<String, String> entry = its.next();
	    nvPairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
	}
	return nvPairList;
    }

}
