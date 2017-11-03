<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script language="javascript"
	src="${pageContext.request.contextPath}/js/jquery-1.8.0.js"></script>
<script language="javascript"
	src="${pageContext.request.contextPath}/js/login.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-easyui-v1.5/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-easyui-v1.5/jquery.easyui.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/jquery-easyui-v1.5/themes/default/easyui.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
</head>
<body>
	<input id="pageContext" type="hidden"
		value="${pageContext.request.contextPath}" />
	<center>
		<h1>欢迎登录科达聊天平台</h1>
	</center>
	<table border="0" width="100%" height="350" align="center">
		<tr height="50">
			<td></td>
		</tr>
		<tr height="20">
			<td height="62"></td>
		</tr>
		<tr height="250">
			<td width="378"></td>
			<td width="397" height="100">
				<table border="0" width="100%" height="89%">
					<tr>
						<td width="22%" height="25"><font color="#000000" face="微软雅黑"
							size="+1">用户名：</font></td>
						<td width="48%" height="25"><input type="text"
							name="username" class="username" width="150"
							style="height: 30px; font-size: 14px" maxlength=10 /></td>
						<td width="30%"></td>
					</tr>
					<tr>
						<td width="22%" height="25"><font color="#000000" face="微软雅黑"
							size="+1">密码：</font></td>
						<td width="48%" height="25"><input type="password"
							name="password" class="password" width="150"
							style="height: 30px; font-size: 14px" maxlength=10 /></td>
						<td></td>
					</tr>
					<tr height="20">
						<td width="22%" height="25"></td>
						<td width="48%" align="center" height="25"><input
							type="image" id="submit"
							src="${pageContext.request.contextPath}/images/button.png" /></td>
					</tr>
				</table>
			</td>
			<td width="286"></td>
		</tr>
	</table>
	<div style="height: 150px"></div>
	<center>
		<font size="0">copyright KeDacom software </font>
	</center>
</body>
</html>