function createTaskElement(task) 
{
	// Parse and validate param
	if (task.tags === undefined)
		task.tags = [];
	if (task.done === undefined)
		task.done = false;
	if (task.date === undefined)
		task.date = '';

	var tempstring = "";
		tempstring += "<li>";
			tempstring += '<div class="dashboard_tugas_element">';
				tempstring += '<div class="dashboard_tugas_judul">';
					tempstring += "<a class='task_link' href='task?id="+task.id+"'>";
						tempstring += task.name;
					tempstring += "</a>";
				tempstring += '</div>';
				tempstring += '<div class="dashboard_tugas_tanda">';
					tempstring += "<a href='javascript:void(0);' onclick='checkTask("+task.id+","+!task.done+");'>";
						if (task.done)
						{
							tempstring += "&lt&lt&lt&lt";
						}
						else
						{
							tempstring += "&gt&gt&gt&gt";
						}
					tempstring += "</a>";
				tempstring += '</div>';
				tempstring += '<div class="dashboard_tugas_tanggal">Deadline: '+task.deadline+'</div>';
				tempstring += '<div class="dashboard_tugas_tag_area">';
					tempstring += "<ul>";	
						var tags = task.tags;
						for (var j=0;j<tags.length;++j)
						{
							tempstring += "<li>";
							tempstring += tags[j];
							tempstring += "</li>";
						}
					tempstring += "</ul>";
				tempstring += "</div>";
			tempstring += "</div>";	
		tempstring += "</li>";
	
	return tempstring;
}

function fillTasks(tasks) 
{
	not_donetasksList = document.getElementById('not_done_tasks');
	not_donetasksList.innerHTML = "";
	
	donetasksList = document.getElementById('done_tasks');
	donetasksList.innerHTML = "";
	
	tasks.forEach(function(task) 
	{
		if (task.done)
		{
			donetasksList.innerHTML += createTaskElement(task);
		}
		else
		{
			not_donetasksList.innerHTML += createTaskElement(task);
		}
	});
}

function checkCat(id_kategori, obj)
{
	var ajax_req = ajax();
	ajax_req.onreadystatechange = function()
	{
		if (ajax_req.readyState == 4) 
		{
			var response = JSON.parse(ajax_req.responseText);
			if (response.success)
			{
				fillTasks(response.tasks);

				var new_task_link = document.getElementById("new_task_link");
				if (response.canEditCategory)
				{
					new_task_link.style.display = "block";
					new_task_link.href = "new_task";
						new_task_link.href += "?cat="+(id_kategori);
				}
				else
				{
					new_task_link.style.display = "none";
				}
				
				
            	var categories = document.getElementById("categories");
            	
            	for (var j=0;j<categories.childNodes.length;++j)
				{
            		if (categories.childNodes[j].nodeName!="#text")
					{
						categories.childNodes[j].className = "dashboard_category_element";
					}
				}
            	
            	if (obj!=null)
				{
            		obj.parentNode.className = "dashboard_category_element_aktif";
				}
				else if (id_kategori==0)
				{
					var total = categories.childNodes.length;
					var j = 0;
					var check = true;
					while ((check) && (j<total))
					{
						if (categories.childNodes[j].nodeName!="#text")
						{
							categories.childNodes[j].className = "dashboard_category_element_aktif";
							check = false;
						}
						j++;
					}
				}
            	
            	currentCat = id_kategori;
			}
			else 
			{
				checkCat(0);
			}
		}
		else if (ajax_req.readyState > 0) 
		{
			// Still loading
		}
	};
	
	if (id_kategori!=0)
	{
		ajax_req.get('api/retrieve_tasks?category_id=' + id_kategori);
	}
	else
	{
		ajax_req.get('api/retrieve_tasks');
    }
}

function checkTask(id_task, checked)
{
	var ajax_req = ajax();
	ajax_req.onreadystatechange = function()
	{
		if (ajax_req.readyState == 4) 
		{
			var response = JSON.parse(ajax_req.responseText);
			if (response.success) 
			{
				checkCat(currentCat, null);
			}
			else 
			{
				console.log('Failure to update status of task.');
			}
		}
		else if (ajax_req.readyState > 0) 
		{
			// Still loading
		}
	};
	ajax_req.post('api/mark_task', {
		'taskID': id_task,
		'completed': checked
	});
}

var new_category_name = document.getElementById("new_category_name");

new_category_name.onkeyup = function()
{
	if ((this.checkValidity()))
		this.style.backgroundImage = "url('images/valid.png')";
	else
		this.style.backgroundImage = "url('images/warning.png')";
	check_submit_category();
};

function check_submit_category()
{
	if (new_category_name.checkValidity())
	{
		document.getElementById("new_category_submit").disabled="";
	}
	else
	{
		document.getElementById("new_category_submit").disabled="disabled";
	}
}

//Assignee
var new_category_username = document.getElementById("new_category_username");
new_category_username.setAttribute('autocomplete', 'off');
new_category_username.onkeyup = function()
{
	var value = new_category_username.value;
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
	
	check_submit_category();
};

function choose_assignee (username)
{
	var elm = document.getElementById("auto_comp_assignee");
	var value = new_category_username.value;
	value = value.substr(0, value.lastIndexOf(",")+1);
	value += username;
	new_category_username.value = value;
	elm.style.display = "none";
};

function deleteCat(id_kategori, obj)
{
	var conf = confirm("Yakin ingin menghapus kategori ini?");
	if (conf==true)
	{
		var ajax_req = ajax();
		ajax_req.onreadystatechange = function()
		{
			if (ajax_req.readyState == 4) 
			{
				var response = JSON.parse(ajax_req.responseText);
				if (response.success) 
				{
					checkCat(0, null);
					obj.parentNode.style.display = "none";
				}
				else 
				{
					console.log('Failure to update status of task.');
				}
			}
			else if (ajax_req.readyState > 0) 
			{
				// Still loading
			}
		};
		ajax_req.post("api/delete_category", 'category_id=' + id_kategori);
	}
}

/*Bagian menampilkan pembuatan new_category dan new_task*/
function new_category()
{
	document.getElementById("black_trans").className = "appear";
	document.getElementById("new_category_area").className = "appear";
	document.getElementById("close_button").className = "appear";
	setTimeout(function()
	{
		document.getElementById("frame_new_category").className = "frame_new_category_enter";
	}, 100);
}

function close()
{
	document.getElementById("frame_new_category").className = "";
	setTimeout(function()
	{
		document.getElementById("black_trans").className = "";
		document.getElementById("new_category_area").className = "";
		document.getElementById("close_button").className = "";
	}, 400);
}

/*--- Bagian Validasi Pembuatan Kategori Baru ---*/
var new_category_form = document.getElementById("new_category_form");
new_category_form.onsubmit = function(e)
{
	e.preventDefault();
	if (new_category_name.checkValidity())
	{
		var serialized = serialize2(this);

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
						var categories = document.getElementById("categories");
						categories.innerHTML = "";
						
						append_category("Semua Kategori", 0, false);	
						response.forEach(function(cat) 
						{
							append_category(cat.nama_kategori, cat.id, cat.canDeleteCategory);	
						});
						
						close();
						new_category_form.reset();
						new_category_name.style.backgroundImage = "";
						new_category_username.style.backgroundImage = "";
					}
					catch (e) {
						console.log(e);
					}
					break;
			}
		};
		req.post("api/add_category", serialized);
	}
	else
	{
		alert("Ada kesalahan pada data yang dimasukkan");
	}
	return false;
};

function append_category(category_name, category_id, deleteable)
{
	var categories = document.getElementById("categories");
	var tempstring = "";
	tempstring += "<li class='dashboard_category_element";
	if (category_id==currentCat)
	{
		tempstring += "_aktif";
	}
	tempstring += "'>";
		tempstring += "<a href='javascript:void(0);' onclick='checkCat("+category_id+", this);'>";
			tempstring += category_name;
		tempstring += "</a>";
		if (deleteable)
		{
			tempstring += "<a href='javascript:void(0);' onclick='deleteCat("+category_id+", this);' class='bold right'>";
				tempstring += "X";
			tempstring += "</a>";
		}
	tempstring += "</li>";
	categories.innerHTML += tempstring;
}