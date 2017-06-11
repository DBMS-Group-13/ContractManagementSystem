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
	<div class="mtitle">Process</div>

	<div>
		<form>
			Contract process <input
				placeholder="Enter the search conditions.." class="textF"/><input
				type="submit" value="Search" class="button" />
		</form>
	</div>

	<div class="list">
		<table>
			<tr>
				<th>Contract name</th>
				<th class="th1">Draft time</th>
				<th width="400px">Process</th>
			</tr>
			<%
				List<ConBusiModel> contractList = (List<ConBusiModel>)request.getAttribute("contractList");
			    if(contractList != null){
		        for (ConBusiModel cbm : contractList) {
       	 	%>
			<tr>
				<td class="tdname"><a
					href="javascript:preview('contractDetail?conId=<%=cbm.getConId()%>')"><%=cbm.getConName()%></a>
				</td>
				<td><%=cbm.getDrafTime()%></td>
				<td><a>3/5</a></td>
			</tr>
			<%} 
			}%>
		</table>
	</div>
</body>
</html>
