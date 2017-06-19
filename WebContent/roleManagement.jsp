<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="model.Role"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/style.css" rel="stylesheet" media="screen"
	type="text/css" />
<title>User permission list</title>
</head>

<body>
	<div class="mtitle">Role list</div>

	<div class="list">
		<table>
			<tr>
				<th width="200px">Role id</th>
				<th width="200px">Role name</th>
				<th width="200px">Role FuncIds</th>
				<th width="200px">Role Description</th>
				<th width="400px">Options</th>
			</tr>
			<%
					List<Role> userList = (List<Role>) request.getAttribute("roles");
					for (Role pbm : userList) {
				%>
			<tr>
				<td align="center"><%=pbm.getId() %></td>
				<td align="center"><%=pbm.getName() %></td>
				<td align="center"><%=pbm.getFuncIds() %></td>
				<td align="center"><%=pbm.getDescription() %></td>
				<td align="center"><a href="toDeleteRole?delRoleId=<%=pbm.getId()%>"><img alt="delete" src="images/delete.png" width="15" height="15">Del</a>
			<%
					}
				%>
			<tr>
			    <td align="center"><a href="addRole.jsp?id=-1">Add</a></td>
				<td colspan="3"></td>
			</tr>
		</table>
	</div>

	<div align="left" class="pagelist">
		 Total&nbsp;<strong><%=userList.size()%></strong>&nbsp;Users
	</div>
</body>
</html>
