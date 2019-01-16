<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'login.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->


    <link rel="stylesheet" type="text/css" href="css/webbase.css" />
    <link rel="stylesheet" type="text/css" href="css/pages-login-manage.css" />

  </head>
  
  <body>
    <div class="loginmanage">
		<div class="py-container">
			<h4 class="manage-title"></h4>
			<div class="loginform">

				<ul class="sui-nav nav-tabs tab-wraped" style="text-align:center">
					<h3>用户登录</h3>
				</ul>
				
				<div class="tab-content tab-wraped">
					
					<div id="profile" class="tab-pane  active">
						<form class="sui-form" action="Login" method="post">
							<div class="input-prepend"><span class="add-on loginname"></span>
								<input id="prependedInput1" name="username" type="text" placeholder="用户名" class="span2 input-xfat">
								
							</div>
							<div class="input-prepend"><span class="add-on loginpwd"></span>
								<input id="prependedInput2" name="userpass" type="password" placeholder="请输入密码" class="span2 input-xfat">
							</div>
							<div class="setting">
								 <div id="slider">
									<div id="slider_bg"></div>
									<span id="label">>></span> <span id="labelTip">拖动滑块验证</span>
									</div>
							</div>
							<div class="logined">
								<input type="submit" class="sui-btn btn-block btn-xlarge btn-danger" value="登&nbsp;&nbsp;录">
							</div>
						</form>

					</div>
				</div>
			</div>
			<div class="clearfix"></div>
		</div>
	</div>


	<!--foot-->
	<div class="py-container copyright">
		<ul>
			<li>关于我们</li>
			<li>联系我们</li>
			<li>联系客服</li>
			<li>商家入驻</li>
			<li>营销中心</li>
			<li>手机</li>
			<li>销售联盟</li>
			<li>社区</li>
		</ul>
		<div class="address">地址：北京市昌平区建材城 邮编：100096 电话：400-618-4000 传真：010-82935100</div>
		<div class="beian">京ICP备08001421号京公网安备110108007702
		</div>
	</div>

  </body>
</html>
