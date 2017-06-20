<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="model.PermissionDetailModel" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN">
<html lang="en">

<head>
	<title>Contract Management System - Operator page</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
	<!-- VENDOR CSS -->
	<link rel="stylesheet" href="assets/vendor/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="assets/vendor/font-awesome/css/font-awesome.min.css">
	<link rel="stylesheet" href="assets/vendor/linearicons/style.css">
	<link rel="stylesheet" href="assets/vendor/chartist/css/chartist-custom.css">
	<!-- MAIN CSS -->
	<link rel="stylesheet" href="assets/css/main.css">
	<!-- GOOGLE FONTS -->
	<link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700" rel="stylesheet">
</head>
<body>
	<!-- WRAPPER -->
	<div id="wrapper">
		<!-- LEFT SIDEBAR -->
		<div id="sidebar-nav" class="sidebar">
			<div class="sidebar-scroll">
				<nav>
					<ul class="nav">
					<%PermissionDetailModel pm = (PermissionDetailModel)session.getAttribute("pdm"); 
					if(pm.getDraft() == true){%>
						<li>
							<a href="#subPages" data-toggle="collapse" class="collapsed"><i class="lnr lnr-file-empty"></i> <span>Draft</span> <i class="icon-submenu lnr lnr-chevron-left"></i></a>
							<div id="subPages" class="collapse ">
								<ul class="nav">
									<li><a href="toDraft" target="main" class="">Draft Contract</a></li>
									<li><a href="toDdghtList" target="main" class="">Contract to be finalized</a></li>
									<li><a href="toDrafted" target="main" class="">Finalized Contract</a></li>
									<li><a href="toQueryProcess" target="main" class="">Query Process</a></li>
								</ul>
							</div>
						</li>
						<%}if(pm.getCsign() == true){%>
						<li>
							<a href="#subPages2" data-toggle="collapse" class="collapsed"><i class="lnr lnr-file-empty"></i> <span>Countersign</span> <i class="icon-submenu lnr lnr-chevron-left"></i></a>
							<div id="subPages2" class="collapse ">
								<ul class="nav">
									<li><a href="toDhqhtList" target="main">Contract to be countersigned</a></li>
									<li><a href="toCountersigned" target="main" class="">Countersigned Contract</a></li>
								</ul>
							</div>
						</li>
						<%}if(pm.getApprove()){ %>
						<li>
							<a href="#subPages3" data-toggle="collapse" class="collapsed"><i class="lnr lnr-file-empty"></i> <span>Approve</span> <i class="icon-submenu lnr lnr-chevron-left"></i></a>
							<div id="subPages3" class="collapse ">
								<ul class="nav">
									<li><a href="toDshphtList" target="main">Contract to be approved</a></li>
									<li><a href="toApproved" target="main" class="">Approved Contract</a></li>
								</ul>
							</div>
						</li>
						<%}if(pm.getSign() == true){ %>
						<li>
							<a href="#subPages4" data-toggle="collapse" class="collapsed"><i class="lnr lnr-file-empty"></i> <span>Sign</span> <i class="icon-submenu lnr lnr-chevron-left"></i></a>
							<div id="subPages4" class="collapse ">
								<ul class="nav">
									<li><a href="toDqdhtList" target="main">Contract to be signed</a></li>
									<li><a href="toSigned" target="main" class="">Signed Contract</a></li>
								</ul>
							</div>
						</li>
						<%} %>
						<li><a href="toLogin" class=""><i class="lnr lnr-home"></i> <span>log out</span></a></li>
					</ul>
				</nav>
			</div>
		</div>
		<!-- END LEFT SIDEBAR -->
		<!-- MAIN -->
		<iframe src="result.jsp" name="main" scrolling="auto" style="position:absolute;left:260;" height="100%" width="100%"></iframe>
		<!-- END MAIN -->
		<div class="clearfix"></div>
		<footer>
			<!-- footer -->
		</footer>
	</div>
	<!-- END WRAPPER -->
	<!-- Javascript -->
	<script src="assets/vendor/jquery/jquery.min.js"></script>
	<script src="assets/vendor/bootstrap/js/bootstrap.min.js"></script>
	<script src="assets/vendor/jquery-slimscroll/jquery.slimscroll.min.js"></script>
	<script src="assets/vendor/jquery.easy-pie-chart/jquery.easypiechart.min.js"></script>
	<script src="assets/vendor/chartist/js/chartist.min.js"></script>
	<script src="assets/scripts/klorofil-common.js"></script>
</body>
</html>
