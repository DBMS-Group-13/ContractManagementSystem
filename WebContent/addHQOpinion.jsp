<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="model.Contract"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Countersign contract</title>
<link href="css/style.css" rel="stylesheet" media="screen"
	type="text/css" />
<script type="text/javascript">
			function check(){
				var content = document.getElementById('content');
				if(content.value == ""){
					alert("Countersign opinion can not be empty!");
					content.focus();
					return false;
				}
			}
		</script>
</head>

<body>
	<div class="mtitle">Countersign contract</div>
	<br />
	<form name="addHQOpinionForm" action="addHQOpinion" method="post">
		<%
				Contract contract = (Contract)request.getAttribute("contract");
			%>
		<input type="hidden" name="conId" value="<%=contract.getId()%>">
		<table class="update" style="width: 680px;">
			<tr height="28">
				<td width="180px">Contract name:</td>
				<td><%=contract.getName()%></td>
			</tr>
			<tr>
				<td>Countersign opinion:</td>
				<td><textarea rows="10" cols="40" id="content" name="content"
						style="width: 400px; height: 100px; resize: none;"></textarea></td>
			</tr>
			<tr height="28">
				<td align="center" colspan="2"><input type="submit"
					value="Submit" class="button" onclick="return check()">
					&nbsp; &nbsp; &nbsp; <input type="reset" value="Cancel"
					class="button"></td>
			</tr>
		</table>
	</form>
</body>
</html>
