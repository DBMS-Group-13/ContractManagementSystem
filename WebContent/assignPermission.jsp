<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page
	import="model.PermissionBusiModel,model.Role"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Configure Permission</title>
		<link href="css/style.css" rel="stylesheet" media="screen"
			type="text/css" />
	</head>

	<body>
		<div class="mtitle">
			Configure permission
		</div>
		<br />
		<form action="assignPerm" method="post">
			<%
				PermissionBusiModel permission = (PermissionBusiModel) request
						.getAttribute("permission");
			%>
			<input type="hidden" name="userId" value="<%=permission.getUserId()%>">
			<table class="update" style="width:500px;">
				<tr height="28">
					<td width="180px">
						User name:
					</td>
					<td><%=permission.getUserName()%></td>
				</tr>
				<tr>
					<td>
						Configure permission:
					</td>
					<td>
						<!-- Get user's role id that already had-->
						<%
							int uRoleId = permission.getRoleId();
						%>
						
						<!--Display all role list,and select the user's role that already had -->
						<%
							List<Role> roleList = (List<Role>) request.getAttribute("roleList");
							for (Role role : roleList) {
						%>
						<input name="roleId" type="radio" value="<%=role.getId()%>"
							<%if (uRoleId == role.getId()) {%> checked="checked" <%} %> />
						<%=role.getName()%>
						<br />
						<%} %>
					</td>
				</tr>

				<tr height="28">
					<td align="center" colspan="2">
						<input type="submit" value="Submit" class="button">
						&nbsp; &nbsp; &nbsp;
						<input type="reset" value="Reset" class="button">
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>