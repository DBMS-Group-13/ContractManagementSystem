<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="model.ConBusiModel"%>
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
	<div class="mtitle">Finallized Contract</div>

	<div class="search">
		<form>
			Search contract finallized <input type="text" class="textF"
				value="Please enter contract name..." name="name"
				onFocus="this.value=''" class=""/> &nbsp;&nbsp; <input type="submit"
				value="Search" class="button" /> <br />
		</form>
	</div>

	<div class="list">
		<table>
			<tr>
				<th width="200px">Contract name</th>
				<th width="200px">Draft time</th>
				<th width="200px">Status</th>
			</tr>
			<%
				List<ConBusiModel> contractList = (List<ConBusiModel>)request.getAttribute("contractList");  
		        for (ConBusiModel cbm : contractList) {
       	 	%>
			<tr>
				<td align="center"><a href="javascript:void(0)"><%=cbm.getConName()%></a>
				</td>
				<td align="center"><%=cbm.getDrafTime()%></td>
				<td align="center">Finalized</td>
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