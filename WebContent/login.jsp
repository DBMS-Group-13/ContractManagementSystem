<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Contract Management System - Login page</title>
		<link href="css/style.css" rel="stylesheet" media="screen"
			type="text/css" />
		<script type="text/javascript">
			function check(){
				var name = document.getElementById('name');
				var password = document.getElementById('password');
				if(name.value == ""){
					alert("User name can not be empty!");
					name.focus();
					return false;
				}
				if(password.value == ""){
					alert("Password can not be empty!");
					password.focus();
					return false;
				}
			}
		</script>
		<script type="text/javascript">  
 			// Make the page as the parent window display
 			if(top!=self){
 				top.location.href=self.location.href;
 			}  
  		</script>
  	
	</head>

	<body>
		<!-- header start -->
		<jsp:include page="header.jsp"></jsp:include>
		<!-- header end -->

		<!-- main start -->
		<div class="main">
			<form action="login" method="post">

				<div class="register_main">
					<table>
						<tr>
							<td class="title" colspan="3">
								User Login
							</td>
						</tr>
						<tr>
							<td width="60">
								User name:
							</td>
							<!-- Get user name -->
							<%
								// Set the name's default value is "",to avoid the page display null.
								String name = "";
								if (request.getAttribute("userName") != null) {
									name = (String) request.getAttribute("userName");
								}
							%>
							<td width="200">
								<input type="text" name="name" id="name" value="<%=name%>" height="40"/>
							</td>
							<td width="200"></td>
						</tr>

						<tr>
							<td>
								Password：
							</td>
							<td>
								<input type="password" name="password" id="password" value="" />
							</td>
							<td></td>
						</tr>
						<tr>
							<td colspan="3" align="left" style="color:red;">
							<%
								if (request.getAttribute("message") != null) {
							%>
							<%=request.getAttribute("message")%>
							<%
								}
							%>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<input type="submit" value="Login" class="button" onclick="return check()"/>
							</td>
						</tr>
					</table>
				</div>

			</form>
		</div>
		<!-- main end -->

		<!-- footer start -->
		<jsp:include page="footer.jsp"></jsp:include>
		<!-- footer end -->
	</body>
</html>