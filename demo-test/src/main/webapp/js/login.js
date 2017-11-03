// 工程名（根目录）
var pageContext;
$(function() {
	pageContext = $("#pageContext").val();
	$("#submit").click(
			function() {
				var chars = /^[`~!@#$%^&*()+=|{}':;',.<>\/ ]+$/;
				var username = $(".username").val();
				var password = $(".password").val();
				var json = {
					username : username,
					password : password
				};
				if (username != "" && password != "") {
					if (chars.test(username) || chars.test(password)) {
						$.messager.alert("警告", "用户名或密码错误！", "warning");
						return;
					}
					$.ajax({
						type : "POST",
						url : pageContext + "/test/login",
						data : json,
						dataType : "json",
						success : function(data) {
							if (data.result == "0") {
								$.messager.alert("警告", "用户名不存在", "warning");
								$(".password").val("");
							} else if (data.result == "1") {
								$.messager.alert("警告", "密码错误", "warning");
								$(".password").val("");
							} else if (data.result == "2") {
								// 跳转到主页面
								window.location.href = pageContext
										+ "/test/chatRoom/?username="
										+ username + "";
							} else if (data.result == "3") {
								$.messager.alert("警告", "请勿重复登录", "warning");
								$(".password").val("");
							} else if (data.result == "4") {
								$.messager.alert("提示", "一个浏览器只能登录一个用户！",
										"info", function() {
											window.close();
										});
							}
						}
					});
				} else {
					$.messager.alert("警告", "请输入用户名和密码！", "warning");
				}
			}).mouseover(function() {
		$("#submit").attr("src", pageContext + "/images/btndown.png");
	}).mouseout(function() {
		$("#submit").attr("src", pageContext + "/images/button.png");
	});
});
