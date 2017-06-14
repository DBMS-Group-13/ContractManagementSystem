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
		<div class="viewbox">
			<div class="title">
			Add Role
			</div>
			<div class="info">
			<form action="toAddRole">
				<small width="100">id:</small><input
				value="-1" readonly="readonly" id="id" name="id" class="textF"/><br/>
				<small width="100">name:</small>
				<select id="name" name="name">
                <option>operator</option>
                <option>admin</option>
                </select>
				<br/>
				<small width="100">description:</small><input
				id="description" name="description" class="textF"/><br/>
				<input type="checkbox" value="001" name="funcIds"/>System management
				<input type="checkbox" value="002" name="funcIds"/>Contract management
				<input type="checkbox" value="003" name="funcIds"/>Draft<br/>
				<input type="checkbox" value="004" name="funcIds"/>Countersign
				<input type="checkbox" value="005" name="funcIds"/>Approve
				<input type="checkbox" value="006" name="funcIds"/>Sign
				<input
				type="submit" value="submit" class="button" />
				</form>
			</div>
		</div>
	</div>
</body>
</html>
