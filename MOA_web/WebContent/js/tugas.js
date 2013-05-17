var delete_task = document.getElementById("removeTaskLink");
if (delete_task != undefined)
{
	delete_task.onclick = function()
	{
		var serialized = "task_id="+id_task;
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
						if (response.success)
						{
							window.location = "dashboard";
						}
					}
					catch (e) {
	
					}
					break;
			}
		};
		req.post("api/delete_task", serialized);
		return false;
	};
}

var edit_task_status = document.getElementById("editTaskStatusLink");
if (edit_task_status != undefined)
{
	edit_task_status.onclick = function()
	{
		var serialized = "taskID="+id_task+"&completed="+checked;
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
						if (response.done == 0)
						{
							checked = true;
							document.getElementById("task_status").innerHTML = "belum selesai";
							document.getElementById("task_status").className = "status_not_done";
						}
						else if (response.done == 1)
						{
							checked = false;
							document.getElementById("task_status").innerHTML = "selesai";
							document.getElementById("task_status").className = "status_done";
						}
					}
					catch (e) {
	
					}
					break;
			}
		};
		req.post("api/mark_task", serialized);
		return false;
	};
}

function retrieve_detail_task()
{
	var month = ["January","February","March","April","May","June",
				"July","August","September","October","November","December"];
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
					document.getElementById("task_name").innerHTML = response.nama_task;
					date = new Date(response.deadline.substr(0,4),(parseInt(response.deadline.substr(5,2)))-1,parseInt(response.deadline.substr(8,2)));
					document.getElementById("birth_date").innerHTML = date.getDate()+" "+month[date.getMonth()]+" "+date.getFullYear();
					var string = "";
					for (var i in response.asignee)
					{
						string += "<a href='profile?id="+response.asignee[i].id_user+"'>"+response.asignee[i].username+"</a>,";
					}
					string = string.substr(0,string.length-1);
					document.getElementById("task_username").innerHTML = string;
					var tag_string = "";
					for (var i in response.tag)
					{
						tag_string += response.tag[i]+",";
					}
					tag_string = tag_string.substr(0,tag_string.length-1);
					document.getElementById("task_tag").innerHTML = tag_string;
				}
				catch (e) {

				}
				break;
		}
	};
	req.get('api/get_task?id_task=' + id_task);
}

window.onload = function()
{
	setInterval(function(){retrieve_detail_task();},10000);
};
