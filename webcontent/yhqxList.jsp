<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.ruanko.model.PermissionBusiModel;"%>
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
				<th>User name</th>
				<th class="th2">Role name</th>
				<th class="th2">Operation</th>
			</tr>
			<%
					List<PermissionBusiModel> permissionList = (List<PermissionBusiModel>) request
							.getAttribute("permissionList");
					for (PermissionBusiModel pbm : permissionList) {
				%>
			<tr>
				<td class="tdname"><%=pbm.getUserName()%></td>
				<td><%=pbm.getRoleName()%></td>
				<td><a
					href="toAssignPerm?userId=<%=pbm.getUserId()%>&uName=<%=pbm.getUserName()%>&roleId=<%=pbm.getRoleId()%>">
						<img src="images/cog_edit.png" alt="Authorize" /> Authorize
				</a></td>
			</tr>
			<%
					}
				%>
			<tr>
				<td colspan="3"></td>
			</tr>
		</table>
	</div>

	<div align="right" class="pagelist">
		<a href="#"><img src="images/page/first.png" alt="" /></a> &nbsp; <a
			href="#"><img src="images/page/pre.png" alt="" /></a>&nbsp; <a
			href="#"><img src="images/page/next.png" alt="" /></a>&nbsp; <a
			href="#"><img src="images/page/last.png" alt="" /></a>&nbsp; <span
			class="pageinfo"> Total&nbsp;<strong>2</strong>&nbsp;pages&nbsp;<strong>13</strong>&nbsp;records
		</span>
	</div>
</body>
</html>
