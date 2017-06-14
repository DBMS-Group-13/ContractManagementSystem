<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="model.Customer"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/style.css" rel="stylesheet" media="screen"
	type="text/css" />
<title>User permission list</title>
</head>

<body>
	<div class="mtitle">Customer list</div>

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
				<th width="200px">Customer Id</th>
				<th width="200px">Customer Name</th>
				<th width="200px">Customer Number</th>
				<th width="200px">Customer Address</th>
				<th width="200px">Customer Tel</th>
				<th width="200px">Customer Fax</th>
				<th width="200px">Customer Code</th>
				<th width="200px">Customer Bank</th>
				<th width="200px">Customer Account</th>
				<th width="400px">Options</th>
			</tr>
			<%
					List<Customer> userList = (List<Customer>) request.getAttribute("customers");
					for (Customer pbm : userList) {
				%>
			<tr>
				<td align="center"><%=pbm.getId()%></td>
				<td align="center"><%=pbm.getName() %></td>
				<td align="center"><%=pbm.getNum() %></td>
				<td align="center"><%=pbm.getAddress() %></td>
				<td align="center"><%=pbm.getTel() %></td>
				<td align="center"><%=pbm.getFax() %></td>
				<td align="center"><%=pbm.getCode() %></td>
				<td align="center"><%=pbm.getBank() %></td>
				<td align="center"><%=pbm.getAccount() %></td>
				<td align="center"><a href="toDeleteCustomer?delCustomerId=<%=pbm.getId()%>"><img alt="delete" src="images/delete.png" width="15" height="15">Del</a> | <a 
				href="customerUpdate.jsp?id=<%=pbm.getId()%>&name=<%=pbm.getName()%>&number=<%=pbm.getNum()%>&address=<%=pbm.getAddress()%>&tel=<%=pbm.getTel()%>&fax=<%=pbm.getFax()%>&code=<%=pbm.getCode()%>&bank=<%=pbm.getBank()%>&account=<%=pbm.getAccount()%>"><img alt="update" src="images/cog_edit.png" width="15" height="15">update</a>
			<%
					}
				%>
			<tr>
			    <td align="center"><a href="customerUpdate.jsp?id=-1">Add</a></td>
				<td colspan="3"></td>
			</tr>
		</table>
	</div>

	<div align="left" class="pagelist">
		 Total&nbsp;<strong><%=userList.size()%></strong>&nbsp;Users
	</div>
</body>
</html>
