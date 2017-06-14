<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
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
			Search user: <input placeholder="Enter search conditions.." class="textF" />
			&nbsp;&nbsp; <input type="submit" value="Search"
				class="search-submit" /> <br />
		</form>
	</div>

	<div class="list">
		<table>
			<tr>
				<th width="200px">User name</th>
				<th width="200px">User password</th>
				<th width="200px">User email</th>
				<th width="200px">User MD5</th>
				<th width="200px">User createDate</th>
				<th width="200px">User status</th>
				<th width="400px">Options</th>
			</tr>
			<%
					List<User> userList = (List<User>) request.getAttribute("users");
					for (User pbm : userList) {
				%>
			<tr>
				<td align="center"><%=pbm.getName() %></td>
				<td align="center"><%=pbm.getPassword()%></td>
				<td align="center"><%=pbm.getEmail()%></td>
				<td align="center">MD5:<%=pbm.getToken()%></td>
				<td align="center"><%=pbm.getCreateDate()%></td>
				<%if(pbm.getStatus()==0){%>
					<td align="center">用户待激活</td>
				<%}else{%>
				    <td align="center">已经激活</td>
				<%} %>
				<td align="center"><a href="toDeleteUser?delUserId=<%=pbm.getId()%>"><img alt="delete" src="images/delete.png" width="15" height="15">Del</a> | <a href="userUpdate.jsp?name=<%=pbm.getName()%>&password
				=<%=pbm.getPassword()%>&email=<%=pbm.getEmail()%>"><img alt="update" src="images/cog_edit.png" width="15" height="15">update</a>
			<%
					}
				%>
			<tr>
				<td colspan="3"></td>
			</tr>
		</table>
	</div>

	<div align="left" class="pagelist">
		 Total&nbsp;<strong><%=userList.size()%></strong>&nbsp;Users
	</div>
</body>
</html>
