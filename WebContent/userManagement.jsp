<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="model.PermissionBusiModel"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/style.css" rel="stylesheet" media="screen"
	type="text/css" />
<title>User permission list</title>
</head>

<body>
	<div class="mtitle">User permission list</div>

	<div class="search">
		<form>
			Search user: <input value="Enter search conditions.." />
			&nbsp;&nbsp; <input type="submit" value="Search"
				class="search-submit" /> <br />
		</form>
	</div>

	<div class="list">
		<table>
			<tr>
				<th width="200px">User name</th>
				<th width="200px">Role name</th>
				<th width="200px">Operation</th>
			</tr>
			<%
					List<PermissionBusiModel> permissionList = (List<PermissionBusiModel>) request
							.getAttribute("permissionList");
					for (PermissionBusiModel pbm : permissionList) {
				%>
			<tr>
				<td align="center"><%=pbm.getUserName()%></td>
				<td align="center"><%=pbm.getRoleName()%></td>
				<td align="center"><a
					href="toAssignPerm?userId=<%=pbm.getUserId()%>&uName=<%=pbm.getUserName()%>&roleId=<%=pbm.getRoleId()%>">
						<img src="images/cog_edit.png" alt="Authorize" /> Authorize </a>| 
						<a href="toDeleteUser?userId=<%=pbm.getUserId()%>"><img alt="delete" src="images/delete.png">Del</a>
				</td>
			</tr>
			<%
					}
				%>
			<tr>
				<td colspan="3"></td>
			</tr>
		</table>
	</div>

	<div align="left" class="pagelist">
		 Total&nbsp;<strong><%=permissionList.size()%></strong>&nbsp;Users
	</div>
</body>
</html>
