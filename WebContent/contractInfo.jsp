<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="model.ConDetailBusiModel"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/style.css" rel="stylesheet" media="screen"
	type="text/css" />
<title>List of contract to be finalized</title>
<!-- Use JavaScript script to open a new window display information when preview-->
<script>
			function preview(url) {
				window.open(url,'Preview','resizable=no,toolbar=no,width=620,height=500,top=50,left=200');
			}
		</script>
</head>

<body>
	<div class="mtitle">Contract Inf</div>

	<div>
		<form>
			Search contract<input
				placeholder="Enter the search conditions.." class="textF"/><input
				type="submit" value="Search" class="button" />
		</form>
	</div>

	<div class="list">
		<table>
			<tr>
				<th width="200px">Contract id</th>
				<th width="200px">Contract name</th>
				<th width="200px">Drafted</th>
				<th width="200px">Begin time</th>
				<th width="200px">End time</th>
				<th width="400px">Operation</th>
			</tr>
			<%
				List<ConDetailBusiModel> contractList = (List<ConDetailBusiModel>)request.getAttribute("conds");  
		        for (ConDetailBusiModel cbm : contractList) {
       	 	%>
			<tr>
				<td align="center"><a
					href="javascript:preview('contractDetail?conId=<%=cbm.getId() %>')"><%=cbm.getId()%></a>
				</td>
				<td align="center" href="javascript:preview('contractDetail?conId=<%=cbm.getId() %>')"><%=cbm.getName() %></td>
				<td align="center"><%=cbm.getDraftsman() %></td>
				<td align="center"><%=cbm.getBeginTime() %></td>
				<td align="center"><%=cbm.getEndTime() %></td>
				<td align="center"><a href="toDeleteContract?delConId=<%=cbm.getId()%>"><img alt="delete" src="images/delete.png" width="15" height="15">Del</a>
			</tr>
			<%} %>
		</table>
	</div>
</body>
</html>
