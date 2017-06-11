<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.ruanko.model.ConBusiModel"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/style.css" rel="stylesheet" media="screen"
	type="text/css" />
<title>List of contract to be countersigned</title>
<!-- Use JavaScript script to open a new window display information when preview-->
<script>
			function preview(url) {
				window.open(url,'Preview','resizable=no,toolbar=no,width=620,height=500,top=50,left=200');
			}
		</script>
</head>

<body>
	<div class="mtitle">Contract to be countersigned</div>

	<div class="search">
		<form>
			Search contract to be countersigned: <input
				value="Enter search conditions.." class="textF"/> &nbsp;&nbsp; <input
				type="submit" value="Search" class="search-submit" /> <br />
		</form>
	</div>

	<div class="list">
		<table>
			<tr>
				<th>Contract name</th>
				<th class="th1">Draft time</th>
				<th class="th2">Operation</th>
			</tr>
			<%
				List<ConBusiModel> contractList = (List<ConBusiModel>)request.getAttribute("contractList");  
		        for (ConBusiModel cbm : contractList) {
       	 	%>
			<tr>
				<td class="tdname"><a
					href="javascript:preview('contractDetail?conId=<%=cbm.getConId()%>')"><%=cbm.getConName()%></a>
				</td>
				<td><%=cbm.getDrafTime()%></td>
				<td><a href="toAddHQOpinion?conId=<%=cbm.getConId()%>"> <img
						src="images/icon-edit.png" alt="Countersign" /> Countersign
				</a></td>
			</tr>
			<%} %>

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
