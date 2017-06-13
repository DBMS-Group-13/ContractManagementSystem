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
	<div class="mtitle">Countersigned Contract</div>

	<div class="search">
		<form>
			Search contract to countersigned <input type="text"
				value="Please enter contract name..." name="name"
				onFocus="this.value=''" class=""/> &nbsp;&nbsp; <input type="submit"
				value="Search" class="search-submit" /> <br />
		</form>
	</div>

	<div class="list">
		<table>
			<tr>
				<th>Contract name</th>
				<th class="th1">Draft time</th>
				<th class="th1">Status</th>
			</tr>
			<%
				List<ConBusiModel> contractList = (List<ConBusiModel>)request.getAttribute("contractList");  
		        for (ConBusiModel cbm : contractList) {
       	 	%>
			<tr>
				<td class="tdname"><a href="javascript:void(0)"><%=cbm.getConName()%></a>
				</td>
				<td><%=cbm.getDrafTime()%></td>
				<td><%=cbm.getDONENum()%>/
				<%=cbm.getDistributeENum()%></td>
			</tr>
			<%
				}
			%>
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