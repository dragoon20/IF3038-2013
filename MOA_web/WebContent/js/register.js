/*----- Bagian Munculin Register ----*/
function register()
{
	document.getElementById("black_trans").className = "appear";
	document.getElementById("register_area").className = "appear";
	document.getElementById("close_button").className = "appear";
	setTimeout(function()
	{
		document.getElementById("frame_register").className = "frame_register_enter";
	}, 100);
}

function close()
{
	document.getElementById("frame_register").className = "";
	setTimeout(function()
	{
		document.getElementById("black_trans").className = "";
		document.getElementById("register_area").className = "";
		document.getElementById("close_button").className = "";
	}, 400);
}

/*----- Bagian Register ----*/
var register_form = document.getElementById("register_form");

var register_username = document.getElementById("register_username");
var register_password = document.getElementById("register_password");
var register_confirm_password = document.getElementById("register_confirm_password");
var register_name = document.getElementById("register_name");
var birth_date = document.getElementById("birth_date");
var register_email = document.getElementById("register_email");
var register_avatar = document.getElementById("register_avatar");

register_form.onsubmit = function()
{
	if ((register_password.value==register_confirm_password.value) && (register_username.value!=register_password.value) &&
		(register_email.value!=register_password.value))
	{
		var user = new Object();
		user.userName = register_username.value;
		user.passWord = register_password.value;
		user.fullName = register_name.value;
		user.birthDate = birth_date.value;
		user.emailAddress = register_email.value;
		
		var tempavatar = "images/"+register_avatar.value.split("\\")[register_avatar.value.split("\\").length-1];
		user.Avatar = tempavatar;
		
		var userlist = JSON.parse(localStorage.MOA_userList);
		userlist[userlist.length] = user;
		localStorage.MOA_userList = JSON.stringify(userlist);
		
		close();
		register_form.reset();
		
		register_username.backgroundImage = "";
		register_password.backgroundImage = "";
		register_confirm_password.backgroundImage = "";
		register_name.backgroundImage = "";
		birth_date.backgroundImage = "";
		register_email.backgroundImage = "";
		register_avatar.backgroundImage = "";
		alert("Registrasi berhasil, silahkan login");
	}
	else if (register_password.value!=register_confirm_password.value)
	{
		alert("Konfirmasi sandi harus sama dengan sandi");
	}
	else if (register_email.value==register_password.value)
	{
		alert("Alamat email tidak boleh sama dengan sandi");
	}
	else if (register_username.value==register_password.value)
	{
		alert("Username tidak boleh sama dengan sandi");
	}
	return false;
};

register_username.onkeyup = function()
{
	if (this.checkValidity() && (register_username.value != register_password.value))
		this.style.backgroundImage = "url('images/valid.png')";
	else
		this.style.backgroundImage = "url('images/warning.png')";
	check_submit();
};

register_password.onkeyup = function()
{
	if (this.checkValidity() && (register_username.value != register_password.value) && (register_email.value != register_password.value))
	{
		this.style.backgroundImage = "url('images/valid.png')";
		document.getElementById("register_confirm_password").pattern = this.value;
	}
	else
		this.style.backgroundImage = "url('images/warning.png')";
	check_submit();
};

register_confirm_password.onkeyup = function()
{
	if (this.checkValidity() && register_password.checkValidity())
		this.style.backgroundImage = "url('images/valid.png')";
	else
	
		this.style.backgroundImage = "url('images/warning.png')";
	check_submit();
};

register_name.onkeyup = function()
{
	if (this.checkValidity())
		this.style.backgroundImage = "url('images/valid.png')";
	else
		this.style.backgroundImage = "url('images/warning.png')";
	check_submit();
};

birth_date.onkeyup = function()
{
	if ((this.checkValidity()) && (check_date(this.value)))
		this.style.backgroundImage = "url('images/valid.png')";
	else
		this.style.backgroundImage = "url('images/warning.png')";
	check_submit();
};

register_email.onkeyup = function()
{
	if (this.checkValidity() && (register_email.value != register_password.value))
		this.style.backgroundImage = "url('images/valid.png')";
	else
		this.style.backgroundImage = "url('images/warning.png')";
	check_submit();
};

register_avatar.onchange = function()
{
	if (check_file_jpeg(this))
		this.style.backgroundImage = "url('images/valid.png')";
	else
		this.style.backgroundImage = "url('images/warning.png')";
	check_submit();
};

function check_date(date)
{
	var temp = date.split("-");
	var d = new Date(parseInt(temp[0]), parseInt(temp[1]) - 1, parseInt(temp[2]));
	if ((d) && ((d.getMonth() + 1) == parseInt(temp[1])) && (d.getDate() == Number(parseInt(temp[2]))) && (parseInt(temp[0]) >= 1955))
	{
		datePicker.populateTable(d.getMonth(),d.getFullYear());
		return true;
	}
	else
		return false;
}

function check_file_jpeg(image)
{
	return (image.value.match("^.+\.(jpe?g|JPE?G)$"));
}

function check_submit()
{
	if ((register_username.checkValidity()) && (register_password.checkValidity()) && 
		(register_confirm_password.checkValidity()) && (register_name.checkValidity()) && 
		(birth_date.checkValidity()) && (register_email.checkValidity()) && 
		(check_file_jpeg(register_avatar)) && (register_email.value != register_password.value) &&
		(register_email.value != register_password.value) && (check_date(birth_date.value)))
	{
		document.getElementById("register_submit").disabled="";
	}
	else
	{
		document.getElementById("register_submit").disabled="disabled";
	}
}