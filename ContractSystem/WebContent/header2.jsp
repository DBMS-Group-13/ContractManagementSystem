<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Contract Management System - Head</title>
		<link href="css/frame.css" rel="stylesheet" type="text/css" />
	</head>

	<body>
		<div class="header">
			<div class="toplinks">
				<%
					String userName = (String) session.getAttribute("userName");
				%>
				<span>Hello:<%=userName%>，Welcome to Contract Management System [<a href="logout"
					target="_top">Logout</a>]</span>
			</div>
			<h1>
				<img src="images/logo_title.png" alt="Contract Management System" />
			</h1>
		</div>
	</body>
</html>