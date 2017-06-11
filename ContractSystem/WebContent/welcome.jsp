<%@ page language="java" import="java.util.Date,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Contract Management System - Welcome</title>
		<link href="css/frame.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<div class="content">
			<p>
			Welcome to Contract Management System!
			<br>			
			<%
				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//Parameter is time format you need
			%>
			Current time:<%=sdf.format(now)%>
			</p>
		</div>
	</body>
</html>