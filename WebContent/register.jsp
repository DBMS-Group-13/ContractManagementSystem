<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Contract Management System - Registration page</title>
		<link href="css/style.css" rel="stylesheet" media="screen"
			type="text/css" />
		<script type="text/javascript">
			function check(){
				var name = document.getElementById('name');
				var password = document.getElementById('password');
				var password2 = document.getElementById('password2');
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
				if(password2.value != password.value){
					alert("Repeat password and password should keep consistent!");
					return false;
				}			
			}
		</script>	
	</head>

	<body>
		<!-- header start -->
		<jsp:include page="header.jsp"></jsp:include>
		<!-- header end -->

		<!-- main start -->
		<div class="main">
			<form action="register" method="post">

				<div class="register_main">
					<table>
						<tr>
							<td class="title" colspan="2">
								User register
							</td>
						</tr>
						<tr>
							<td width="120" align="center">
								User name:
							</td>
							<td>
								<input type="text" name="name" id="name" value="" />
							</td>
						</tr>
						<tr>
							<td class="info" colspan="2">
								User name must begin with a letter, at least four words(letters, Numbers, underscores).
							</td>
						</tr>

						<tr>
							<td align="center">
								Password:
							</td>
							<td>
								<input type="password" name="password" id="password" value="" />
							</td>
						</tr>
						<tr>
							<td class="info" colspan="2">
								Password can not be too simple, at least contain six words; Recommend to use numbers and letters mixed arrangement, case-insensitive.
							</td>
						</tr>

						<tr>
							<td align="center">
								Repeat Password:
							</td>
							<td>
								<input type="password" name="password2" id="password2" value="" />
							</td>
						</tr>
						<tr>
							<td class="info" colspan="2">
								Repeat password and password should keep consistent!
							</td>
						</tr>
						<tr>
							<td colspan="2" align="left" style="color:red;">
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
							<td colspan="2">
								<input type="submit" value="Submit" class="button" onclick="return check()"/>
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