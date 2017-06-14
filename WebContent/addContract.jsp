<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@page import="model.Contract"%>
<%@page import="model.Customer"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Draft contract</title>
<link href="css/style.css" rel="stylesheet" media="screen" type="text/css" />
<script type="text/javascript">
			function check(){
				var name = document.getElementById('name');
				var beginTime = document.getElementById('beginTime');
				var endTime = document.getElementById('endTime');
				var content = document.getElementById('content');
				if(name.value == ""){
					alert("Contract name can not be empty!");
					name.focus();
					return false;
				}
				if(beginTime.value == ""){
					alert("Begin time can not be empty!");
					beginTime.focus();
					return false;
				}
				if(endTime.value == ""){
					alert("End time can not be empty!");
					endTime.focus();
					return false;
				}
				if(content.value == ""){
					alert("Contract content can not be empty!");
					content.focus();
					return false;
				}
			}
		</script>
</head>

<body>
	<div class="mtitle">Draft Contract</div>
	<br />
	<div
		style="font-size: 18px; color: green; width: 700px; text-align: center;">
		<%
				if (request.getAttribute("message") != null) {
			%>
		<%=request.getAttribute("message")%>
		<%
				}
			%>
	</div>

	<form action="draft" method="post">
		<table class="update" style="width: 700px;">
			<%
					// Get contract object
					Contract contract = new Contract();
					if (request.getAttribute("contract") != null) {
				 		contract = (Contract)request.getAttribute("contract");
				 	}
				%>
			<tr height="28">
				<td width="140px">Contract name:</td>
				<td><input type="text" id="name" name="name" class="textF"
					value="<%=contract.getName()%>" /><font color="red">&nbsp;&nbsp;*</font>
				</td>
			</tr>

			<tr height="28">
				<td>Customer:</td>
				<!-- <td><input type="text" name="customer" class="textF" value="" /></td> -->
				<td>
				<select id="customer" name="customer">
                <% List<Customer> contractList = (List<Customer>)request.getAttribute("customers");  
		        for (Customer cbm : contractList) { %>
		        <option><%=cbm.getName()%></option>
		        <%} %>
            </select>
			</td>
			</tr>
			<tr>
				<td>Begin time:</td>
				<td><input type="text" id="beginTime" class="textF" name="beginTime" placeholder=" yyyy-mm-dd"/>
			</tr>
			<tr>
				<td>End time:</td>
				<td><input type="text" id="endTime" class="textF" name="endTime" placeholder=" yyyy-mm-dd"/>
			</tr>
			<tr>
				<td>Penalty:</td>
				<td><input type="text" id="penalty" class="textF" name="penalty" placeholder="Positive number"/>
			<tr>
				<td>Content:</td>
				<td><font color="red">&nbsp;&nbsp;*</font></td>
			</tr>

			<tr>
				<td colspan="2"><textarea id="content" name="content" class="content"
						 ><%=contract.getContent()%></textarea>
				</td>
			</tr>
			<tr height="28">
				<td>Attachment:</td>
				<td><input type="file"/></td>
			</tr>
			<tr height="28">
				<td><input type="submit" value="Submit" class="button" onclick="return check()"></td><td><input type="reset" value="Reset" class="button"></td>
			</tr>
		</table>
	</form>
</body>
</html>
