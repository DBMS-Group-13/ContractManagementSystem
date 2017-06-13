<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="model.ConDetailBusiModel"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Contract details</title>
<link href="css/style.css" rel="stylesheet" media="screen"
	type="text/css" />
</head>

<body>
	<div class="preview">
		<%
			// Get contract details business object
			ConDetailBusiModel contract = new ConDetailBusiModel();
			if (request.getAttribute("conDetailBusiModel") != null) {
				contract = (ConDetailBusiModel)request.getAttribute("conDetailBusiModel");
			}
		%>

		<div class="viewbox">
			<div class="title">
				<%=contract.getName()%>
			</div>
			<div class="info">
				<small>Customer:</small><%=contract.getCustomer()%><br/>
				<small>Drafter:</small><%=contract.getDraftsman()%><br/>
				<small>Begin time:</small><%=contract.getBeginTime()%><br/>
				<small>End time:</small><%=contract.getEndTime()%><br/>
			</div>
			<div>
				<small>Content:<br/></small>
				<%=contract.getContent()%>
			</div>
		</div>
	</div>
</body>
</html>
