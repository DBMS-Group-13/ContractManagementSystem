<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="model.ConBusiModel"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/style.css" rel="stylesheet" media="screen"
	type="text/css" />
<title>List of contract to be approved</title>
<!-- Use JavaScript script to open a new window display information when preview-->
<script>
			function preview(url) {
				window.open(url,'Preview','toolbar=no,scrollbars=yes,width=720,height=560,top=50,left=100');
			}
		</script>
</head>

<body>
<%
List<ConBusiModel> contractList = (List<ConBusiModel>)request.getAttribute("contractList"); 
// Get session by using request
session.setAttribute("contractList", contractList);
%>
	<div class="mtitle">Contract to be approved</div>

	<div class="search">
		<form action = "searchConBusi?jspname=<%="/dshphtList.jsp"%>" method = "post">
			Search contract to be approved: <input
				value="Enter the search conditions.." class="textF" name = "searchname"/> &nbsp;&nbsp; <input
				type="submit" value="Search" class="search-submit" /> <br />
		</form>
	</div>

	<div class="list">
		<table>
			<tr>
				<th width="200px">Contract name</th>
				<th width="200px">Draft time</th>
				<th width="200px">Operation</th>
			</tr>
			<%  
		        for (ConBusiModel cbm : contractList) {
       	 	%>
			<tr>
				<td align="center"><a
					href="javascript:preview('contractDetail?conId=<%=cbm.getConId()%>')"><%=cbm.getConName()%></a>
				</td>
				<td align="center"><%=cbm.getDrafTime()%></td>
				<td align="center"><a href="toAddSHPOpinion?conId=<%=cbm.getConId()%>"> <img
						src="images/icon-edit.png" alt="Approve" /> Approve
				</a></td>
			</tr>
			<%} %>

			<tr>
				<td colspan="3"></td>
			</tr>
		</table>
	</div>

	<div align="left" class="pagelist">
		 Total&nbsp;<strong><%=contractList.size()%></strong>&nbsp;Contracts
	</div>
</body>
</html>
