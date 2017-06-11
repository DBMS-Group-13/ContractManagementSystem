<%@ page language="java"
	import="java.util.Date,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Contract Management System - New User</title>
<link href="css/frame.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<div class="header">
		<div class="toplinks">
			<%
					String name = (String) session.getAttribute("userName");
				%>
			<span>Hello:<%=name%>,Welcome to contract management system! [<a
				href="logout" target="_top">Logout</a>]
			</span>
		</div>
		<h1>
			<img src="images/logo_title.png" alt="Contract Management System" />
		</h1>
	</div>

	<div class="content">
		<p>
			You hava no contract operation privileges,<br /> please waiting for
			the administrators to configure permissions for you! <br />
			<%
					Date now = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				%>
			Current time：<%=sdf.format(now)%>
		</p>
	</div>

	<div class="footer">
		<ul>
			<li><a target="_blank" href="#">Contract Management System</a></li>
			<li>｜</li>
			<li><a target="_blank" href="#">Help</a></li>
		</ul>

		<p>
			Copyright&nbsp;&copy;&nbsp;Ruanko COE &nbsp; <a
				href="http://www.ruanko.com" title="www.ruanko.com" target="_blank"><strong>www.ruanko.com</strong>
			</a>&nbsp;Copyright Reserved
		</p>
	</div>
</body>
</html>