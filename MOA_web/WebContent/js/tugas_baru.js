/*----- Bagian tugas baru ----*/
var new_task_form = document.getElementById("new_task_form");

var new_task_name = document.getElementById("new_task_name");
var new_task_attachment = document.getElementById("new_task_attachment");
var birth_date = document.getElementById("birth_date");
var new_task_username = document.getElementById("new_task_username");
var new_task_tag = document.getElementById("new_task_tag");

new_task_name.onkeyup = function()
{
	if (this.checkValidity())
		this.style.backgroundImage = "url('images/valid.png')";
	else
		this.style.backgroundImage = "url('images/warning.png')";
	check_submit();
};

new_task_username.setAttribute('autocomplete', 'off');
new_task_username.onkeyup = function()
{
	var value = new_task_username.value;
	var req = ajax();
	req.onreadystatechange = function() {
		switch (req.readyState) {
			case 1:
			case 2:
			case 3:
				break;
			case 4:
				try {
					response = JSON.parse(req.responseText);
					var elm = document.getElementById("auto_comp_assignee");
					var inflate = document.getElementById("auto_comp_inflate_assignee");
					inflate.innerHTML = "";
					var temp = 0;
					for (var i in response)
					{
						temp++;
						var newLi = document.createElement("li");
						newLi.innerHTML = "<a href='javascript:choose_assignee(\""+response[i]+"\")'>"+
											response[i]+"</a>";
						inflate.insertBefore(newLi, inflate.firstChild);
						check_submit();
					}
					
					if (temp!=0)
						elm.style.display = "block";
				}
				catch (e) {

				}
				break;
		}
	};
	req.get('api/get_username?username=' + value);
};

function choose_assignee(username)
{
	var elm = document.getElementById("auto_comp_assignee");
	var value = new_task_username.value;
	value = value.substr(0, value.lastIndexOf(",")+1);
	value += username;
	new_task_username.value = value;
	elm.style.display = "none";
	check_submit();
}

birth_date.onkeyup = function()
{
	if ((this.checkValidity()) && (check_date(this.value)))
		this.style.backgroundImage = "url('images/valid.png')";
	else
		this.style.backgroundImage = "url('images/warning.png')";
	check_submit();
};

new_task_tag.onkeyup = function()
{
	var value = new_task_tag.value;
	var req = ajax();
	req.onreadystatechange = function() 
	{
		switch (req.readyState) {
			case 1:
			case 2:
			case 3:
				break;
			case 4:
				try {
					response = JSON.parse(req.responseText);
					var elm = document.getElementById("auto_comp_tag");
					var inflate = document.getElementById("auto_comp_inflate_tag");
					inflate.innerHTML = "";
					var temp = 0;
					for (var i in response)
					{
						temp ++;
						var newLi = document.createElement("li");
						newLi.innerHTML = "<a href='javascript:choose_tag(\""+response[i]+"\")'>"+
											response[i]+"</a>";
						inflate.insertBefore(newLi, inflate.firstChild);
					}
					
					if (temp!=0)
						elm.style.display = "block";
					check_submit();
				}
				catch (e) {

				}
				break;
		}
	};
	req.get('api/get_tag?tag=' + value);
};

function choose_tag(temptag)
{
	var elm = document.getElementById("auto_comp_tag");
	var value = new_task_tag.value;
	value = value.substr(0, value.lastIndexOf(",")+1);
	value += temptag;
	new_task_tag.value = value;
	elm.style.display = "none";
}

function check_date(date)
{
	var temp = date.split("-");
	var d = new Date(parseInt(temp[0]), parseInt(temp[1]) - 1, parseInt(temp[2]));
	if ((d) && ((d.getMonth() + 1) == parseInt(temp[1])) && (d.getDate() == Number(parseInt(temp[2]))))
	{
		datePicker.populateTable(d.getMonth(),d.getFullYear());
		return true;
	}
	else
		return false;
}

function check_submit()
{
	if (new_task_name.checkValidity()&& new_task_username.checkValidity() && 
		birth_date.checkValidity() && new_task_tag.checkValidity() && 
		check_date(birth_date.value))
	{
		document.getElementById("new_task_submit").disabled="";
	}
	else
	{
		document.getElementById("new_task_submit").disabled="disabled";
	}
}
window.onload = function()
{
	datePicker.init(document.getElementById("calendar"), document.getElementById("new_task_form"));
};