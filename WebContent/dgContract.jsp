<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="model.Contract"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Finalize contract</title>
<link href="css/style.css" rel="stylesheet" media="screen"
	type="text/css" />
<script type="text/javascript">
			function check(){
				var content = document.getElementById('content');
				if(content.value == ""){
					alert("Content of contract can not be empty!");
					content.focus();
					return false;
				}
			}
		</script>
</head>

<body>
	<div class="mtitle">Finalize contract</div>
	<br />
	<form action="dgContract" method="post">
		<%
				// Get contract object
				Contract contract = new Contract();
				if (request.getAttribute("contract") != null) {
				 	contract = (Contract)request.getAttribute("contract");
				}
			%>
		<input type="hidden" name="conId" value="<%=contract.getId()%>"
			readonly="readonly">
		<table class="update" style="width: 700px;">
			<tr height="28">
				<td width="140">Contract name:</td>
				<td><input type="text" id="name" name="name" class="textF"
					value="<%=contract.getName()%>" readonly="readonly"></td>
			</tr>
			<tr height="28">
				<td>Customer:</td>
				<td><input type="text" name="customer" class="textF"
					value="<%=contract.getCustomer()%>" readonly="readonly"></td>
			</tr>
			<tr>
				<td>Begin time:</td>
				<td><input type="text" id="beginTime" name="beginTime" class="textF"
					value="<%=contract.getBeginTime()%>" readonly="readonly"></td>

			</tr>
			<tr>
				<td>End time:</td>
				<td><input type="text" id="endTime" name="endTime" class="textF"
					value="<%=contract.getEndTime()%>" readonly="readonly"></td>
			</tr>
			<tr>
				<td>Content:</td>
				<td></td>
			</tr>
			<tr>
				<td colspan="2"><textarea id="content" name="content" class="content"
						style="width: 680px; height: 300px; resize: none;"><%=contract.getContent()%></textarea>
				</td>
			</tr>
			<tr height="28">
				<td>Attachment:</td>
				<td><input type="file" /></td>
			</tr>
			<tr height="28">
				<td align="center" colspan="2"><input type="submit"
					value="Submit" class="button" onclick="return check()">
					&nbsp; &nbsp; &nbsp; <input type="reset" value="Reset"
					class="button"></td>
			</tr>
		</table>
	</form>
</body>
</html>
