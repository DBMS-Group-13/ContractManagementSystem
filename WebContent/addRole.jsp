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

<script type="text/javascript">
    	function change(ele) {
    		var value = ele.value;
    		if(value == "admin"){
    			document.getElementById("001").disabled=false;
    			document.getElementById("002").disabled=false;
    			document.getElementById("003").disabled=true;
    			document.getElementById("004").disabled=true;
    			document.getElementById("005").disabled=true;
    			document.getElementById("006").disabled=true;
    			document.getElementById("001").checked=false;
    			document.getElementById("002").checked=false;
    			document.getElementById("003").checked=false;
    			document.getElementById("004").checked=false;
    			document.getElementById("005").checked=false;
    			document.getElementById("006").checked=false;
    		}
    		if(value == "operator"){
    			document.getElementById("001").disabled=true;
    			document.getElementById("002").disabled=true;
    			document.getElementById("003").disabled=false;
    			document.getElementById("004").disabled=false;
    			document.getElementById("005").disabled=false;
    			document.getElementById("006").disabled=false;
    			document.getElementById("001").checked=false;
    			document.getElementById("002").checked=false;
    			document.getElementById("003").checked=false;
    			document.getElementById("004").checked=false;
    			document.getElementById("005").checked=false;
    			document.getElementById("006").checked=false;    			
    		}
    	}
</script>
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
				<select id="name" name="name" onchange="change(this)">
                <option>operator</option>
                <option>admin</option>
                </select>
				<br/>
				<small width="100">description:</small><input
				id="description" name="description" class="textF"/><br/>
				<input type="checkbox" value="001" name="funcIds" id = "001" disabled = "disabled"/>System management
				<input type="checkbox" value="002" name="funcIds" id = "002" disabled = "disabled"/>Contract management
				<input type="checkbox" value="003" name="funcIds" id = "003"/>Draft<br/>
				<input type="checkbox" value="004" name="funcIds" id = "004"/>Countersign
				<input type="checkbox" value="005" name="funcIds" id = "005"/>Approve
				<input type="checkbox" value="006" name="funcIds" id = "006"/>Sign
				<input
				type="submit" value="submit" class="button" />
				</form>
			</div>
		</div>
	</div>
</body>
</html>
