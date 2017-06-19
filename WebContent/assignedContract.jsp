<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="model.ConDistribute"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>CountersignedContract</title>
<link href="css/style.css" rel="stylesheet" media="screen"
	type="text/css" />
<!-- Use JavaScript script to open a new window display information when preview-->
<script>
			function preview(url) {
				window.open(url,'Preview','toolbar=no,scrollbars=yes,width=720,height=560');
			}
		</script>
</head>

<body>
	<div class="mtitle">Assigned Contract</div>

	<div class="list">
		<table>
			<tr>
				<th width="200px">Contract Id</th>
				<th width="200px">Contract Name</th>
				<th width="200px">Contract Time</th>
				<th width="200px">Contract Csign</th>
				<th width="200px">Contract Approve</th>
				<th width="200px">Contract Sign</th>
			</tr>
			<%
				List<ConDistribute> contractList = (List<ConDistribute>)request.getAttribute("conDistributeList");  
		        for (ConDistribute cbm : contractList) {
       	 	%>
			<tr>
				<td align="center"><%=cbm.getId() %></td>
				<td align="center"><%=cbm.getConName() %></td>
				<td align="center"><%=cbm.getDrafTime() %></td>
				<td align="center"><%=cbm.getCsign() %></td>
				<td align="center"><%=cbm.getApprove() %></td>
				<td align="center"><%=cbm.getSign() %></td>
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
		 Total&nbsp;<strong><%=contractList.size()%></strong>&nbsp;Contracts
	</div>
</body>
</html>