<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="model.Log"%>
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
	<div class="mtitle">log</div>

	<div class="list">
		<table>
			<tr>
				<th width="200px">Index</th>
				<th width="200px">Time</th>
				<th width="600px">Content</th>
			</tr>
			<%
			    List<Log> logList = (List<Log>)request.getAttribute("logList");  
		        for (Log cbm : logList) {
       	 	%>
			<tr>
			    <td align="center"><%=cbm.getId() %></td>
				<td align="center"><%=cbm.getTime()%></td>
				<td align="center"><%=cbm.getContent()%></td>
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
		 Total&nbsp;<strong><%=logList.size()%></strong>&nbsp;log
	</div>
</body>
</html>