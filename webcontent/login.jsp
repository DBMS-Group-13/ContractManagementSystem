<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html >
<head>
<title>ContractSys</title>
<link rel="stylesheet" href="css/login.css">
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
			function check2(){
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
<div class="cotn_principal">
  <div class="cont_centrar">
    <div class="cont_login">
      <div class="cont_info_log_sign_up">
        <div class="col_md_login">
          <div class="cont_ba_opcitiy">
            <h2>LOGIN</h2>
            <p>tape here if you have an account</p>
            <button class="btn_login" onClick="cambiar_login()">LOGIN</button>
          </div>
        </div>
        <div class="col_md_sign_up">
          <div class="cont_ba_opcitiy">
            <h2>SIGN UP</h2>
            <p>Or not</p>
            <button class="btn_sign_up" onClick="cambiar_sign_up()">SIGN UP</button>
          </div>
        </div>
      </div>
      <div class="cont_back_info">
        <div class="cont_img_back_grey"> <img src="./images/po.jpg" alt="" /> </div>
      </div>
      <div class="cont_forms" >
        <div class="cont_img_back_"> <img src="./images/po.jpg" alt="" /> </div>
        <div class="cont_form_login"> <a href="#" onClick="ocultar_login_sign_up()" ><i class="material-icons">←</i></a>
          <h2>LOGIN</h2>
          <form action="login" method="post">
          <%
			String name = "";
			if (request.getAttribute("userName") != null) {
			name = (String) request.getAttribute("userName");
			}
		  %>
          <input type="text" name="name" id="name" placeholder="ID" />
          <input type="password" name="password" id="password" placeholder="Password" />
          <%
								if (request.getAttribute("message") != null) {
							%> <%=request.getAttribute("message")%> <%
								}
							%>
          <button class="btn_login" onClick="return check()">LOGIN</button>
          </form>
        </div>
        <div class="cont_form_sign_up"> <a href="#" onClick="ocultar_login_sign_up()"><i class="material-icons">←</i></a>
          <h2>SIGN UP</h2>
          <form action="register" method="post">
          <table>
          <tr><td>
          <input type="text" name="name" id="name" placeholder="User" /></td></tr>
          <tr><td>
          <input type="password" name="password" id="password" placeholder="Password" /></td></tr>
          <tr><td>
          <input type="password" name="password2" id="password2" placeholder="Confirm Password" /></td></tr>
          </table>
          <button class="btn_sign_up" onClick="return check2()">SIGN UP</button>  
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="assets/scripts/login.js"></script>

</body>
</html>
