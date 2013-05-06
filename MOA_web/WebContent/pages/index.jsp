<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="controllers.MainApp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	if (MainApp.LoggedIn(session))
	{
		response.sendRedirect("dashboard");
	}
	else
	{
%>
		<!doctype html>
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
				<meta name="Description" content="" />
				<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" />
				<title>MOA</title>
				<link rel="stylesheet" href="css/style.css" />
				<link rel="stylesheet" href="css/index.css" />
			</head>
			<body>
				<%
					Map<String, Map<String, String>> menu_main = new HashMap<String, Map<String, String>>();
					Map<String, String> daftar = new HashMap<String, String>();
					daftar.put("id", "register_button");
					daftar.put("href", "javascript:register()");
					menu_main.put("Daftar", daftar);
					request.setAttribute("menu", menu_main);
				%>
				<%@ include file="../template/header.jsp" %>
				<section>
					<div id="content_wrap" class="wrap">
						<div id="slide_left">
						</div>
						<div id="content_wrap_inner">
							<img class="slide_item" src="images/tes.jpg" alt="gambar 1" />
							<img class="slide_item" src="images/tes2.jpg" alt="gambar 2" />
							<img class="slide_item" src="images/tes3.jpg" alt="gambar 3" />
							<img class="slide_item" src="images/tes4.jpg" alt="gambar 4" />
						</div>
						<div id="slide_right">
						</div>
						<div class="clear"></div>
						<div id="slide_bullet">
							<div class="bullet_slide active" onclick="go_to_image(1);"> </div>
							<div class="bullet_slide" onclick="go_to_image(2);"> </div>
							<div class="bullet_slide" onclick="go_to_image(3);"> </div>
							<div class="bullet_slide" onclick="go_to_image(4);"> </div>
						</div>
					</div>
				</section>
				<%
					Map<String, Map<String, String>> breadcrumbs_main = new HashMap<String, Map<String, String>>();
					Map<String, String> beranda = new HashMap<String, String>();
					beranda.put("href", "index");
					daftar.put("class", "active");
					menu_main.put("Beranda", beranda);
					request.setAttribute("breadcrumbs", breadcrumbs_main);
				%>
				<%@ include file="../template/footer.jsp" %>
				
				<div id="black_trans">
					<div id="frame_register">
						<div id="close_button">
							<a href="javascript:close();">X</a>
						</div>
						<div id="register_area">
							<div id="register_form_wrap">
								<div id="register_head">
									<h2>Form Pendaftaran</h2>
								</div>
								<form id="register_form" method="post">
									<div class="row">
										<label for="register_username">Username</label>
										<input id="register_username" name="username" pattern="^.{5,}$" type="text" title="Username minimal harus terdiri dari 5 karakter dan tidak sama dengan sandi." required />
									</div>
									<div class="row">
										<label for="register_password">Sandi</label>
										<input id="register_password" name="password" pattern="^.{8,}$" type="password" title="Sandi minimal harus terdiri dari 8 karakter dan tidak sama dengan username dan email." required />							
									</div>
									<div class="row">
										<label for="register_confirm_password">Konfirmasi Sandi</label>
										<input id="register_confirm_password" name="confirm_password" pattern="^.{8,}$" type="password" title="Konfirmasi sandi harus sama dengan sandi." required />
									</div>
									<div class="row">
										<label for="register_name">Nama Lengkap</label>
										<input id="register_name" name="name" pattern="^.+ .+$" type="text" title="Nama lengkap harus terdiri dari 2 kata dipisah oleh sebuah spasi." required />
									</div>
									<div class="row">
										<label for="birth_date">Tanggal Lahir</label>
										<input id="birth_date" name="birth_date" type="text" pattern="^[1-9][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]$" onclick="datePicker.showCalendar(event);" title="Tahun harus minimal dari tahun 1955." autocomplete=off required/>
									</div>
									<div class="row">
										<label for="register_email">Email</label>
										<input id="register_email" name="email" pattern="^.+@.+\...+$" type="text" title="Email yang dimasukkan harus benar dan tidak sama dengan sandi." required />
									</div>
									<div class="row">
										<label for="register_avatar">Avatar</label>
										<input id="register_avatar" name="avatar" type="file" title="Avatar yang diupload harus berekstensi jpg atau jpeg." required />
									</div>
									<input id="register_submit" type="submit" value="Daftar" disabled="disabled" title="Semua elemen form harus diisi dengan benar dahulu."/>
								</form>
							</div>
						</div>
					</div>
				</div>
				
				<%@ include file="../template/calendar.jsp" %>
				
				<script type="text/javascript" src="js/request.js"></script>
				<script type="text/javascript" src="js/register.js"></script>
				<script type="text/javascript">
					/*----- Bagian SlideShow ----*/
					var content_wrap = document.getElementById("content_wrap");
					content_wrap.onmouseover = function()
					{
						document.getElementById("slide_left").style.opacity = "1";
						document.getElementById("slide_right").style.opacity = "1";
					}
					
					content_wrap.onmouseout = function()
					{
						document.getElementById("slide_left").style.opacity = "0";
						document.getElementById("slide_right").style.opacity = "0";
					}
					
					var current_image = 1;				
					var max_image = 4;
					var width = 960;
					function go_to_bullet(index)
					{
						var bullets = document.getElementById("slide_bullet").childNodes;
						var i = 1;
						for (var j=0;j<bullets.length;++j)
						{
							if (bullets[j].nodeName!="#text")
							{
								if (i==index)
									bullets[j].className = "bullet_slide active";
								else
									bullets[j].className = "bullet_slide";
								i++;
							}
						}
					}
					function next_image()
					{
						current_image++;
						if (current_image > max_image)
							current_image = 1;
						document.getElementById("content_wrap_inner").style.left = (-1*width*(current_image-1))+"px";
						go_to_bullet(current_image);
					}
					function prev_image()
					{
						current_image--;
						if (current_image < 1)
							current_image = max_image;
						document.getElementById("content_wrap_inner").style.left = (-1*width*(current_image-1))+"px";
						go_to_bullet(current_image);
					}
					document.onkeydown = function(event)
					{
						if (event.keyCode=="37")
							prev_image();
						else if (event.keyCode=="39")
							next_image();
		
					}
					function go_to_image(index)
					{
						current_image = index;
						document.getElementById("content_wrap_inner").style.left = (-1*width*(current_image-1))+"px";
						go_to_bullet(current_image);
					}
					document.getElementById("slide_left").onclick = function() {prev_image()};
					document.getElementById("slide_right").onclick = function() {next_image()};
				</script>
				<script type="text/javascript">	
					window.onload = function() 
					{
						datePicker.init(document.getElementById("calendar"), document.getElementById("register_form"));
						setInterval(function(){next_image();}, 5500);
					}
				</script>
			</body>
		</html>
<%
	}
%>