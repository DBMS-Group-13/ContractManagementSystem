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
		    int id = Integer.parseInt(request.getParameter("id"));
			String name =request.getParameter("name");
		    String number =request.getParameter("number");
		    String address =request.getParameter("address");
		    String fax =request.getParameter("fax");
		    String tel =request.getParameter("tel");
		    String code =request.getParameter("code");
		    String account =request.getParameter("account");
		    String bank =request.getParameter("bank");
		%>

		<div class="viewbox">
			<div class="title">
				<%=name %>
			</div>
			<div class="info">
			<form action="toUpdateCustomer">
				<small width="100">id:</small><input
				value=<%=id %> id="id" name="id" class="textF" readonly="readonly"/><br/>
				<small width="100">name:</small><input
				value=<%=name %> id="name" name="name" class="textF"/><br/>
				<small width="100">number:</small><input
				value=<%=number %> id="num" name="num" class="textF"/><br/>
				<small width="100">address:</small><input
				value=<%=address %> id="address" name="address" class="textF"/><br/>
				<small width="100">fax:</small><input
				value=<%=fax %> id="fax" name="fax" class="textF"/><br/>
				<small width="100">tel:</small><input
				value=<%=tel %> id="tel" name="tel" class="textF"/><br/>
				<small width="100">code:</small><input
				value=<%=code %> id="code" name="code" class="textF"/><br/>
				<small width="100">account:</small><input
				value=<%=account %> id="account" name="account" class="textF"/><br/>
				<small width="100">bank:</small><input
				value=<%=bank %> id="bank" name="bank" class="textF"/><br/>
				<input
				type="submit" value="submit" class="button" />
				</form>
			</div>
		</div>
	</div>
</body>
</html>
