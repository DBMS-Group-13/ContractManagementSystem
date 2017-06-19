<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="model.ConBusiModel"%>
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
<%
List<ConBusiModel> contractList = (List<ConBusiModel>)request.getAttribute("contractList"); 
// Get session by using request
session.setAttribute("contractList", contractList);
%>
	<div class="mtitle">Contract to be finalized</div>

	<div>
		<form action = "searchConBusi?jspname=<%="/ddghtList.jsp"%>" method = "post">
			Search contract to be finalized <input
				placeholder="Enter the search conditions.." class="textF" name = "searchname"/><input
				type="submit" value="Search" class="button" />
		</form>
	</div>

	<div class="list">
		<table>
			<tr>
				<th width="200px">Contract name</th>
				<th width="200px">Draft time</th>
				<th width="400px">Operation</th>
			</tr>
			<%				 
		        for (ConBusiModel cbm : contractList) {
       	 	%>
			<tr>
				<td align="center"><a
					href="javascript:preview('contractDetail?conId=<%=cbm.getConId()%>')"><%=cbm.getConName()%></a>
				</td>
				<td align="center"><%=cbm.getDrafTime()%></td>
				<td align="center"><a href="showHQOpinion?conId=<%=cbm.getConId()%>"> <img
						src="images/information.png" alt="Countersign opinion" />
						Countersign opinion
				</a> &nbsp;|&nbsp; <a href="toDgContract?conId=<%=cbm.getConId()%>">
						<img src="images/icon-edit.png" alt="Finalize" /> Finalize
				</a></td>
			</tr>
			<%} %>
		</table>
	</div>
</body>
</html>
