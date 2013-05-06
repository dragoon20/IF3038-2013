/*----- Bagian Login ----*/
var login_area = document.getElementById("login_area");
login_area.onmouseover = function()
{
	document.getElementById("login_form_wrap").className = "appear";
	document.getElementById("login_button").className = "login_button_appear";
	document.getElementById("login_link").className = "login_link_appear";
	document.getElementById("border_login").className = "appear";
};

login_area.onmouseout = function()
{
	document.getElementById("login_form_wrap").className = "";
	document.getElementById("login_button").className = "";
	document.getElementById("login_link").className = "";
	document.getElementById("border_login").className = "";
};

var form_login = document.getElementById("login_form");
form_login.onsubmit = function()
{
	var data = serialize(form_login);
	var request = new ajaxRequest();
	request.onreadystatechange = function()
	{
		if (request.readyState==4)
		{
			if (request.status==200 || window.location.href.indexOf("http")==-1)
			{
				var result = eval("("+request.responseText+")");
				if (result.status == "success")
				{
					window.location = "dashboard.php";
				}
				else
				{
					alert("Username dan password yang dimasukkan salah.");
				}
			}
			else
			{
				alert("Mohon maaf, sedang ada kesalahan pada server.");
			}
		}
	};
	request.open("POST", "ws/login.php", true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	request.send(data);
	
	document.getElementById("login_username").value = "";
	document.getElementById("login_password").value = "";
	return false;
};