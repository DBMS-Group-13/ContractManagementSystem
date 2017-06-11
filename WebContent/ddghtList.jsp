<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.ruanko.model.ConBusiModel"%>
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
	<div class="mtitle">Contract to de finalized</div>

	<div>
		<form>
			Search contract to be finalized <input
				value="Enter the search conditions.." class="textF"/><input
				type="submit" value="Search" class="button" />
		</form>
	</div>

	<div class="list">
		<table>
			<tr>
				<th>Contract name</th>
				<th class="th1">Draft time</th>
				<th width="270px">Operation</th>
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
				<td><a href="showHQOpinion?conId=<%=cbm.getConId()%>"> <img
						src="images/information.png" alt="Countersign opinion" />
						Countersign opinion
				</a> &nbsp;|&nbsp; <a href="toDgContract?conId=<%=cbm.getConId()%>">
						<img src="images/icon-edit.png" alt="Finalize" /> Finalize
				</a></td>
			</tr>
			<%} %>

			<tr>
				<td colspan="3"></td>
			</tr>
		</table>
	</div>
</body>
</html>
