<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="model.ConDetailBusiModel"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Contract details</title>
<link href="css/style.css" rel="stylesheet" media="screen"
	type="text/css" />
</head>

<body>
	<div class="preview">
		<%
			String name =request.getParameter("name");
		    String password =request.getParameter("password");
		    String email =request.getParameter("email");
		%>

		<div class="viewbox">
			<div class="title">
				<%=name%>
			</div>
			<div class="info">
			<form action="toUpdateUser">
				<small width="100">name:</small><input
				value=<%=name %> id="name" name="name" class="textF"/><br/>
				<small width="100">password:</small><input
				value=<%=password %> id="password" name="password" class="textF"/><br/>
				<small width="100">old email:</small><input
				value="new email" id="afterEmail" name="afterEmail" class="textF"/><br/>
				<small width="100">new email:</small><input
				value=<%=email %> id="beforeEmail" name="beforeEmail" readonly="readonly" class="textF"/><br/>
				<input
				type="submit" value="submit" class="button" />
				</form>
			</div>
		</div>
	</div>
</body>
</html>
